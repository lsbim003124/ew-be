package com.lsbim.wowlsb.util.init;

import com.lsbim.wowlsb.cache.SpellIdCache;
import com.lsbim.wowlsb.repository.SpellRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class WowSpellIdInit {

    private final SpellRepository spellRepository;
    private final SpellIdCache spellIdCache;

    public void initWowSpellIdSet() {
        Set<Integer> spellIds = spellRepository.findAllSpellIds();

        spellIdCache.addMissingIds(spellIds);
    }
}
