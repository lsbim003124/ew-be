package com.lsbim.wowlsb.repository;

import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import com.lsbim.wowlsb.entity.MplusTimelineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MplusTimelineDataRepository extends JpaRepository<MplusTimelineData, Long> {

//    주의: JPQL 보기좋게 줄바꿈 하되 끝에 한칸 공백을 주어야 오류가 안 난다.
    @Query("SELECT mt.timelineData, mt.createdDate " +
            "FROM MplusTimelineData mt " +
            "WHERE mt.className = :className AND mt.specName = :specName AND mt.dungeonId = :dungeonId " +
            "ORDER BY mt.dataId DESC " +
            "LIMIT 1")
    MplusTimelineDataDTO findTimelineDataByClassNameAndSpecNameAndDungeonId(@Param("className") String className // 메소드명 변경 예정
            , @Param("specName") String specName
            , @Param("dungeonId") int dungeonId);
}
