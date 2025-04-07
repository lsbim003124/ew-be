package com.lsbim.wowlsb.service.wcl.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusResourcesGraphDTO;
import com.lsbim.wowlsb.service.wcl.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Log4j2
public class ResourcesService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    public MplusResourcesGraphDTO getResourcesData(String code, int fightId, Long startTime, Long endTime, int sourceId){

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
                    """,code,fightId,startTime,endTime,sourceId,fightId,startTime,endTime,sourceId);

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

        MplusResourcesGraphDTO dto = MplusResourcesGraphDTO.fromArrayNode(result);

        return dto;
    }

    public Set<Integer> getResourcesBuffIds(ArrayNode damageTakenDataList){
        Set<Integer> buffIds = new HashSet<>();

        for (JsonNode taken : damageTakenDataList) {
            if(!taken.has("buffs")){ // buffs 필드가 없으면
                continue;
            }

            int takenSpellId = taken.path("abilityGameID").asInt();
            buffIds.add(takenSpellId); // 피해를 입힌 주문도 삽입

            String buffs = taken.path("buffs").asText();

            if (buffs != null && !buffs.isEmpty()) {
                String[] parts = buffs.split("\\.");

                for (String part : parts) {
                    if (part.isBlank()) {
                        continue; // 빈 문자열(마침표가 연속된 경우) 무시
                    }
                    try {
                        int buffId = Integer.parseInt(part);
                        buffIds.add(buffId); // 플레이어에게 적용된 버프 목록 전부 삽입
                    } catch (NumberFormatException nfe) {
                        // 숫자가 아닌 토큰이 섞여 있으면 무시하거나 로깅
                         log.warn("Invalid buff id token: {}", part);
                    }
                }
            }
        }

        return buffIds;
    }
}
