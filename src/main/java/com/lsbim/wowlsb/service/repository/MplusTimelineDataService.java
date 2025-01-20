package com.lsbim.wowlsb.service.repository;

import com.lsbim.wowlsb.cache.TimelineCache;
import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import com.lsbim.wowlsb.entity.MplusTimelineData;
import com.lsbim.wowlsb.repository.MplusTimelineDataRepository;
import com.lsbim.wowlsb.service.queue.QueueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class MplusTimelineDataService {

    private final MplusTimelineDataRepository mplusTimelineDataRepository;
    private final TimelineCache timelineCache;
    private final QueueService queueService;

    @Transactional
    public void addTimelineData(String className, String specName, int dungeonId, String timelineData) {
        MplusTimelineData addData = MplusTimelineData.builder()
                .className(className)
                .specName(specName)
                .dungeonId(dungeonId)
                .timelineData(timelineData)
                .build();

        mplusTimelineDataRepository.save(addData);

        log.info("insert timeline data... class: " + className + ", spec: " + specName + ", dungeonId: " + dungeonId);
    }

    @Transactional
    private MplusTimelineDataDTO findTimelineData(String className, String specName, int dungeonId) {
        MplusTimelineDataDTO findData = mplusTimelineDataRepository.findTimelineDataByClassNameAndSpecNameAndDungeonId(className, specName, dungeonId);

        return findData;
    }

    public String getTimelineData(String className, String specName, int dungeonId) {
        String timelineData = timelineCache.getData(className + "-" + specName + "-" + dungeonId);

        if (timelineData == null) {
            MplusTimelineDataDTO dto = findTimelineData(className, specName, dungeonId);

            if (dto != null && isDataExpired(dto.getCreatedDate())) {
                timelineData = dto.getTimelineData();
            } else {
                timelineData = queueService.enqueueTask(className, specName, dungeonId);
                if (timelineData == null) {
                    return null;
                }
//                갱신한 데이터 DB에 저장
                addTimelineData(className, specName, dungeonId, timelineData);
            }
//              갱신한 데이터 캐싱
            timelineCache.putData(className + "-" + specName + "-" + dungeonId, timelineData);
        }

        return timelineData;
    }

    private boolean isDataExpired(LocalDateTime createdDate) {
        // 기준 시간 createdDate이 비교대상보다 이전인지? .isBefore
        return createdDate.isBefore(LocalDateTime.now().minusWeeks(2));
    }
}
