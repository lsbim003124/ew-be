package com.lsbim.wowlsb.service.wcl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.dto.mplus.pamameter.CodeAndFightIdDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    public MplusFightsDTO getMplusFights(CodeAndFightIdDTO paramDTO) {

        StringBuilder query = new StringBuilder("{\n  reportData {\n");

        // 각 요청에 대한 GraphQL 쿼리 생성
        query.append(String.format("    report(code:\"%s\"){\n", paramDTO.getCode()));
        query.append("      startTime\n      endTime\n");
        query.append(String.format("      " +
                        "fights(fightIDs:%d " +
                        "translate:true){\n"
                , paramDTO.getFightId()));
        query.append("        startTime\n        dungeonPulls{\n");
        query.append("          name,kill,startTime,endTime,\n");
        query.append("          enemyNPCs{id,gameID}\n        }\n");
        query.append("      }\n    }\n");

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

        JsonNode node = null;
        MplusFightsDTO dto = null;

        try {

            node = result
                    .path("data").path("reportData")
                    .path("report").path("fights").get(0);

            long startTime = node.path("startTime").asLong();
            ArrayNode killPull = getKillPull(node);

            dto = MplusFightsDTO.fromArrayNode(killPull, startTime);

            return dto;
        } catch (NullPointerException e) {
            log.info("NPE! Failed process fights info: '{}'", paramDTO);
        }
        return dto;
    }

    //    kill이 true인 pull만 가져오기(네임드 킬)
    public ArrayNode getKillPull(JsonNode node) {
        if (node.isNull()) return null;

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
