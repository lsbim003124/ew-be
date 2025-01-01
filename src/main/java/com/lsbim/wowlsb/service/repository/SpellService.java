package com.lsbim.wowlsb.service.repository;

import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.repository.SpellRepository;
import com.lsbim.wowlsb.service.BlizzardService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class SpellService {

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private BlizzardService blizzardService;

    @Transactional
    public void addSpell(int spellId) {
        String spellName = blizzardService.getSpellInfoByBlizzard(spellId);

        if (spellName == null) {
            log.info("spellId: " + spellId + "'s spellName is null");
            return;
        }

        Spell spell = Spell.builder()
                .spellName(spellName)
                .spellId(spellId)
                .build();

        spellRepository.save(spell);
        log.info("Insert. Spell. => " + spell);
    }

    public List<Spell> findBySpellIds(Set<Integer> spellIds) {

        List<Spell> spells = spellRepository.findByspellIdIn(spellIds);

        Set<Integer> diffSet = new HashSet<>(spellIds);

        diffSet.removeAll(spells.stream()
                .map(Spell::getSpellId) // Spell::getSpellId == Spell -> Spell.getSpellId() 와 같다.
                .collect(Collectors.toSet())
        ); // Spells에 들어있는 Spell들의 spellId들을 diffSet에서 제거, Spells에 없는것들만 남는다

        if (diffSet.size() == 0) {
            return spells;
        }

        for(int spellId : diffSet){
            addSpell(spellId);
        }

        return spellRepository.findByspellIdIn(spellIds);
    }
}
