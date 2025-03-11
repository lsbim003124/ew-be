package com.lsbim.wowlsb.repo;

import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.repository.SpellRepository;
import com.lsbim.wowlsb.service.blizzard.BlizzardService;
import com.lsbim.wowlsb.service.repository.SpellService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class SpellTests {

/*    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private SpellService spellService;

    @Autowired
    private BlizzardService blizzardService;

    @Test
    public void spellTest1(){
        Set<Integer> spellIds = new HashSet<>();

        spellIds.add(22812);
        spellIds.add(108238);
        spellIds.add(5487);
        spellIds.add(102560);

        log.info("spellIds: {}", spellIds);

        List<Spell> spells = spellRepository.findByspellIdIn(spellIds);

        log.info("spells: {}", spells);

        Set<Integer> diffSet = new HashSet<>(spellIds);

        diffSet.removeAll(spells.stream()
                .map(Spell::getSpellId) // Spell::getSpellId == Spell -> Spell.getSpellId() 와 같다.
                .collect(Collectors.toSet())
        ); // Spells에 들어있는 Spell들의 spellId들을 diffSet에서 제거, Spells에 없는것들만 남는다

        log.info("diffSet: {}", diffSet);
        log.info("diffSet size: " + diffSet.size());
    }

    @Test
    public void spellTest2(){
        Set<Integer> spellIds = new HashSet<>();

        spellIds.add(22812);
        spellIds.add(108238);
        spellIds.add(5487);
        spellIds.add(102560);

        log.info("spellIds: {}", spellIds);

        List<Spell> spells = spellService.getBySpellIds(spellIds);

        log.info("spells: {}", spells);
    }*/
}
