package com.lsbim.wowlsb.api.blizzard;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Log4j2
public class GetAccessTokenTests {

/*    @Autowired
    private RestTemplate restTemplate;

    @Value("${wow.access.token.url}")
    private String url;

    @Value("${blizzard.id}")
    private String id;

    @Value("${blizzard.secret}")
    private String secret;

    @Test
    public void getAccessTokenTest1(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //        Basic Auth 설정
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor(id,secret)
        );

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

//        HttpEntity 생성 (Headers와 Body 포함)
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // API 요청
        ResponseEntity<ObjectNode> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ObjectNode.class
        );

        ObjectNode result = response.getBody();

        String token = result.path("access_token").asText();

        log.info(token);
    }*/
}
