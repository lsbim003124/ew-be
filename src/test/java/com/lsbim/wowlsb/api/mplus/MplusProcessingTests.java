package com.lsbim.wowlsb.api.mplus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusEnemyCastsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusPlayerCastsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusRankingsDTO;
import com.lsbim.wowlsb.service.events.CastsService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import com.lsbim.wowlsb.service.wcl.DungeonService;
import com.lsbim.wowlsb.service.wcl.FightsService;
import com.lsbim.wowlsb.service.wcl.PlayerService;
import com.lsbim.wowlsb.service.wcl.RankingsService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.File;
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
    private RankingsService rankingsService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Test
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
    }

    @Test
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
    }

    @Test
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


/*        CompletableFuture<String> timelineFuture1 = CompletableFuture.supplyAsync(() ->
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
        log.info("TimelineData3 Length: " + timelineData3.length());*/


    }

}
