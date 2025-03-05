package com.lsbim.wowlsb.repository;

import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import com.lsbim.wowlsb.entity.MplusTimelineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MplusTimelineDataRepository extends JpaRepository<MplusTimelineData, Long> {

//    주의: JPQL 보기좋게 줄바꿈 하되 끝에 한칸 공백을 주어야 오류가 안 난다.
    @Query("SELECT new com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO(mt.timelineData, mt.createdDate) " +
            "FROM MplusTimelineData mt " +
            "WHERE mt.className = :className AND mt.specName = :specName AND mt.dungeonId = :dungeonId " +
            "ORDER BY mt.dataId DESC " +
            "LIMIT 1")
    MplusTimelineDataDTO findTimelineDataByClassNameAndSpecNameAndDungeonId(@Param("className") String className // 
            , @Param("specName") String specName
            , @Param("dungeonId") int dungeonId);

    @Modifying
    @Query(value = "DELETE t " +
            "FROM mplus_timeline_data AS t " +
            "JOIN ( " +
            "    SELECT sub.data_id " +
            "    FROM ( " +
            "        SELECT " +
            "            mtd.data_id, " +
            "            ROW_NUMBER() OVER ( " +
            "                PARTITION BY mtd.spec_name, mtd.dungeon_id " +
            "                ORDER BY mtd.created_date DESC " +
            "            ) AS rn, " +
            "            mtd.created_date " +
            "        FROM mplus_timeline_data mtd " +
            "    ) sub " +
            "    WHERE sub.rn > :keepCount " +
            "    AND sub.created_date < DATE_SUB(NOW(), INTERVAL :days DAY) " +
            ") del ON t.data_id = del.data_id", nativeQuery = true)
    int deleteOldTimelineData(@Param("days") int days, @Param("keepCount") int keepCount);
}
