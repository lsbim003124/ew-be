package com.lsbim.wowlsb.service.repository;

import com.lsbim.wowlsb.cache.SpellIdCache;
import com.lsbim.wowlsb.entity.Spell;
import com.lsbim.wowlsb.repository.SpellRepository;
import com.lsbim.wowlsb.service.blizzard.BlizzardService;
import com.lsbim.wowlsb.service.wcl.gameData.GameDataService;
import com.lsbim.wowlsb.util.CustomSpellConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

    private final SpellIdCache spellIdCache;

    private final GameDataService gameDataService;

    @Transactional
    public void addSpell(int spellId) {
        String spellName = blizzardService.getSpellInfoByBlizzard(spellId);

        if (spellName == null) {
            log.info("spellId: " + spellId + "'s spellName is null");
//            return;

//            현재  spring.jpa.hibernate.ddl-auto=update + 엔티티에서 nullable=false
//            제거해도 null을 거부하는 중. 임시로 빈 문자열 삽입
            spellName = "";
        }

        Spell spell = Spell.builder()
                .spellName(spellName)
                .spellId(spellId)
                .build();

        spellRepository.save(spell);
        log.info("Insert. Spell. => " + spell);
    }

    public List<Spell> getBySpellIds(Set<Integer> spellIds) {

//        diffSet이 반환된 시점에서 이미 새 주문번호들은 캐싱됨. -> 반환된 buffIds는 DB에 없던 주문목록들
        Set<Integer> diffSet = spellIdCache.addMissingIds(spellIds);

        if (diffSet.size() > 0) {
            for (int spellId : diffSet) {
                // 블리자드가 주지 않는 주문정보 수동삽입
                if (customSpellConfig.CUSTOM_SPELLS.containsKey(spellId)) {
                    customSpellConfig.createCustomSpell(spellId);
                } else {
                    log.info("Insert Spell List -> {}", diffSet);
//                    DB에도 추가
                    addSpell(spellId);
                }
            }

//        GCS에 이미지도 추가
            try {
                gameDataService.findAndUploadSpellImg(spellIds);
            } catch (HttpClientErrorException.TooManyRequests e){
                log.warn("Failed process get gameData...",e);
            }
        }

        return spellRepository.findByspellIdIn(spellIds);
    }
}
