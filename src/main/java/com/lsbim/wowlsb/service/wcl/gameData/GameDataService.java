package com.lsbim.wowlsb.service.wcl.gameData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.service.gcp.storage.GCSService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Log4j2
public class GameDataService {
// WCL 요청 필드 명 gameData

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GCSService gcsService;

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;
    
//    주문번호로 이미지URL 엔드포인트를 가져와서 이미지를 다운받고 GCS에 보내기
    public void findAndUploadSpellImg(Set<Integer> spellIds) {

        StringBuilder query = new StringBuilder();
        query.append("{\n  gameData{\n");

        for(int spellId : spellIds){
            query.append(String.format("    img%d:ability(id:%d){icon}\n", spellId, spellId));
        }

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

//        반환해서 다시 gcsService로 보내기 vs 여기서 반복문으로 처리하기 어느게 더 나은지? 꼬리물기가 계속되면 의존성이 과해지진 않는가?
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();

            String fieldName = entry.getKey(); // 예: "img123456"
            int spellId = Integer.parseInt(fieldName.replace("img", "")); // 주문번호 추출 ("img" 제거)
            String icon = entry.getValue().get("icon").asText();

            gcsService.uploadObject(icon, spellId);
        }
    }
}
