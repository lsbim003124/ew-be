package com.lsbim.wowlsb.api.mplus.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusEnemyCastsDTO;
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
public class GetMplusEventByEnemyCastsTests {

    @Value("${api.client.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Value("${api.token}")
    private String token;

/*    @Test
    public void getMplusEventByEnemyCastsTest1() {

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

//        Body(GraphQL)
        String query = "{\n" +
                "  reportData {\n" +
                "\t\treport(code:\"HKvCpm4bYRqLdAzQ\"){\n" +
                "\t\t\tevents(\n" +
                "\t\t\t\tdataType:Casts\n" +
                "\t\t\t\tfightIDs:6\n" +
                "\t\t\t\tstartTime:9760172\n" +
                "\t\t\t\tendTime:9959461\n" +
                "\t\t\t\ttranslate:true\n" +
                "\t\t\t\thostilityType:Enemies\n" +
                "\t\t\t\tsourceID:34\n" +
                "\t\t\t){\n" +
                "\t\t\t\tdata\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "  }\n" +
                "}";

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
        log.info(result);

        ArrayNode events = (ArrayNode) result
                .path("data").path("reportData")
                .path("report").path("events").path("data");
        log.info(events);

        int actorId = 521;

        List<MplusEnemyCastsDTO> arr = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            int targetId = events.get(i).path("targetID").asInt();
            String type = events.get(i).path("type").asText();

            if(type.equals("begincast") && i + 1 < events.size()){
                JsonNode nextEvent = events.get(i + 1);

                if (nextEvent.path("type").asText().equals("cast")) {
                    int nextTargetId = nextEvent.path("targetID").asInt();

                    // targetId가 -1 또는 519인지 확인
                    if (nextTargetId == -1 || nextTargetId == actorId) {
                        MplusEnemyCastsDTO dto = addDto(events.get(i));

                        arr.add(dto);
                    }
                }
            } else {
                if (type.equals("cast") && (targetId == -1 || targetId == actorId)) {
                    MplusEnemyCastsDTO dto = addDto(events.get(i));

                    arr.add(dto);
                }
            }
        }

        log.info(arr);
    }

    private MplusEnemyCastsDTO addDto (JsonNode node) {

        MplusEnemyCastsDTO dto = new MplusEnemyCastsDTO();

        dto.setType(node.path("type").asText());
        dto.setTimestamp(node.path("timestamp").asLong());
        dto.setAbilityGameID(node.path("abilityGameID").asInt());

        return dto;
    }*/
}
