package com.lsbim.wowlsb.service.wcl.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusResourcesGraphDTO;
import com.lsbim.wowlsb.dto.mplus.pamameter.CodeAndFightIdDTO;
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

    public List<MplusResourcesGraphDTO> getResourcesData(
            CodeAndFightIdDTO fightParamDTO, MplusFightsDTO fightsDTO, int actorId) {

        StringBuilder query = new StringBuilder("{\n  reportData {\n");

//        pull별 반복문
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
            MplusFightsDTO.Pull pull = fightsDTO.getPulls().get(i);

//                alias는 report+인덱스
            query.append(String.format("        report%d: report(code:\"%s\"){\n", i, fightParamDTO.getCode()));
            query.append(String.format("        events(\n"));
            query.append(String.format("        dataType:DamageTaken\n"));
            query.append(String.format("        fightIDs:%d\n", fightParamDTO.getFightId()));
            query.append(String.format("        startTime:%d\n", pull.getStartTime()));
            query.append(String.format("        endTime:%d\n", pull.getEndTime()));
            query.append(String.format("        sourceID:%d\n", actorId));
            query.append(String.format("        hostilityType:Friendlies\n"));
            query.append(String.format("        translate:true\n"));
            query.append(String.format("        ){data}\n"));// events 종료
            query.append(String.format("        hp:graph(\n"));
            query.append(String.format("        dataType:Resources\n"));
            query.append(String.format("        fightIDs:%d\n", fightParamDTO.getFightId()));
            query.append(String.format("        startTime:%d\n", pull.getStartTime()));
            query.append(String.format("        endTime:%d\n", pull.getEndTime()));
            query.append(String.format("        sourceID:%d\n", actorId));
            query.append(String.format("        abilityID:1000\n"));
            query.append(String.format("        hostilityType:Friendlies\n"));
            query.append(String.format("        translate:true\n"));
            query.append(String.format("        )")); // hp 종료
            query.append(String.format("        }\n")); // report 종료(alias)

        }// pull별 종료


        query.append("  }\n"); // reportData 종료
        query.append("  }\n"); // 전체 종료

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


        ObjectNode result = (ObjectNode) response.getBody()
                .path("data").path("reportData");


//            유저별 그래프데이터
        List<MplusResourcesGraphDTO> arr = new ArrayList<>();

        // pull별 resources 기록 유저 전용으로 통합
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
            ObjectNode node = (ObjectNode) result.path("report" + i);
            MplusResourcesGraphDTO dto = MplusResourcesGraphDTO.fromArrayNode(node);

            arr.add(dto);
        }


        return arr;
    }

    public Set<Integer> getResourcesBuffIds(ArrayNode damageTakenDataList) {
        Set<Integer> buffIds = new HashSet<>();

        for (JsonNode taken : damageTakenDataList) {
            if (!taken.has("buffs")) { // buffs 필드가 없으면
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
