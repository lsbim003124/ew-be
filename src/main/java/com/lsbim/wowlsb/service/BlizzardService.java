package com.lsbim.wowlsb.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class BlizzardService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wow.access.token.url}")
    private String tokenUrl;

    @Value("${wow.spell.url}")
    private String spellUrl;

    @Value("${blizzard.id}")
    private String id;

    @Value("${blizzard.secret}")
    private String secret;

    @Value("${wow.api.param}")
    private String wowParam;

    public String getSpellInfoByBlizzard(int spellId){
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity requestEntity = new HttpEntity(headers);

        String url = spellUrl+spellId+wowParam;

        // API 요청
        ResponseEntity<ObjectNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                ObjectNode.class
        );

        String spellName = response.getBody().path("name").asText();

        return spellName;
    }

    private String getAccessToken(){
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
                tokenUrl,
                HttpMethod.POST,
                requestEntity,
                ObjectNode.class
        );

        ObjectNode result = response.getBody();

        String token = result.path("access_token").asText();

        return token;
    }
}
