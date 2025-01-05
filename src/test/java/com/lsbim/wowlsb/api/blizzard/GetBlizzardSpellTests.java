package com.lsbim.wowlsb.api.blizzard;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.repository.SpellRepository;
import com.lsbim.wowlsb.service.BlizzardService;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest
@Log4j2
public class GetBlizzardSpellTests {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wow.spell.url}")
    private String apiUrl;

    @Value("${wow.token}")
    private String token;

    @Value("${wow.api.param}")
    private String wowParam;

    @Autowired
    private BlizzardService blizzardService;

    @Autowired
    private SpellRepository spellRepository;

    @Test
    public void getBlizzradSpellTest1() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity requestEntity = new HttpEntity(headers);

        int spellId = 102560;

        String url = apiUrl + spellId + wowParam;

        // API 요청
        ResponseEntity<ObjectNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                ObjectNode.class
        );

        log.info(response);
        String spellName = response.getBody().path("name").asText();

        log.info("spellName: {}", spellName);
    }

    @Test
    public void getBlizzradSpellTest2() {
        int spellId = 448847;

        String spellName = blizzardService.getSpellInfoByBlizzard(spellId);

        log.info("spellName: {}", spellName);

        Spell spell = Spell.builder()
                .spellId(spellId)
                .spellName(spellName)
                .build();

        log.info("spell: {}", spell);

        spellRepository.save(spell);

        log.info("=================================");

        Optional<Spell> find = spellRepository.findById(spellId);

        if (find.isPresent()) {
            log.info("find: {}", find);
        }
    }

    @Test
    public void getBlizzradSpellTest3() {
        Set<Integer> spells = new HashSet<>(Arrays.asList(448847, 448877, 447261, 448953));

        for (int spellId : spells) {

            String spellName = blizzardService.getSpellInfoByBlizzard(spellId);

            log.info("spellName: {}", spellName);

            Spell spell = Spell.builder()
                    .spellId(spellId)
                    .spellName(spellName)
                    .build();

            log.info("spell: {}", spell);

            spellRepository.save(spell);

            log.info("=================================");

            Optional<Spell> find = spellRepository.findById(spellId);

            if (find.isPresent()) {
                log.info("find: {}", find);
            }
        }
    }
}
