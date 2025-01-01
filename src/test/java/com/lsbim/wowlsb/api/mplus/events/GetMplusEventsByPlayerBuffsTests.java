package com.lsbim.wowlsb.api.mplus.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusEnemyCastsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusPlayerCastsDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest
@Log4j2
public class GetMplusEventsByPlayerBuffsTests {

    @Value("${api.client.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Value("${api.token}")
    private String token;

    @Test
    public void getMplusEventByPlayerBuffsTest1() {
        // 추후 DB에서 외생기 목록만 가져오도록 변경
        List<Integer> helfDef = Arrays.asList(6940, 116849, 33206, 1022, 204018, 102342);
        int actorId = 5;
        List<List<MplusPlayerCastsDTO>> dtoList = new ArrayList<>();

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        for (int skillId : helfDef) {

            String query = String.format("""
                    {
                      reportData {
                    		report(code:"x2vgYDqydAaPfkTc"){
                    			events(
                    				dataType:Buffs
                    				fightIDs:12
                    				startTime:0
                    				endTime:6958462
                    				translate:true
                    				abilityID:%d
                    			){
                    				data
                    			}
                    		}
                      }
                    }
                        """, skillId);

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

            log.info(events);
            List<MplusPlayerCastsDTO> arr = new ArrayList<>();

            for (JsonNode event : events) {
                if (event.path("targetID").asInt() == actorId) {
                    MplusPlayerCastsDTO dto = createPlayerDefensiveDTO(event);
                    arr.add(dto);
                }
            }
            if (!arr.isEmpty()) {
                dtoList.add(arr);
            }
        }
        ArrayNode arrayNode = om.valueToTree(dtoList);
        log.info(arrayNode);
    }

    private MplusPlayerCastsDTO createPlayerDefensiveDTO(JsonNode node) {

        MplusPlayerCastsDTO dto = new MplusPlayerCastsDTO();

        dto.setType(node.path("type").asText());
        dto.setTimestamp(node.path("timestamp").asLong());
        dto.setAbilityGameID(node.path("abilityGameID").asInt());

        return dto;
    }
}
