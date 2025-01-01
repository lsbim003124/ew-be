package com.lsbim.wowlsb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class FightsService {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    public MplusFightsDTO getMplusFights(String code, int fightId) {

        //        %d는 정수형(int), %s는 문자열(String)
        String query = String.format("""
        {
          reportData {
                report(code:"%s"){
                    startTime
                    endTime
                    fights(
                        fightIDs:%d
                        translate:true
                    ){
                        startTime
                        dungeonPulls{name,kill,startTime,endTime,
                            enemyNPCs{id,gameID}
                        }
                    }
                }
          }
        }
        """, code, fightId);

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

        JsonNode node = result
                .path("data").path("reportData")
                .path("report").path("fights").get(0);

        long startTime = node.path("startTime").asLong();
        ArrayNode killPull = getKillPull(node);

        MplusFightsDTO dto = MplusFightsDTO.fromArrayNode(killPull, startTime);

//        log.info("1회 호출");
        return dto;
    }

//    kill이 true인 pull만 가져오기(네임드 킬)
    public ArrayNode getKillPull(JsonNode node) {
        if(node.isNull()) return null;

        JsonNode dungeonPulls = node.path("dungeonPulls");

        ArrayNode arrayNode = om.createArrayNode();

        for (JsonNode pull : dungeonPulls) {
            if (pull.has("kill") && pull.get("kill").asBoolean()) {
                arrayNode.add(pull);
            }
        }

        return arrayNode;
    }
}
