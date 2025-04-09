package com.lsbim.wowlsb.service.wcl.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.json.Json;
import com.lsbim.wowlsb.dto.mplus.MplusEnemyCastsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusPlayerCastsDTO;
import com.lsbim.wowlsb.dto.mplus.pamameter.CodeAndFightIdDTO;
import com.lsbim.wowlsb.service.wcl.DungeonService;
import com.lsbim.wowlsb.service.wcl.PlayerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Log4j2
public class CastsService {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DungeonService dungeonService;

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    public List<List<MplusEnemyCastsDTO>> getEnemyCasts(
            CodeAndFightIdDTO fightParamDTO, MplusFightsDTO fightsDTO, int dungeonId, int actorId) {

        StringBuilder query = new StringBuilder("{\n  reportData {\n");

//        pull별 반복문
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
            MplusFightsDTO.Pull pull = fightsDTO.getPulls().get(i);
            List<Integer> bossNpcIds = dungeonService.findBossIdByList(pull.getEnemyNpcs(), dungeonId);

//            보스별(여럿일 때를 가정)
            for (int j = 0; j < bossNpcIds.size(); j++) {
                int bossNpcId = bossNpcIds.get(j);

//                alias는 pull+풀인덱스_boss+보스인덱스
                query.append(String.format("        pull%d_boss%d: report(code:\"%s\"){\n", i, j, fightParamDTO.getCode()));
                query.append(String.format("        events(\n"));
                query.append(String.format("        dataType:Casts\n"));
                query.append(String.format("        fightIDs:%d\n", fightParamDTO.getFightId()));
                query.append(String.format("        startTime:%d\n", pull.getStartTime()));
                query.append(String.format("        endTime:%d\n", pull.getEndTime()));
                query.append(String.format("        translate:true\n"));
                query.append(String.format("        hostilityType:Enemies\n"));
                query.append(String.format("        sourceID:%d\n", bossNpcId));
                query.append(String.format("        )\n"));
                query.append(String.format("        {data}"));// events 종료
                query.append(String.format("        }\n")); // report 종료(alias)

            }// 보스별 종료

        }// pull별 종료

        query.append("  }\n"); // reportData 종료
        query.append("  }\n"); // 전체 종료

//        log.info(query.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        // 요청 본문 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query.toString());

//        HttpEntity 생성 (Headers와 Body 포함)
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 요청
        ResponseEntity<ObjectNode> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                ObjectNode.class
        );

        ObjectNode result = response.getBody();
        JsonNode objNode = result
                .path("data").path("reportData");


//          유저 개인의 보스캐스트 데이터
        List<List<MplusEnemyCastsDTO>> enemyCasts = new ArrayList<>();

//        pull별 가공
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {

            List<MplusEnemyCastsDTO> arr = new ArrayList<>();
//            보스별
            for (int j = 0; j < dungeonService.findBossIdByList(fightsDTO.getPulls().get(i).getEnemyNpcs(), dungeonId).size(); j++) {

                ArrayNode events = (ArrayNode) objNode.path("pull" + i + "_boss" + j)
                        .path("events").path("data");

//              보스별 가공 유저 혹은 광역기 스킬만 필터링
                for (int k = 0; k < events.size(); k++) {
                    int targetId = events.get(k).path("targetID").asInt();
                    String type = events.get(k).path("type").asText();

//                  캐스팅 시작일 경우 대상이 없음(-1)
                    if (type.equals("begincast") && k + 1 < events.size()) {
                        JsonNode nextEvent = events.get(k + 1);

                        if (nextEvent.path("type").asText().equals("cast")) {
                            int nextTargetId = nextEvent.path("targetID").asInt();

                            // targetId가 -1 또는 actorId인지 확인
                            if (nextTargetId == -1 || nextTargetId == actorId) {
                                MplusEnemyCastsDTO dto = createEnemyDTO(events.get(k));

                                arr.add(dto);
                            }
                        }
                    } else {
                        if (type.equals("cast") && (targetId == -1 || targetId == actorId)) {
                            MplusEnemyCastsDTO dto = createEnemyDTO(events.get(k));

                            arr.add(dto);
                        }
                    }
                } // 가공 종료
            } // 보스마릿수별
            enemyCasts.add(arr);
        }// pull별 종료


