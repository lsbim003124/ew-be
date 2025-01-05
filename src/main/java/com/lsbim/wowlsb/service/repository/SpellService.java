package com.lsbim.wowlsb.service.repository;

import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.repository.SpellRepository;
import com.lsbim.wowlsb.service.BlizzardService;
import com.lsbim.wowlsb.util.CustomSpellConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class SpellService {

    private final SpellRepository spellRepository;

    private final BlizzardService blizzardService;

    private final CustomSpellConfig customSpellConfig;

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

    public List<Spell> getBySpellIds(Set<Integer> spellIds) {

        List<Spell> spells = spellRepository.findByspellIdIn(spellIds);

        Set<Integer> diffSet = new HashSet<>(spellIds);

        diffSet.removeAll(spells.stream()
                .map(Spell::getSpellId) // Spell::getSpellId == Spell -> Spell.getSpellId() 와 같다.
                .collect(Collectors.toSet())
        ); // Spells에 들어있는 Spell들의 spellId들을 diffSet에서 제거, Spells에 없는것들만 남는다

        if (diffSet.size() == 0) {
            return spells;
        }

        for (int spellId : diffSet) {
            if (customSpellConfig.CUSTOM_SPELLS.containsKey(spellId)) {
                customSpellConfig.createCustomSpell(spellId);
            } else {
                log.info("Insert Spell List -> {}", diffSet);
                addSpell(spellId);
            }
        }

        return spellRepository.findByspellIdIn(spellIds);
    }
}
