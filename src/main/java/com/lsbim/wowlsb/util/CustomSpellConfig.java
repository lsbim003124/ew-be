package com.lsbim.wowlsb.util;

import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.repository.SpellRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomSpellConfig {

    private final SpellRepository spellRepository;

    public static final Map<Integer, String> CUSTOM_SPELLS = Map.of(
            451235,"공허의 마귀",
            452930,"악마의 생명석",
            414658,"얼음장",
            370564,"정지장 배출"
    );

    @Transactional
    public void createCustomSpell(int spellId){
        String spellName = CUSTOM_SPELLS.get(spellId);

        Spell customSpell = Spell.builder()
                .spellId(spellId)
                .spellName(spellName)
                .build();

        spellRepository.save(customSpell);
        log.info("Insert. Custom Spell. => " + customSpell);
    }
}
