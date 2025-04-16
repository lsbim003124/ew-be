package com.lsbim.wowlsb.service.wcl.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusFightsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusPlayerCastsDTO;
import com.lsbim.wowlsb.dto.mplus.pamameter.CodeAndFightIdDTO;
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


    //  [유저의][pull별][외생기 주문별]
    public List<List<List<MplusPlayerCastsDTO>>> getTakenBuffs(
            CodeAndFightIdDTO fightParamDTO, MplusFightsDTO fightsDTO, int actorId) {
//      유효 외생기 주문번호 목록
        List<Integer> helpDef = Arrays.asList(6940, 116849, 33206, 1022, 204018, 102342);

        StringBuilder query = new StringBuilder("{\n  reportData {\n");

//        pull별 반복문
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
            MplusFightsDTO.Pull pull = fightsDTO.getPulls().get(i);

//            외생기 리스트
            for (int j = 0; j < helpDef.size(); j++) {

//                alias는 report+인덱스
                query.append(String.format("        report%d_help%d: report(code:\"%s\"){\n", i, j, fightParamDTO.getCode()));
                query.append(String.format("        events(\n"));
                query.append(String.format("        dataType:Buffs\n"));
                query.append(String.format("        fightIDs:%d\n", fightParamDTO.getFightId()));
                query.append(String.format("        startTime:%d\n", pull.getStartTime()));
                query.append(String.format("        endTime:%d\n", pull.getEndTime()));
                query.append(String.format("        translate:true\n"));
                query.append(String.format("        abilityID:%d\n", helpDef.get(j)));
                query.append(String.format("        ){data}\n"));// events 종료
                query.append(String.format("        }\n")); // report 종료(alias)
            }

        }// pull별 종료


        query.append("  }\n"); // reportData 종료
        query.append("  }\n"); // 전체 종료

        // 요청 본문 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query.toString());

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

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

//        log.info(result);

//        유저 하나의 외생기데이터
        List<List<List<MplusPlayerCastsDTO>>> userHelpList = new ArrayList<>();

//        pull 단위
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
//            pull 하나의 외생기데이터
            List<List<MplusPlayerCastsDTO>> dtoList = new ArrayList<>();

//            외생기 리스트
            for (int j = 0; j < helpDef.size(); j++) {

                ArrayNode events = (ArrayNode) result
                        .path("data").path("reportData")
                        .path("report" + i + "_" + "help" + j).path("events")
                        .path("data");

//                외생기 하나 단위
                List<MplusPlayerCastsDTO> arr = new ArrayList<>();

                for (JsonNode event : events) {
                    if (event.path("targetID").asInt() == actorId) {
                        MplusPlayerCastsDTO dto = createPlayerDefensiveDTO(event);
                        arr.add(dto);
                    }
                }

//              배열이 비어있어도 pull 인덱스를 유지하기 위해 무조건 추가
                dtoList.add(arr);
            }

            userHelpList.add(dtoList);
        }

        return userHelpList;
    }

    public List<List<List<MplusPlayerCastsDTO>>> getTakenBloodlusts(
            CodeAndFightIdDTO fightParamDTO, MplusFightsDTO fightsDTO, int actorId) {
//      유효 외생기 주문번호 목록
        List<Integer> helpDef = Arrays.asList(
                80353 // 시간 왜곡
                , 390386 // 위상의 격노
                , 32182 // 영웅심
                , 2825 // 피의 욕망
                , 466904 // 유린자의 울음소리
                , 264667 // 원초적 분노
                , 444257 // 우레의 북
        );

        StringBuilder query = new StringBuilder("{\n  reportData {\n");

//        pull별 반복문
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
            MplusFightsDTO.Pull pull = fightsDTO.getPulls().get(i);

//            외생기 리스트
            for (int j = 0; j < helpDef.size(); j++) {

//                alias는 report+인덱스
                query.append(String.format("        report%d_help%d: report(code:\"%s\"){\n", i, j, fightParamDTO.getCode()));
                query.append(String.format("        events(\n"));
                query.append(String.format("        dataType:Buffs\n"));
                query.append(String.format("        fightIDs:%d\n", fightParamDTO.getFightId()));
                query.append(String.format("        startTime:%d\n", pull.getStartTime()));
                query.append(String.format("        endTime:%d\n", pull.getEndTime()));
                query.append(String.format("        translate:true\n"));
                query.append(String.format("        abilityID:%d\n", helpDef.get(j)));
                query.append(String.format("        ){data}\n"));// events 종료
                query.append(String.format("        }\n")); // report 종료(alias)
            }

        }// pull별 종료


        query.append("  }\n"); // reportData 종료
        query.append("  }\n"); // 전체 종료

        // 요청 본문 구성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query.toString());

//        Content-Type: application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

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

//        log.info(result);

//        유저 하나의 외생기데이터
        List<List<List<MplusPlayerCastsDTO>>> userHelpList = new ArrayList<>();

//        pull 단위
        for (int i = 0; i < fightsDTO.getPulls().size(); i++) {
//            pull 하나의 외생기데이터
            List<List<MplusPlayerCastsDTO>> dtoList = new ArrayList<>();

//            외생기 리스트
            for (int j = 0; j < helpDef.size(); j++) {

                ArrayNode events = (ArrayNode) result
                        .path("data").path("reportData")
                        .path("report" + i + "_" + "help" + j).path("events")
                        .path("data");

//                외생기 하나 단위
                List<MplusPlayerCastsDTO> arr = new ArrayList<>();

                for (JsonNode event : events) {
                    if (event.path("targetID").asInt() == actorId) {
                        MplusPlayerCastsDTO dto = createPlayerDefensiveDTO(event);
                        arr.add(dto);
                    }
                }

//              배열이 비어있어도 pull 인덱스를 유지하기 위해 무조건 추가
                dtoList.add(arr);
            }

            userHelpList.add(dtoList);
        }

        return userHelpList;
    }

    private MplusPlayerCastsDTO createPlayerDefensiveDTO(JsonNode node) {

        MplusPlayerCastsDTO dto = new MplusPlayerCastsDTO();

        dto.setType(node.path("type").asText());
        dto.setTimestamp(node.path("timestamp").asLong());
        dto.setAbilityGameID(node.path("abilityGameID").asInt());

        return dto;
    }
}
