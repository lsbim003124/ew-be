package com.lsbim.wowlsb.api.mplus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.service.wcl.PlayerService;
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
public class GetMplusPlayerDetailsTests {

    @Value("${api.client.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlayerService playerService;

    @Value("${api.token}")
    private String token;
/*
    @Test
    public void getMplusPlayerDetailsTest1() {

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        String code = "HKvCpm4bYRqLdAzQ";

//        Body(GraphQL)
        String query = "{\n" +
                "  reportData {\n" +
                "\t\treport(code:\""+code+"\"){\n" +
                "\t\t\tplayerDetails(\n" +
                "\t\t\t\tfightIDs:6\n" +
                "\t\t\t\ttranslate:true\n" +
                "\t\t\t)\n" +
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

        String name = "Fabríce";
        String className = "Mage";
        String spec = "Arcane";

//        String role = playerService.findRole(className, spec);
//        log.info(role);

        JsonNode details = result.path("data").path("reportData")
                .path("report").path("playerDetails")
                .path("data").path("playerDetails");

//        int actorId = playerService.findActorId(role, className, name, details);
//        log.info(actorId);
    }*/
}
