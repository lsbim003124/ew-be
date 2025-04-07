package com.lsbim.wowlsb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.*;
import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.service.wcl.events.BuffsService;
import com.lsbim.wowlsb.service.wcl.events.CastsService;
import com.lsbim.wowlsb.service.repository.SpellService;
import com.lsbim.wowlsb.service.wcl.DungeonService;
import com.lsbim.wowlsb.service.wcl.FightsService;
import com.lsbim.wowlsb.service.wcl.PlayerService;
import com.lsbim.wowlsb.service.wcl.RankingsService;
import com.lsbim.wowlsb.service.wcl.events.ResourcesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProcessingService {

    private final PlayerService playerService;
    private final DungeonService dungeonService;
    private final CastsService castsService;
    private final FightsService fightsService;
    private final BuffsService buffsService;
    private final RankingsService rankingsService;
    private final SpellService spellService;
    private final ResourcesService resourcesService;

    @Autowired
    private ObjectMapper om;


    public ObjectNode doProcessing(String className, String spec, int dungeonId) {
        Set<Integer> usedPlayerSkillIds = new HashSet<>();
        Set<Integer> usedBossSkillIds = new HashSet<>();
        Set<Integer> takenSkillIds = new HashSet<>();
        String processingKey = className + " " + spec + " " + dungeonId;

//        그래프 buffs 이미지 수거용 Set
        Set<Integer> buffs = new HashSet<>();

        MplusRankingsDTO rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        log.info("rankingsDTO: {}", rankingsDTO);
        if (rankingsDTO == null) {
            return null;
        }
        List<MplusRankingsDTO.Ranking> rankings = rankingsDTO.getRankings();

//        1~10등 모든 풀링, 모든 생존기재 캐스트까지 요청, **보스는 1등만!!!**
        for (int i = 0; i < rankings.size(); ) {
            try {
                MplusRankingsDTO.Ranking ranking = rankings.get(i);
                String name = ranking.getName();
                String code = ranking.getReport().getCode();
                int fightId = ranking.getReport().getFightID();

                MplusFightsDTO fightsDTO = fightsService.getMplusFights(code, fightId);
                if (fightsDTO == null) {
                    log.warn("Failed get fights data code: {}, fightId: {} skip this index", code, fightId);
                    continue;
                }

                ranking.setMplusFightsDTO(fightsDTO);

                int actorId = playerService.getMplusActorId(code, fightId, className, spec, name);

                // 보스 마리 당 pull 단위로 쪼개진 상태
                for (MplusFightsDTO.Pull pull : fightsDTO.getPulls()) {
                    if (pull.getEvents() == null) {
                        pull.setEvents(new MplusFightsDTO.Pull.Events()); // Events 객체 초기화
                    }

/*                    // 유저랭킹 인덱스가 0일 때만 보스 캐스트 가져오기
                    if (i == 0) {*/
                    List<MplusEnemyCastsDTO> allEnemyCasts = new ArrayList<>();
                    List<Integer> bossNpcIds = dungeonService.findBossIdByList(pull.getEnemyNpcs(), dungeonId);
                    for (int bossNpcId : bossNpcIds) { // 보스가 여럿일때를 가장
                        List<MplusEnemyCastsDTO> enemyCastsDTO = castsService.getEnemyCasts(
                                code, fightId,
                                pull.getStartTime(),
                                pull.getEndTime(),
                                bossNpcId, actorId
                        );
                        if (enemyCastsDTO.isEmpty() || enemyCastsDTO.size() == 0) {
                            log.warn("Failed get enemy casts bossNpcId: {}. skip this boss.", bossNpcId);
                            continue;
                        }

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
//                    } // 1위 보스캐스트 종료

//                    유저별 체력의 변화 그래프, 받은 피해 기록 가져오기
                    MplusResourcesGraphDTO resourcesGraphDTO = resourcesService.getResourcesData(
                            code, fightId, pull.getStartTime(), pull.getEndTime(), actorId);
                    pull.getEvents().setMplusResourcesGraphDTO(resourcesGraphDTO); // fights.pull.events에 이벤트 삽입

                    Set<Integer> buffIds = resourcesService.getResourcesBuffIds(resourcesGraphDTO.getDamageTakenDataList());
                    buffs.addAll(buffIds);

                    // 받은 외생기 가져오기
                    List<List<MplusPlayerCastsDTO>> takenBuffs = buffsService.getTakenBuffs(
                            code, fightId
                            , pull.getStartTime()
                            , pull.getEndTime()
                            , actorId);
                    pull.getEvents().setPlayerTakenDefensiveDTO(takenBuffs); // fights.pull.events에 이벤트 삽입

//                해당 직업의 생존기 목록불러오기
                    List<Integer> defList = playerService.findDefensive(className, spec);

//                생존기 주문번호에 해당하는 cast 기록 가져오기
                    List<List<MplusPlayerCastsDTO>> defensiveCasts = castsService.getPlayerDefensiveCasts(
                            code, fightId
                            , pull.getStartTime()
                            , pull.getEndTime()
                            , actorId
                            , defList, className);

                    pull.getEvents().setPlayerDefensiveCastsDTO(defensiveCasts); // fights.pull.events에 이벤트 삽입

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

                i++; // 모든 작업이 완료되면 인덱스 증가
            } catch (HttpClientErrorException.TooManyRequests e) {
                log.error("Too Many Requests index {}, Waiting for 1m before retrying.", i, e);
                try {
                    Thread.sleep(60000); // 1분 대기
                } catch (InterruptedException ie) {
                    log.error("Sleep interrupted: ", ie);
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                log.error("Error processing ranking index {}: {}", i, e.getMessage());
                i++; // 에러 발생 시 인덱스 넘어가기
            }
            log.info("{}/{} Complete Rakings Data, {}", i, rankings.size(), processingKey);
        } // 1~10등 데이터 가공 종료
        
        if (usedPlayerSkillIds.size() > 0) {
            // 실제 사용한 스킬목록 추가
            rankingsDTO.setPlayerSkillInfo(spellService.getBySpellIds(usedPlayerSkillIds));
        }
        if (takenSkillIds.size() > 0) {
            // 실제 적용된 외생기목록 추가
            rankingsDTO.setTakenBuffInfo(spellService.getBySpellIds(takenSkillIds));
        }

        List<Spell> newSpells =  spellService.getBySpellIds(buffs);
        log.info("Get New {} Spells ",newSpells.size());

        ObjectNode objectNode = om.valueToTree(rankingsDTO);

        return objectNode;
    }
}
