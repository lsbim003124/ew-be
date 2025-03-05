package com.lsbim.wowlsb.api.mplus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusRankingsDTO;
import com.lsbim.wowlsb.service.wcl.DungeonService;
import com.lsbim.wowlsb.service.wcl.PlayerService;
import com.lsbim.wowlsb.service.wcl.RankingsService;
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
public class GetMplusRankingListTests {

    @Value("${api.client.url}")
    private String apiUrl;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private RankingsService rankingsService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Value("${api.token}")
    private String token;

   /* @Test
    public void getMplusRankingListsTest1() {

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);


//        Body(GraphQL)
        String query = "{\n" +
                "  worldData {\n" +
                "    encounter(id: 12660) {\n" +
                "      id\n" +
                "      name\n" +
                "\t\t\tcharacterRankings(\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tpage:1\n" +
                "\t\t\t\tmetric:dps\n" +
                "\t\t\t\tclassName:\"Mage\"\n" +
                "\t\t\t\tspecName:\"Arcane\"\n" +
                "\t\t\t\tleaderboard:LogsOnly\n" +
                "\t\t\t\t\n" +
                "\t\t\t)\n" +
                "    }\n" +
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

        log.info("==================================");
        log.info(result);

        ArrayNode rankings = (ArrayNode) result.path("data").path("worldData")
                .path("encounter").path("characterRankings")
                .path("rankings");
        log.info(rankings);

        MplusRankingsDTO dto = MplusRankingsDTO.fromArrayNode(rankings);

//        log.info(dto);
//        dto.getRankings().forEach(ranking -> {
//            log.info("Player: {}, Report Code: {}",
//                    ranking.getName(),
//                    ranking.getReport().getCode());
//        });

        ObjectNode objectNode = om.valueToTree(dto);
        log.info(objectNode);

        JsonNode firstNode = objectNode.path("rankings").get(0);
        log.info(firstNode);

        log.info("====================================");
        String dungeonName = result.path("data").path("worldData")
                .path("encounter").path("name").asText();
        log.info("dungeon: " + dungeonName);
        int dungeonId = dungeonService.findDungeonIdByDungeonName(dungeonName);
        log.info("dungeon id : " + dungeonId);
    }*/

    @Test
    public void getMplusRankingListsTest2() {

        int dungeonId = 12660;
        String className = "Druid";
        String spec = "Balance";

        MplusRankingsDTO dto = rankingsService.getMplusRankings(dungeonId, className, spec);
        log.info(dto);

        ObjectNode objectNode = om.valueToTree(dto);
        log.info(objectNode);
    }

}

