package com.lsbim.wowlsb.repository;

import com.lsbim.wowlsb.entity.Spell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SpellRepository extends JpaRepository<Spell, Integer> {
//    제네릭 안에있는 <Spell, Integer>의 의미는 엔티티와 기본키의 자료타입.

    List<Spell> findByspellIdIn(Collection<Integer> ids); // Set, List, Map 등
}
