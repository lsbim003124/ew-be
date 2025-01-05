package com.lsbim.wowlsb.repository;

import com.lsbim.wowlsb.entity.WowSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WowSpecRepository extends JpaRepository<WowSpec, Integer> {


    @Query("SELECT ws.role " +
            "FROM WowSpec ws " +
            "JOIN ws.wowClass wc " + // 현재 WowSpec에 단방향으로 WowClass와 관계가 맺어져있기 때문에 ws.wowClass(속성명)로 조인
            "WHERE wc.className = :className AND ws.specName = :specName")
    String findRoleByClassNameAndSpecName(@Param("className") String className, @Param("specName") String specName);
}
