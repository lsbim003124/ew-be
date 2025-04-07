package com.lsbim.wowlsb.service.wcl.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusPlayerCastsDTO;
import com.lsbim.wowlsb.enums.character.skill.defensive.SkillInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.lsbim.wowlsb.service.wcl.PlayerService.SKILL_ENUMS;

@Service
@Log4j2
public class BuffsService {
    @Value("${api.client.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Value("${api.token}")
    private String token;

    public List<List<MplusPlayerCastsDTO>> getTakenBuffs(
            String code, int fightId, long startTime, long endTime, int actorId) {
        // 추후 DB에서 외생기 목록만 가져오도록 변경
        List<Integer> helfDef = Arrays.asList(6940, 116849, 33206, 1022, 204018, 102342);
        List<List<MplusPlayerCastsDTO>> dtoList = new ArrayList<>();

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        for (int skillId : helfDef) {

            String query = String.format("""
                    {
                      reportData {
                    		report(code:"%s"){
                    			events(
                    				dataType:Buffs
                    				fightIDs:%d
                    				startTime:%d
                    				endTime:%d
                    				translate:true
                    				abilityID:%d
                    			){
                    				data
                    			}
                    		}
                      }
                    }
                        """, code, fightId, startTime, endTime, skillId);

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

            ArrayNode events = (ArrayNode) result
                    .path("data").path("reportData")
                    .path("report").path("events").path("data");

            List<MplusPlayerCastsDTO> arr = new ArrayList<>();

            for (JsonNode event : events) {
                if (event.path("targetID").asInt() == actorId) {
                    MplusPlayerCastsDTO dto = createPlayerDefensiveDTO(event);
                    arr.add(dto);
                }
            }
            if (!arr.isEmpty()) {
                dtoList.add(arr);
            }
        }
//        log.info(helfDef.size()+"회 호출");
        return dtoList;
    }

    private MplusPlayerCastsDTO createPlayerDefensiveDTO(JsonNode node) {

        MplusPlayerCastsDTO dto = new MplusPlayerCastsDTO();

        dto.setType(node.path("type").asText());
        dto.setTimestamp(node.path("timestamp").asLong());
        dto.setAbilityGameID(node.path("abilityGameID").asInt());

        return dto;
    }

    // playerService에서 skill_enums 가져옴. 추후 import static에서 제거할 것
    private String findTakenSkillName(int skillId) {

        return SKILL_ENUMS.stream()
                .map(Class::getEnumConstants)
                .flatMap(Arrays::stream)
                .filter(SkillInfo.class::isInstance)
                .map(SkillInfo.class::cast)
                .filter(skill -> skill.getSkillId() == skillId)
                .map(SkillInfo::getSkillName)
                .findFirst()
                .orElse(null);
    }
}
