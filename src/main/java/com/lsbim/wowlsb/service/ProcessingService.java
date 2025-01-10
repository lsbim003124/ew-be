package com.lsbim.wowlsb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusEnemyCastsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusPlayerCastsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusRankingsDTO;
import com.lsbim.wowlsb.service.events.BuffsService;
import com.lsbim.wowlsb.service.events.CastsService;
import com.lsbim.wowlsb.service.repository.SpellService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProcessingService {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private CastsService castsService;

    @Autowired
    private FightsService fightsService;

    @Autowired
    private BuffsService buffsService;

    @Autowired
    private RankingsService rankingsService;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpellService spellService;


    public ObjectNode doProcessing(String className, String spec, int dungeonId) {
        Set<Integer> usedPlayerSkillIds = new HashSet<>();
        Set<Integer> usedBossSkillIds = new HashSet<>();
        Set<Integer> takenSkillIds = new HashSet<>();

        MplusRankingsDTO rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        List<MplusRankingsDTO.Ranking> rankings = rankingsDTO.getRankings();

//        1~10등 모든 풀링, 모든 생존기재 캐스트까지 요청, **보스는 1등만!!!**
        for (int i = 0; i < rankings.size(); i++) {
            MplusRankingsDTO.Ranking ranking = rankings.get(i);
            String name = ranking.getName();
            String code = ranking.getReport().getCode();
            int fightId = ranking.getReport().getFightID();

            MplusFightsDTO fightsDTO = fightsService.getMplusFights(code, fightId);
            ranking.setMplusFightsDTO(fightsDTO);

            int actorId = playerService.getMplusActorId(code, fightId, className, spec, name);

            for (MplusFightsDTO.Pull pull : fightsDTO.getPulls()) {
                if (pull.getEvents() == null) {
                    pull.setEvents(new MplusFightsDTO.Pull.Events()); // Events 객체 초기화
                }

                // 인덱스가 0일 때만 보스 캐스트 가져오기
                if (i == 0) {
                    List<MplusEnemyCastsDTO> allEnemyCasts = new ArrayList<>();
                    List<Integer> bossNpcIds = dungeonService.findBossIdByList(pull.getEnemyNpcs(), dungeonId);
                    for (int bossNpcId : bossNpcIds) { // 보스가 여럿일때를 가장
                        List<MplusEnemyCastsDTO> enemyCastsDTO = castsService.getEnemyCasts(
                                code, fightId,
                                pull.getStartTime(),
                                pull.getEndTime(),
                                bossNpcId, actorId
                        );
                        allEnemyCasts.addAll(enemyCastsDTO); // 보스 개인의 스킬 목록을 옮겨담기
                    }
                    pull.getEvents().setEnemyCastsDTO(allEnemyCasts); // 모든 보스 스킬 등록

                    // 실제로 사용한 주문만 Set에 넣기
                    if (allEnemyCasts.size() > 0) { // 배열이 유효하면
                        usedBossSkillIds.addAll(allEnemyCasts.stream()
                                .map(cast -> cast.getAbilityGameID())
                                .collect(Collectors.toSet()));
                    }

                    if (usedBossSkillIds.size() > 0) {
                        //        스킬목록 추가
                        rankingsDTO.setBossSkillInfo(usedBossSkillIds);
                    }
                }

                // 받은 외생기 가져오기
                List<List<MplusPlayerCastsDTO>> takenBuffs = buffsService.getTakenBuffs(
                        code, fightId
                        , pull.getStartTime()
                        , pull.getEndTime()
                        , actorId);
                pull.getEvents().setPlayerTakenDefensiveDTO(takenBuffs);

//                해당 직업의 생존기 목록불러오기
                List<Integer> defList = playerService.findDefensive(className, spec);

//                생존기 주문번호에 해당하는 cast 기록 가져오기
                List<List<MplusPlayerCastsDTO>> defensiveCasts = castsService.getPlayerDefensiveCasts(
                        code, fightId
                        , pull.getStartTime()
                        , pull.getEndTime()
                        , actorId
                        , defList, className);

                pull.getEvents().setPlayerDefensiveCastsDTO(defensiveCasts);

                // 실제로 사용한 주문만 Set에 넣기
                for (List<MplusPlayerCastsDTO> defensiveCast : defensiveCasts) {
                    if (defensiveCast.size() > 0) { // 배열이 유효하면
                        usedPlayerSkillIds.addAll(defensiveCast.stream()
                                .map(cast -> cast.getAbilityGameID())
                                .collect(Collectors.toSet()));
                    }
                }

                // 받은 외생기 주문 Set에 넣기
                for (List<MplusPlayerCastsDTO> buff : takenBuffs) {
                    if (buff.size() > 0) { // 배열이 유효하면
                        takenSkillIds.addAll(buff.stream()
                                .map(cast -> cast.getAbilityGameID())
                                .collect(Collectors.toSet()));
                    }
                }
            }
        }
        if (usedPlayerSkillIds.size() > 0) {
            // 실제 사용한 스킬목록 추가
            rankingsDTO.setPlayerSkillInfo(spellService.getBySpellIds(usedPlayerSkillIds));
        }
        if (takenSkillIds.size() > 0) {
            // 실제 적용된 외생기목록 추가
            rankingsDTO.setTakenBuffInfo(spellService.getBySpellIds(takenSkillIds));
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = dateFormat.format(new Date());
        rankingsDTO.setDataTime(date); // 데이터가 만들어진 시간

        ObjectNode objectNode = om.valueToTree(rankingsDTO);

        return objectNode;
    }
}
