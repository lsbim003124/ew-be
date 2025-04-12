package com.lsbim.wowlsb.api.mplus.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class GetMplusEventsByResourcesTests {

/*    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    @Test
    public void getMplusEventsByResourcesTest1() {
        String code = "2TDjkMqYv7FaLmJH";
        int fightId = 2;
        int startTime = 5577360;
        int endTime = 5731105;
        int sourceID = 3;

        String query = String.format("""
                {
                    reportData {
                   		report(code:"%s"){
                   			events(
                   				dataType:DamageTaken
                   				fightIDs:%d
                   				startTime:%d
                   				endTime:%d
                   				sourceID:%d
                   				hostilityType:Friendlies
                   			){data}
                   			hp:graph(
                   				dataType:Resources
                   				fightIDs:%d
                   				startTime:%d
                   				endTime:%d
                   				sourceID:%d
                   				abilityID:1000
                   				hostilityType:Friendlies
                   			)
                   		}
                     }
                }
                    """,code,fightId,startTime,endTime,sourceID,fightId,startTime,endTime,sourceID);

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

        ObjectNode result = (ObjectNode) response.getBody()
                .path("data").path("reportData").path("report");

//        log.info(result.path("hp").path("data")
//                .path("series").get(0).path("data"));

        MplusResourcesGraphDTO dto = MplusResourcesGraphDTO.fromArrayNode(result);

        ObjectNode objectNode = om.valueToTree(dto);

        log.info(objectNode);
    }*/
}
