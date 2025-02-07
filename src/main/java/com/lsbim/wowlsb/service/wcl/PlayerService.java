package com.lsbim.wowlsb.service.wcl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.PlayerSkillInfoDTO;
import com.lsbim.wowlsb.enums.character.Spec;
import com.lsbim.wowlsb.enums.character.WowClass;
import com.lsbim.wowlsb.enums.character.skill.defensive.*;
import com.lsbim.wowlsb.enums.utils.SkillProperty;
import com.lsbim.wowlsb.service.repository.WowClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
public class PlayerService {

    private final WowClassService wowClassService;

    //    순회용 직업별 스킬 열거클래스들..
    public static final List<Class<? extends SkillInfo>> SKILL_ENUMS = Arrays.asList(
//            제네릭 => SkillInfo를 구현한 클래스 목록 이라는 뜻
            Warrior.class,
            Hunter.class,
            Shaman.class,
            Monk.class,
            Rogue.class,
            DeathKnight.class,
            Mage.class,
            Druid.class,
            Paladin.class,
            Priest.class,
            Warlock.class,
            DemonHunter.class,
            Evoker.class
    );

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.client.url}")
    private String apiUrl;

    @Value("${api.token}")
    private String token;

    public int getMplusActorId(String code, int fightId, String className, String spec, String name) {

        String query = String.format("""
                {
                  reportData {
                        report(code:"%s"){
                            playerDetails(
                                fightIDs:%d
                                translate:true
                            )
                        }
                  }
                }
                    """, code, fightId);

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
        JsonNode details = result.path("data").path("reportData")
                .path("report").path("playerDetails")
                .path("data").path("playerDetails");

        String role = wowClassService.getRoleByClassNameAndSpecName(className,spec);
        int actorId = getActorId(role, className, name, details);

//        log.info("1회 호출");
        return actorId;
    }

    public int getActorId(String role, String className, String name, JsonNode node) {

        JsonNode rolePlayers = node.path(role);

        for (JsonNode player : rolePlayers) {
            if (player.path("name").asText().equals(name) && player.path("type").asText().equals(className)) {
                return player.path("id").asInt();
            }
        }

        return 0;
    }

    //    스킬번호 배열 반환
    public List<Integer> findDefensive(String className, String spec) {

        WowClass wowClass = findWowClass(className);

        Class<?> defensiveClass = getEnumClassName(className);
        List<Integer> arr = new ArrayList<>();

        if (defensiveClass.isEnum()) {
            Stream.of(defensiveClass.getEnumConstants())
                    .filter(c -> hasMatchingSpec(c, wowClass, spec))
                    .mapToInt(c -> getSkillIdForFinder(c))
                    .forEach(arr::add);
        }

        return arr;
    }

    private boolean hasMatchingSpec(Object skill, WowClass wowClass, String spec) {
        // 스킬의 WowClass와 Spec이 매개변수와 일치하는지 확인
        if (skill instanceof SkillInfo) { // SkillInfo를 상속했는지?
            SkillInfo wowSkill = (SkillInfo) skill;
            return wowSkill.getClassName().equals(wowClass)
                    && (wowSkill.getSpec().equals(spec) || wowSkill.getSpec().equals("ALL"));
        }
        return false;
    }

    private Class<?> getEnumClassName(String className) {

        WowClass wowClass = findWowClass(className);

        // 직업에 해당하는 열거 클래스를 찾아 반환
        switch (wowClass) {
            case WARRIOR:
                return Warrior.class;
            case HUNTER:
                return Hunter.class;
            case SHAMAN:
                return Shaman.class;
            case MONK:
                return Monk.class;
            case ROGUE:
                return Rogue.class;
            case DEATH_KNIGHT:
                return DeathKnight.class;
            case MAGE:
                return Mage.class;
            case DRUID:
                return Druid.class;
            case PALADIN:
                return Paladin.class;
            case PRIEST:
                return Priest.class;
            case WARLOCK:
                return Warlock.class;
            case DEMON_HUNTER:
                return DemonHunter.class;
            case EVOKER:
                return Evoker.class;
            default:
                return null;
        }
    }

    private int getSkillIdForFinder(Object skillEnum) {
        // 스킬번호 반환
        if (skillEnum instanceof SkillInfo) { // 공통 인터페이스인 SkillInfo를 상속했는지?
            return ((SkillInfo) skillEnum).getSkillId();
        }

        return 0;
    }

    private WowClass findWowClass(String className) {
        // 클래스 이름으로 WowClass enum 찾기
        return Arrays.stream(WowClass.values())
                .filter(wc -> wc.getDisplayName().equalsIgnoreCase(className))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid class name: " + className));

    }
}
