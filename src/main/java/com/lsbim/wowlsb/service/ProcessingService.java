package com.lsbim.wowlsb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.*;
import com.lsbim.wowlsb.dto.mplus.pamameter.CodeAndFightIdDTO;
import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.service.wcl.events.BuffsService;
import com.lsbim.wowlsb.service.wcl.events.CastsService;
import com.lsbim.wowlsb.service.repository.SpellService;
import com.lsbim.wowlsb.service.wcl.FightsService;
import com.lsbim.wowlsb.service.wcl.PlayerService;
import com.lsbim.wowlsb.service.wcl.RankingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProcessingService {

    private final PlayerService playerService;
    private final CastsService castsService;
    private final FightsService fightsService;
    private final BuffsService buffsService;
    private final RankingsService rankingsService;
    private final SpellService spellService;
    @Qualifier("imageUploadExecutor")
    private final Executor imageUploadExecutor;

    @Autowired
    private ObjectMapper om;


    public ObjectNode mplusTimelineProcessing(String className, String spec, int dungeonId) {
        Set<Integer> usedPlayerSkillIds = new HashSet<>();
        Set<Integer> usedBossSkillIds = new HashSet<>();
        Set<Integer> takenSkillIds = new HashSet<>();
        Set<Integer> takenBloodlusts = new HashSet<>();

        String processingKey = className + " " + spec + " " + dungeonId;
        log.info("Start processing. key: {}", processingKey);

//        그래프 buffs 이미지 수거용 Set
        Set<Integer> buffs = new HashSet<>();

//        WCL 쐐기 전문화+던전 1~10위 랭킹 목록, 여기서 예외 발생 시 Queue에서 처리중
        MplusRankingsDTO rankingsDTO;

        try {
            rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("No Rankings Data: {}", processingKey, e.getMessage());
            return null;
        }

        if (rankingsDTO == null) {
            log.warn("No Rankings Data: {}", processingKey);
            return null;
        }

        // 해당 직업의 생존기 목록불러오기
        List<Integer> defList = playerService.findDefensive(className, spec);

        // 1~10등 유저의 닉네임 배열
        List<String> names = rankingsDTO.getRankings().stream()
                .map(MplusRankingsDTO.Ranking::getName)
                .collect(Collectors.toList());
        int rankingSize = rankingsDTO.getRankings().size();

//        1~10등의 개인별 데이터 가져오는 반복문
        for (int rankingsIndex = 0; rankingsIndex < rankingSize; ) {
            try {
                MplusRankingsDTO.Ranking ranking = rankingsDTO.getRankings().get(rankingsIndex);

                // [code, fightId]
                CodeAndFightIdDTO fightParamDTO = CodeAndFightIdDTO.fromRankings(ranking);

//                  fight데이터
                MplusFightsDTO fightsDTO = fightsService.getMplusFights(fightParamDTO);
                if (fightsDTO == null) {
                    log.warn("No fights Data: {}", processingKey);
                    rankingsIndex++;
                    continue;
                }

//                  유저 actorId
                int actorId = playerService.getMplusActorId(fightParamDTO, className, spec, names.get(rankingsIndex));

//                  유저의 pull+boss별 보스 캐스트 데이터
                List<List<MplusEnemyCastsDTO>> enemyCasts = castsService.getEnemyCasts(
                        fightParamDTO
                        , fightsDTO
                        , dungeonId
                        , actorId
                );

                // 실제로 사용한 주문만 Set에 넣기
                enemyCasts.stream()        // List<List<MplusEnemyCastsDTO>>
                        .flatMap(List::stream)                         // 스트림 평탄화: Stream<MplusEnemyCastsDTO>
                        .filter(Objects::nonNull)
                        .mapToInt(MplusEnemyCastsDTO::getAbilityGameID)     // 각 cast 객체에서 abilityGameID 추출
                        .forEach(usedBossSkillIds::add);

                // 받은 외생기 가져오기
                // [pull별][외생기 주문별]
                List<List<List<MplusPlayerCastsDTO>>> takenBuffs = buffsService.getTakenBuffs(
                        fightParamDTO
                        , fightsDTO
                        , actorId
                );

                // 블러드
                List<List<List<MplusPlayerCastsDTO>>> takenBlood = buffsService.getTakenBloodlusts(
                        fightParamDTO
                        , fightsDTO
                        , actorId
                );

//                    사용한 자생기 가져오기
                List<List<List<MplusPlayerCastsDTO>>> defensiveCasts = castsService.getPlayerDefensiveCasts(
                        fightParamDTO
                        , fightsDTO
                        , actorId
                        , defList);

//                    유저 하나의 fightsDTO에 Pull마다 데이터 넣기
                MplusFightsDTO processFightsDTO = processFightsData(fightsDTO
                        , enemyCasts
                        , takenBuffs
                        , defensiveCasts
                        , takenBlood);
//                    ranking에 fightsDTO 삽입
                ranking.setMplusFightsDTO(processFightsDTO);

//                    받은 외생기 Set에 넣기
                takenBuffs.stream()
                        .flatMap(List::stream)
                        .flatMap(List::stream)
                        .filter(Objects::nonNull)
                        .mapToInt(MplusPlayerCastsDTO::getAbilityGameID)
                        .forEach(takenSkillIds::add);
                takenBlood.stream()
                        .flatMap(List::stream)
                        .flatMap(List::stream)
                        .filter(Objects::nonNull)
                        .mapToInt(MplusPlayerCastsDTO::getAbilityGameID)
                        .forEach(takenBloodlusts::add);
//                    사용한 자생기 Set에 넣기
                defensiveCasts.stream()
                        .flatMap(List::stream)
                        .flatMap(List::stream)
                        .filter(Objects::nonNull)
                        .mapToInt(MplusPlayerCastsDTO::getAbilityGameID)
                        .forEach(usedPlayerSkillIds::add);

//                  작업 후 인덱스 증가
                rankingsIndex++;
            } catch (HttpClientErrorException.TooManyRequests e) {
                log.warn("Too Many Requests index {}, Waiting for 2m before retrying.", rankingsIndex, e.getMessage());
                try {
                    Thread.sleep(120000); // 2분 대기
                } catch (InterruptedException ie) {
                    log.warn("Sleep interrupted: ", ie);
                    Thread.currentThread().interrupt();
                }

            } catch (Exception e) {
//                WCL 503, 504 발생 있음
                log.warn("Failed process timeline data... {}", processingKey);
                log.warn(e.getMessage());
                try {
                    Thread.sleep(60000); // 1분 대기
                } catch (InterruptedException ie) {
                    log.warn("Sleep interrupted: ", ie);
                    Thread.currentThread().interrupt();
                }
            }

            log.info("{}/{} Complete get Rakings Data, {}", rankingsIndex, rankingSize, processingKey);
        } // 랭킹 수만큼 유저데이터 가공 종료


        if (usedPlayerSkillIds.size() > 0) {
            // 실제 사용한 스킬목록 추가
            rankingsDTO.setPlayerSkillInfo(spellService.getBySpellIds(usedPlayerSkillIds));
        }
        if (takenSkillIds.size() > 0) {
            // 실제 적용된 외생기목록 추가
            rankingsDTO.setTakenBuffInfo(spellService.getBySpellIds(takenSkillIds));
        }
        if (takenBloodlusts.size() > 0) {
            // 블러드 목록 추가
            rankingsDTO.setTakenBloodlusts(spellService.getBySpellIds(takenBloodlusts));
        }
        if (usedBossSkillIds.size() > 0) {
            // 보스 사용 스킬목록 추가
            rankingsDTO.setBossSkillInfo(usedBossSkillIds);
        }

//            GCS 이미지 삽입은 따로
        CompletableFuture.supplyAsync(() -> {
            List<Spell> newSpells = spellService.getBySpellIds(buffs);
            log.info("Get New '{}'size Spells ", newSpells.size());

            return newSpells;
        }, imageUploadExecutor);

//            기다리지 않고 즉시 리턴
        ObjectNode objectNode = om.valueToTree(rankingsDTO);

        return objectNode;

    }

    private MplusFightsDTO processFightsData(MplusFightsDTO fightsDTO
            , List<List<MplusEnemyCastsDTO>> enemyCasts
            , List<List<List<MplusPlayerCastsDTO>>> takenBuffs
            , List<List<List<MplusPlayerCastsDTO>>> defensiveCasts
            , List<List<List<MplusPlayerCastsDTO>>> takenBlood) {

//        pull 크기만큼 반복문
        for (int k = 0; k < fightsDTO.getPulls().size(); k++) {
            MplusFightsDTO.Pull pull = fightsDTO.getPulls().get(k);
            if (pull.getEvents() == null) {
                pull.setEvents(new MplusFightsDTO.Pull.Events()); // Events 객체 초기화
            }

//                보스 캐스트 데이터
            pull.getEvents().setEnemyCastsDTO(enemyCasts.get(k));
//                받은 외생기 데이터
            pull.getEvents().setPlayerTakenDefensiveDTO(takenBuffs.get(k));
//                블러드 러스트
            pull.getEvents().setBloodlusts(takenBlood.get(k));
//                사용한 자생기 데이터
            pull.getEvents().setPlayerDefensiveCastsDTO(defensiveCasts.get(k));

        } // pull 가공 종료

        return fightsDTO;
    }
}
