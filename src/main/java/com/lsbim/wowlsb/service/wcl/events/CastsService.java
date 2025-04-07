package com.lsbim.wowlsb.service.wcl.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusEnemyCastsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusPlayerCastsDTO;
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

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    public List<MplusEnemyCastsDTO> getEnemyCasts(
            String code, int fightId, long startTime, long endTime,
            int sourceId, int actorId) {

        String query = String.format("""
                {
                  reportData {
                        report(code:"%s"){
                            events(
                                dataType:Casts
                                fightIDs:%d
                                startTime:%d
                                endTime:%d
                                translate:true
                                hostilityType:Enemies
                                sourceID:%d
                            ){
                                data
                            }
                        }
                  }
                }
                    """, code, fightId, startTime, endTime, sourceId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        // 요청 본문 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query);

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
        ArrayNode events = (ArrayNode) result
                .path("data").path("reportData")
                .path("report").path("events").path("data");

        List<MplusEnemyCastsDTO> arr = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            int targetId = events.get(i).path("targetID").asInt();
            String type = events.get(i).path("type").asText();

//            캐스팅 시작일 경우 대상이 없음(-1)
            if (type.equals("begincast") && i + 1 < events.size()) {
                JsonNode nextEvent = events.get(i + 1);

                if (nextEvent.path("type").asText().equals("cast")) {
                    int nextTargetId = nextEvent.path("targetID").asInt();

                    // targetId가 -1 또는 actorId인지 확인
                    if (nextTargetId == -1 || nextTargetId == actorId) {
                        MplusEnemyCastsDTO dto = createEnemyDTO(events.get(i));

                        arr.add(dto);
                    }
                }
            } else {
                if (type.equals("cast") && (targetId == -1 || targetId == actorId)) {
                    MplusEnemyCastsDTO dto = createEnemyDTO(events.get(i));

                    arr.add(dto);
                }
            }
        }
//        log.info("1회 호출");
        return arr;
    }

    public List<List<MplusPlayerCastsDTO>> getPlayerDefensiveCasts(
            String code, int fightId, long startTime, long endTime
            , int sourceId, List<Integer> abilityId
            , String className) {

        List<List<MplusPlayerCastsDTO>> dtoList = new ArrayList<>();

        for (int skillId : abilityId) {
            String query = String.format("""
                    {
                      reportData {
                            report(code:"%s"){
                                events(
                                    dataType:Casts
                                    fightIDs:%d
                                    startTime:%d
                                    endTime:%d
                                    translate:true
                                    sourceID:%d
                                    abilityID:%d
                                ){
                                    data
                                }
                            }
                      }
                    }
                        """, code, fightId, startTime, endTime, sourceId, skillId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(token);

            // 요청 본문 구성
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("query", query);

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
            ArrayNode events = (ArrayNode) result
                    .path("data").path("reportData")
                    .path("report").path("events").path("data");

            List<MplusPlayerCastsDTO> arr = new ArrayList<>();

            for (JsonNode event : events) {
                MplusPlayerCastsDTO dto = createPlayerDefensiveDTO(event);
                arr.add(dto);
            }

            // 비어있지 않은 경우에만 추가
            if (!arr.isEmpty()) {
                dtoList.add(arr);
            }
        }
//        log.info(abilityId.size()+"회 호출");
        return dtoList;
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
