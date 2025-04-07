package com.lsbim.wowlsb.api.mplus.gameData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.json.Json;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.lsbim.wowlsb.service.gcp.storage.GCSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@Log4j2
public class GetSpellImgDataTests {
/*
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GCSService gcsService;

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    @Test
    public void GetSpellImgDataTest1() {
        Set<Integer> spellIds = new HashSet<>();
        spellIds.add(447439);
        spellIds.add(457674);

        StringBuilder query = new StringBuilder();
        query.append("{\n  gameData{\n");

//        만약 Set.forEach에서(람다 내부에서) 인덱스를 관리하고싶으면 아토믹인티저 사용.
        spellIds.forEach(spellId -> {
            query.append(String.format("    img%d:ability(id:%d){icon}\n", spellId, spellId));
        });

        query.append("  }\n}");

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

        log.info(result);

        Iterator<Map.Entry<String, JsonNode>> fields = result.get("data").get("gameData").fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();

            String fieldName = entry.getKey(); // 예: "img123456"
            int spellId = Integer.parseInt(fieldName.replace("img", "")); // 주문번호 추출 ("img" 제거)
            String icon = entry.getValue().get("icon").asText();

            gcsService.uploadObject(icon, spellId);
        }
    }*/

}
