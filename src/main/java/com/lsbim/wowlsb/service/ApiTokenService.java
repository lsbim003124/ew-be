package com.lsbim.wowlsb.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiTokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wow.access.token.url}")
    private String url;

    @Value("${blizzard.id}")
    private String id;

    @Value("${blizzard.secret}")
    private String secret;

    private String blizzardToken;

    public String getBlizzardToken(){
        if(blizzardToken == null){
            refreshToken();
        }

        return blizzardToken;
    }

    public void refreshToken(){
//        헤더 생성
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

        String token = response.getBody().path("access_token").asText();

        blizzardToken = token;
    }
}