//        log.info("1회 호출");
        return enemyCasts;
    }


    public List<List<List<MplusPlayerCastsDTO>>> getPlayerDefensiveCasts(
            CodeAndFightIdDTO fightParamDTO, MplusFightsDTO fightsDTO
            , int actorId, List<Integer> abilityId) {

        StringBuilder query = new StringBuilder("{\n  reportData {\n");

        //        pull별 반복문
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
            MplusFightsDTO.Pull pull = fightsDTO.getPulls().get(i);

//            외생기 리스트
            for (int j = 0; j < abilityId.size(); j++) {

//                alias는 report+인덱스
                query.append(String.format("        report%d_abil%d: report(code:\"%s\"){\n", i, j, fightParamDTO.getCode()));
                query.append(String.format("        events(\n"));
                query.append(String.format("        dataType:Casts\n"));
                query.append(String.format("        fightIDs:%d\n", fightParamDTO.getFightId()));
                query.append(String.format("        startTime:%d\n", pull.getStartTime()));
                query.append(String.format("        endTime:%d\n", pull.getEndTime()));
                query.append(String.format("        translate:true\n"));
                query.append(String.format("        sourceID:%d\n", actorId));
                query.append(String.format("        abilityID:%d\n", abilityId.get(j)));
                query.append(String.format("        ){data}\n"));// events 종료
                query.append(String.format("        }\n")); // report 종료(alias)
            }

        }// pull별 종료

        query.append("  }\n"); // reportData 종료
        query.append("  }\n"); // 전체 종료

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        // 요청 본문 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query.toString());

//        HttpEntity 생성 (Headers와 Body 포함)
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 요청
        ResponseEntity<ObjectNode> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                ObjectNode.class
        );

        ObjectNode result = response.getBody();


        List<List<List<MplusPlayerCastsDTO>>> userDefList = new ArrayList<>();

        //        pull 단위
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {

//            pull 하나의 자생기데이터
            List<List<MplusPlayerCastsDTO>> dtoList = new ArrayList<>();

//            자생기 리스트
            for (int j = 0; j < abilityId.size(); j++) {

                ArrayNode events = (ArrayNode) result
                        .path("data").path("reportData")
                        .path("report" + i + "_abil" + j).path("events")
                        .path("data");

                List<MplusPlayerCastsDTO> arr = new ArrayList<>();

                for (JsonNode event : events) {
                    MplusPlayerCastsDTO dto = createPlayerDefensiveDTO(event);
                    arr.add(dto);
                }

                //배열이 비어있어도 pull 인덱스를 유지하기 위해 무조건 추가\
                dtoList.add(arr);
            }

            userDefList.add(dtoList);
        }

//        log.info(abilityId.size()+"회 호출");
        return userDefList;
    }


    private MplusEnemyCastsDTO createEnemyDTO(JsonNode node) {

        MplusEnemyCastsDTO dto = new MplusEnemyCastsDTO();

        dto.setType(node.path("type").asText());
        dto.setTimestamp(node.path("timestamp").asLong());
        dto.setAbilityGameID(node.path("abilityGameID").asInt());

        return dto;
    }

    private MplusPlayerCastsDTO createPlayerDefensiveDTO(JsonNode node) {

        MplusPlayerCastsDTO dto = new MplusPlayerCastsDTO();

        dto.setType(node.path("type").asText());
        dto.setTimestamp(node.path("timestamp").asLong());
        dto.setAbilityGameID(node.path("abilityGameID").asInt());

        return dto;
    }
}
