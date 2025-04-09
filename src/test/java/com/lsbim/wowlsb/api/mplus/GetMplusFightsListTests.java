package com.lsbim.wowlsb.api.mplus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.service.wcl.FightsService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Log4j2
public class GetMplusFightsListTests {

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FightsService fightsService;


/*    @Test
    public void getMplusFightsListTest1() {

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

//        Body(GraphQL)
        String query = "{\n" +
                "  reportData {\n" +
                "\t\treport(code:\"HKvCpm4bYRqLdAzQ\"){\n" +
                "\t\t\tstartTime\n" +
                "\t\t\tendTime\n" +
                "\t\t\tfights(\n" +
                "\t\t\t\tfightIDs:6\n" +
                "\t\t\t\ttranslate:true\n" +
                "\t\t\t){\n" +
                "\t\t\t\tstartTime\n" +
                "\t\t\t\tkeystoneLevel\n" +
                "\t\t\t\tdungeonPulls{name,kill,startTime,endTime,\n" +
                "\t\t\t\t\tenemyNPCs{id,gameID}\n" +
                "\t\t\t\t}\n" +
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

        JsonNode node = result
                .path("data").path("reportData")
                .path("report").path("fights").get(0);
        log.info(node);

        ArrayNode killPull = fightsService.getKillPull(node);
        log.info(killPull);
    }

    @Test
    public void getMplusFightsListTest2() {

        String code = "HKvCpm4bYRqLdAzQ";
        int fightId = 6;

        MplusFightsDTO dto = fightsService.getMplusFights(code, fightId);

        log.info(dto);
    }*/


}
