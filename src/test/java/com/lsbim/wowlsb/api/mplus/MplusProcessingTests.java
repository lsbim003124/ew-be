package com.lsbim.wowlsb.api.mplus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.*;
import com.lsbim.wowlsb.dto.mplus.pamameter.CodeAndFightIdDTO;
import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.service.repository.SpellService;
import com.lsbim.wowlsb.service.wcl.events.BuffsService;
import com.lsbim.wowlsb.service.wcl.events.CastsService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import com.lsbim.wowlsb.service.wcl.DungeonService;
import com.lsbim.wowlsb.service.wcl.FightsService;
import com.lsbim.wowlsb.service.wcl.PlayerService;
import com.lsbim.wowlsb.service.wcl.RankingsService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class MplusProcessingTests {

//    데이터 파일로 저장해서 뽑기는 Mplus File Save Tests로

    @Autowired
    private MplusTimelineDataService mplusTimelineDataService;

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
    private SpellService spellService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

/*    @Test
    public void processingTest6() {
        int dungeonId = 62293;
        String className = "Druid";
        String spec = "Balance";

        Set<Integer> usedPlayerSkillIds = new HashSet<>();
        Set<Integer> usedBossSkillIds = new HashSet<>();
        Set<Integer> takenSkillIds = new HashSet<>();

        String processingKey = className + " " + spec + " " + dungeonId;

//        그래프 buffs 이미지 수거용 Set
        Set<Integer> buffs = new HashSet<>();

//        WCL 쐐기 전문화+던전 1~10위 랭킹 목록
        MplusRankingsDTO rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        if (rankingsDTO == null) {
            log.warn("No Rankings Data: {}", processingKey);
//            return null;
        }

        // 해당 직업의 생존기 목록불러오기
        List<Integer> defList = playerService.findDefensive(className, spec);

        // 1~10등 유저의 닉네임 배열
        List<String> names = rankingsDTO.getRankings().stream()
                .map(MplusRankingsDTO.Ranking::getName)
                .collect(Collectors.toList());
        int rankingSize = rankingsDTO.getRankings().size();

        try {
            for (int rankingsIndex = 0; rankingsIndex < rankingSize; ) {
                try {
                    MplusRankingsDTO.Ranking ranking = rankingsDTO.getRankings().get(rankingsIndex);

                    // [code, fightId]
                    CodeAndFightIdDTO fightParamDTO = CodeAndFightIdDTO.fromRankings(ranking);

//                  fight데이터
                    MplusFightsDTO fightsDTO = fightsService.getMplusFights2(fightParamDTO);
                    if (fightsDTO == null) {
                        log.warn("No fights Data: {}", processingKey);
//                      return null;
                    }

//                  유저 actorId
                    int actorId = playerService.getMplusActorId2(fightParamDTO, className, spec, names.get(rankingsIndex));

//                  유저의 pull+boss별 보스 캐스트 데이터
                    List<List<MplusEnemyCastsDTO>> enemyCasts = castsService.getEnemyCasts2(
                            fightParamDTO
                            , fightsDTO
                            , dungeonId
                            , actorId
                    );
                    if (enemyCasts == null) {
                        log.warn("No enemyCasts Data: {}", processingKey);
//            return null;
                    }

//        log.info("enemyCasts: {}", enemyCasts);

                    // 실제로 사용한 주문만 Set에 넣기
                    enemyCasts.stream()        // List<List<MplusEnemyCastsDTO>>
                            .flatMap(List::stream)                         // 스트림 평탄화: Stream<MplusEnemyCastsDTO>
                            .filter(Objects::nonNull)
                            .mapToInt(MplusEnemyCastsDTO::getAbilityGameID)     // 각 cast 객체에서 abilityGameID 추출
                            .forEach(usedBossSkillIds::add);

//                  유저의 pull별 체력 그래프 데이터
                    List<MplusResourcesGraphDTO> resourcesGraphDTO = resourcesService.getResourcesData2(
                            fightParamDTO
                            , fightsDTO
                            , actorId
                    );
                    if (resourcesGraphDTO == null) {
                        log.warn("No resourcesGraph Data: {}", processingKey);
//                      return null;
                    }

//                  받은 피해 기록의 buffs 아이콘 수집을 위한 spellId를 Set에
                    resourcesGraphDTO.stream()
                            .filter(Objects::nonNull)
                            .forEach(graph -> {
                                Set<Integer> buffIds = resourcesService.getResourcesBuffIds(graph.getDamageTakenDataList());
                                buffs.addAll(buffIds);
                            });

                    // 받은 외생기 가져오기
                    // [pull별][외생기 주문별]
                    List<List<List<MplusPlayerCastsDTO>>> takenBuffs = buffsService.getTakenBuffs2(
                            fightParamDTO
                            , fightsDTO
                            , actorId
                    );
                    if (takenBuffs == null) {
                        log.warn("No takenBuffs Data: {}", processingKey);
//            return null;
                    }

//                    사용한 자생기 가져오기
                    List<List<List<MplusPlayerCastsDTO>>> defensiveCasts = castsService.getPlayerDefensiveCasts2(
                            fightParamDTO
                            , fightsDTO
                            , actorId
                            , defList);
                    if (defensiveCasts == null) {
                        log.warn("No defensiveCasts Data: {}", processingKey);
//            return null;
                    }

//                    유저 하나의 fightsDTO에 Pull마다 데이터 넣기
                    MplusFightsDTO processFightsDTO = processFightsData(fightsDTO
                            , enemyCasts
                            , resourcesGraphDTO
                            , takenBuffs
                            , defensiveCasts);
//                    ranking에 fightsDTO 삽입
                    ranking.setMplusFightsDTO(processFightsDTO);

//                    받은 외생기 Set에 넣기
                    takenBuffs.stream()
                            .flatMap(List::stream)
                            .flatMap(List::stream)
                            .filter(Objects::nonNull)
                            .mapToInt(MplusPlayerCastsDTO::getAbilityGameID)
                            .forEach(takenSkillIds::add);
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
                    log.error("Too Many Requests index {}, Waiting for 1m before retrying.", rankingsIndex, e);
                    try {
                        Thread.sleep(60000); // 1분 대기
                    } catch (InterruptedException ie) {
                        log.error("Sleep interrupted: ", ie);
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    log.warn("에러", e);
                    rankingsIndex++;
                }
                log.info("{}/{} Complete Rakings Data, {}", rankingsIndex, rankingSize, processingKey);
            }


            if (usedPlayerSkillIds.size() > 0) {
                // 실제 사용한 스킬목록 추가
                rankingsDTO.setPlayerSkillInfo(spellService.getBySpellIds(usedPlayerSkillIds));
            }
            if (takenSkillIds.size() > 0) {
                // 실제 적용된 외생기목록 추가
                rankingsDTO.setTakenBuffInfo(spellService.getBySpellIds(takenSkillIds));
            }

            List<Spell> newSpells = spellService.getBySpellIds(buffs);
            log.info("Get New '{}'size Spells ", newSpells.size());

            ObjectNode objectNode = om.valueToTree(rankingsDTO);

            log.info(objectNode);
//        return objectNode;
        } catch (Exception e) {
            log.error("Error processing MplusTimelineData: '{}'", processingKey, e);
//            return null;
        }
    }*/

  /*  private MplusFightsDTO processFightsData(MplusFightsDTO fightsDTO
            , List<List<MplusEnemyCastsDTO>> enemyCasts
            , List<MplusResourcesGraphDTO> resourcesGraphDTO
            , List<List<List<MplusPlayerCastsDTO>>> takenBuffs
            , List<List<List<MplusPlayerCastsDTO>>> defensiveCasts) {

//        pull 크기만큼 반복문
        for (int k = 0; k < fightsDTO.getPulls().size(); k++) {
            MplusFightsDTO.Pull pull = fightsDTO.getPulls().get(k);
            if (pull.getEvents() == null) {
                pull.setEvents(new MplusFightsDTO.Pull.Events()); // Events 객체 초기화
            }

//                보스 캐스트 데이터
            pull.getEvents().setEnemyCastsDTO(enemyCasts.get(k));
//                체력 그래프 데이터
            pull.getEvents().setMplusResourcesGraphDTO(resourcesGraphDTO.get(k));
//                받은 외생기 데이터
            pull.getEvents().setPlayerTakenDefensiveDTO(takenBuffs.get(k));
//                사용한 자생기 데이터
            pull.getEvents().setPlayerDefensiveCastsDTO(defensiveCasts.get(k));

        } // pull 가공 종료

        return fightsDTO;
    }*/

/*    @Test
    public void processingTest1() {

        int dungeonId = 12669;
        String className = "Priest";
        String spec = "Discipline";

        MplusRankingsDTO rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        log.info(rankingsDTO);

//        1~10등 모든 풀링, 모든 생존기재 캐스트까지 요청
        for (MplusRankingsDTO.Ranking ranking : rankingsDTO.getRankings()) {
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


                List<Integer> bossNpcIds = dungeonService.findBossIdByList(pull.getEnemyNpcs(), dungeonId);
                for (int bossNpcId : bossNpcIds) {

                    List<MplusEnemyCastsDTO> enemyCastsDTO = castsService.getEnemyCasts(
                            code, fightId
                            , pull.getStartTime()
                            , pull.getEndTime()
                            , bossNpcId, actorId);
                    pull.getEvents().setEnemyCastsDTO(enemyCastsDTO);
                }

                List<Integer> defList = playerService.findDefensive(className, spec);

                List<List<MplusPlayerCastsDTO>> defensiveCasts = castsService.getPlayerDefensiveCasts(
                        code, fightId
                        , pull.getStartTime()
                        , pull.getEndTime()
                        , actorId
                        , defList, className);

                pull.getEvents().setPlayerDefensiveCastsDTO(defensiveCasts);
            }
        }

//        log.info(rankingsDTO);

        ObjectNode objectNode = om.valueToTree(rankingsDTO);


        try {
            // JSON 문자열을 보기 좋게 포맷팅
            String prettyJson = om.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(objectNode);

            // 파일로 저장
            om.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("mplus_rankings.json"), objectNode);

            log.info("Rankings data has been saved to mplus_rankings.json");
        } catch (Exception e) {
            log.error("Failed to write rankings data to file", e);
        }
    }*/

    /*@Test
    public void processingTest2() {

        int dungeonId = 12669;
        String className = "Priest";
        String spec = "Discipline";

        MplusRankingsDTO rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        log.info(rankingsDTO);

//        1등 모든 풀링, 모든 생존기재 캐스트까지 요청

        MplusRankingsDTO.Ranking ranking = rankingsDTO.getRankings().get(0);
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

//                GameId
            int bossGameId = dungeonService.findBossGameIdByName(pull.getName(), dungeonId);
            pull.setBossGameId(bossGameId); // 보스 npcId 삽입

//                ActorId
            List<Integer> bossNpcIds = dungeonService.findBossIdByList(pull.getEnemyNpcs(), dungeonId);
            for (int bossNpcId : bossNpcIds) {

                List<MplusEnemyCastsDTO> enemyCastsDTO = castsService.getEnemyCasts(
                        code, fightId
                        , pull.getStartTime()
                        , pull.getEndTime()
                        , bossNpcId, actorId);

                // enemyCastsDTO 리스트가 null인 경우 초기화
                if (pull.getEvents().getEnemyCastsDTO() == null) {
                    pull.getEvents().setEnemyCastsDTO(new ArrayList<>());
                }

                // 기존 리스트에 새로운 리스트를 추가
                pull.getEvents().getEnemyCastsDTO().addAll(enemyCastsDTO);
            }

            List<Integer> defList = playerService.findDefensive(className, spec);
//                log.info(defList);

            List<List<MplusPlayerCastsDTO>> defensiveCasts = castsService.getPlayerDefensiveCasts(
                    code, fightId
                    , pull.getStartTime()
                    , pull.getEndTime()
                    , actorId
                    , defList, className);
//                log.info(defensiveCasts);
            pull.getEvents().setPlayerDefensiveCastsDTO(defensiveCasts);
        }

        log.info(rankingsDTO);

        ObjectNode objectNode = om.valueToTree(rankingsDTO);
        log.info(objectNode);
    }*/

/*    @Test
    public void processingTest3() {

        int dungeonId = 12669;
        String className = "Priest";
        String spec = "Discipline";
        Set<Integer> usedSkillIds = new HashSet<>();

        MplusRankingsDTO rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        List<MplusRankingsDTO.Ranking> rankings = rankingsDTO.getRankings();
//        log.info(rankingsDTO);

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

                // 인덱스가 0일 때만
                if (i == 0) {
                    List<Integer> bossNpcIds = dungeonService.findBossIdByList(pull.getEnemyNpcs(), dungeonId);
                    for (int bossNpcId : bossNpcIds) {
                        List<MplusEnemyCastsDTO> enemyCastsDTO = castsService.getEnemyCasts(
                                code, fightId,
                                pull.getStartTime(),
                                pull.getEndTime(),
                                bossNpcId, actorId
                        );
                        pull.getEvents().setEnemyCastsDTO(enemyCastsDTO);
                    }
                }

                List<Integer> defList = playerService.findDefensive(className, spec);

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
                        usedSkillIds.addAll(defensiveCast.stream()
                                .map(cast -> cast.getAbilityGameID())
                                .collect(Collectors.toSet()));
                    }
                }
            }
        }
//        log.info(usedSkillIds);
        //        스킬목록 추가
//        rankingsDTO.setPlayerSkillInfo(playerService.getPlayerSkillList(className, spec, usedSkillIds));
//        log.info(rankingsDTO);

        ObjectNode objectNode = om.valueToTree(rankingsDTO);

    }

    @Test
    public void processingTest4() {
//  API 호출횟수 계산용 테스트
        int dungeonId = 12669;
        String className = "Priest";
        String spec = "Discipline";
        Set<Integer> usedSkillIds = new HashSet<>();
        int i = 0;

        MplusRankingsDTO rankingsDTO = rankingsService.getMplusRankings(dungeonId, className, spec);
        List<MplusRankingsDTO.Ranking> rankings = rankingsDTO.getRankings();
//        log.info(rankingsDTO);

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

            // 인덱스가 0일 때만
            if (i == 0) {
                List<Integer> bossNpcIds = dungeonService.findBossIdByList(pull.getEnemyNpcs(), dungeonId);
                for (int bossNpcId : bossNpcIds) {
                    List<MplusEnemyCastsDTO> enemyCastsDTO = castsService.getEnemyCasts(
                            code, fightId,
                            pull.getStartTime(),
                            pull.getEndTime(),
                            bossNpcId, actorId
                    );
                    pull.getEvents().setEnemyCastsDTO(enemyCastsDTO);
                }
            }

            List<Integer> defList = playerService.findDefensive(className, spec);

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
                    usedSkillIds.addAll(defensiveCast.stream()
                            .map(cast -> cast.getAbilityGameID())
                            .collect(Collectors.toSet()));
                }
            }
        }

//        log.info(usedSkillIds);
        //        스킬목록 추가
//        rankingsDTO.setPlayerSkillInfo(playerService.getPlayerSkillList(className, spec, usedSkillIds));
//        log.info(rankingsDTO);

        ObjectNode objectNode = om.valueToTree(rankingsDTO);
        log.info(objectNode);
    }

    // 큐 테스트용
    @Test
    public void processingTest5() {

        int dungeonId1 = 12669;
        int dungeonId2 = 12662;
        int dungeonId3 = 12660;
        int dungeonId4 = 12652;
        int dungeonId5 = 62290;
        int dungeonId6 = 62286;
        int dungeonId7 = 61822;
        int dungeonId8 = 60670;
        String className = "Mage";
        String spec = "Fire";


*//*        CompletableFuture<String> timelineFuture1 = CompletableFuture.supplyAsync(() ->
                mplusTimelineDataService.getTimelineData(className, spec, dungeonId1));
        CompletableFuture<String> timelineFuture2 = CompletableFuture.supplyAsync(() ->
                mplusTimelineDataService.getTimelineData(className, spec, dungeonId2));
        CompletableFuture<String> timelineFuture3 = CompletableFuture.supplyAsync(() ->
                mplusTimelineDataService.getTimelineData(className, spec, dungeonId3));

        // 모든 비동기 작업이 완료되기를 기다림
        CompletableFuture.allOf(timelineFuture1, timelineFuture2, timelineFuture3).join();

        String timelineData1 = null;
        String timelineData2 = null;
        String timelineData3 = null;
        try {
            timelineData1 = timelineFuture1.get();
            timelineData2 = timelineFuture2.get();
            timelineData3 = timelineFuture3.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


        log.info("TimelineData1 Length: " + timelineData1.length());
        log.info("TimelineData2 Length: " + timelineData2.length());
        log.info("TimelineData3 Length: " + timelineData3.length());*//*


    }*/

}
