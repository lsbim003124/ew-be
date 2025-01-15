package com.lsbim.wowlsb.api.blizzard;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.service.ApiTokenService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Log4j2
public class GetBlizzardItemTests {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wow.item.url}")
    private String apiUrl;

    @Value("${wow.token}")
    private String token;

    @Autowired
    private ApiTokenService apiTokenService;

//    스프링의 의존성 주입은 필드초기화 이후이므로 메소드로 만들 것
//    private String token = apiTokenService.getBlizzardToken();

    @Value("${wow.api.param}")
    private String wowParam;

    @Test
    public void getBlizzradSpellTest1() {

//        token = apiTokenService.getBlizzardToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity requestEntity = new HttpEntity(headers);

        int itemId = 220202;

        String url = apiUrl + itemId + wowParam;

        // API 요청
        ResponseEntity<ObjectNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                ObjectNode.class
        );

        log.info(response);

        String itemName = response.getBody().path("name").asText();

        log.info("itemName: {}", itemName);
    }
}
