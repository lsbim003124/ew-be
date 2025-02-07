package com.lsbim.wowlsb.service.wcl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusRankingsDTO;
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
public class RankingsService {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PlayerService playerService;

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    public MplusRankingsDTO getMplusRankings(int dungeonId, String className, String spec) {

//        %d는 정수형(int), %s는 문자열(String)
        String query = String.format("""
        {
            worldData {
                encounter(id: %d) {
                    id
                    name
                    characterRankings(
                        metric: dps
                        className: "%s"
                        specName: "%s"
                        leaderboard: LogsOnly
                    )
                }
            }
        }
        
        """, dungeonId, className, spec);

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

        ArrayNode rankings = (ArrayNode) result.path("data").path("worldData")
                .path("encounter").path("characterRankings")
                .path("rankings");

        MplusRankingsDTO dto = MplusRankingsDTO.fromArrayNode(rankings);

//        log.info("1회 호출");
        return dto;
    }
}
