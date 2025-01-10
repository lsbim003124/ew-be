package com.lsbim.wowlsb.repository;

import com.lsbim.wowlsb.entity.MplusTimelineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MplusTimelineDataRepository extends JpaRepository<MplusTimelineData, Long> {

    @Query("SELECT mt.timelineData " +
            "FROM MplusTimelineData mt " +
            "WHERE mt.className = :className AND mt.specName = :specName AND mt.dungeonId = :dungeonId")
    String findTimelineDataByClassNameAndSpecNameAndDungeonId(@Param("className") String className // 메소드명 변경 예정
            , @Param("specName") String specName
            , @Param("dungeonId") int dungeonId);
}
