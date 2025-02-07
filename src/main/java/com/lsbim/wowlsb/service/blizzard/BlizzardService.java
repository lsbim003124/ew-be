package com.lsbim.wowlsb.service.blizzard;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.service.ApiTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BlizzardService {

    @Autowired
    private RestTemplate restTemplate;

    private final ApiTokenService apiTokenService;

    @Value("${wow.spell.url}")
    private String spellUrl;

    @Value("${wow.api.param}")
    private String wowParam;

    public String getSpellInfoByBlizzard(int spellId) {

        // ************보스 스킬 정보는 제공하지 않기 때문에 skillName을 파악할 수 없음************
        String token = apiTokenService.getBlizzardToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity requestEntity = new HttpEntity(headers);

        String url = spellUrl + spellId + wowParam;

        try {
            ResponseEntity<ObjectNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    ObjectNode.class
            );

            String spellName = response.getBody().path("name").asText();

            return spellName;

//            블리자드 액세스 토큰 만료 시 401 Unauthorized: [no body] 발생
        } catch (HttpClientErrorException.Unauthorized e) {
            apiTokenService.refreshToken();
            token = apiTokenService.getBlizzardToken();

            headers.setBearerAuth(token);
            HttpEntity retryRequestEntity = new HttpEntity(headers);

            ResponseEntity<ObjectNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    retryRequestEntity,
                    ObjectNode.class
            );

            String spellName = response.getBody().path("name").asText();

            return spellName;
        } catch (HttpClientErrorException.NotFound e){
            return null;
        }

    }
}
