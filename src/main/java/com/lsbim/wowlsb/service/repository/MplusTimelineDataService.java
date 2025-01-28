package com.lsbim.wowlsb.service.repository;

import com.lsbim.wowlsb.cache.TimelineCache;
import com.lsbim.wowlsb.dto.ApiResponseDTO;
import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import com.lsbim.wowlsb.entity.MplusTimelineData;
import com.lsbim.wowlsb.repository.MplusTimelineDataRepository;
import com.lsbim.wowlsb.service.queue.QueueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

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

    public ApiResponseDTO getTimelineData(String className, String specName, int dungeonId) {
        String cacheKey = className + "-" + specName + "-" + dungeonId;
        String timelineData = timelineCache.getData(cacheKey);

        // 캐시에 데이터가 있으면 반환
        if (timelineData != null) {
            return new ApiResponseDTO("COMPLETE", timelineData);
        }

        // DB에서 데이터 조회
        MplusTimelineDataDTO dto = findTimelineData(className, specName, dungeonId);

        // 데이터가 없는 경우
        if (dto == null) {
            scheduleDataUpdate(className, specName, dungeonId, cacheKey);
            return new ApiResponseDTO("UPDATING", null);
        }

        // 데이터가 오래된 경우, DTO의 데이터를 반환하고 비동기 데이터 갱신
        timelineData = dto.getTimelineData();
        if (isDataExpired(dto.getCreatedDate())) {
            scheduleDataUpdate(className, specName, dungeonId, cacheKey);
        }

        // DTO 데이터를 캐시에 저장하고 데이터 반환
        timelineCache.putData(cacheKey, timelineData);
        return new ApiResponseDTO("COMPLETE", timelineData);
    }

    private void scheduleDataUpdate(String className, String specName, int dungeonId, String cacheKey) {
        // 이미 업데이트 중인 경우 중복 업데이트 방지
        if (queueService.isTaskInSet(className, specName, dungeonId)) {
            log.info("This data updating now: {}, {}, {}", className, specName, dungeonId);
            return;
        }

        CompletableFuture.supplyAsync(() ->
                queueService.enqueueTask(className, specName, dungeonId)
        ).thenAccept(result -> {
            if (result != null) {
                addTimelineData(className, specName, dungeonId, result);
                timelineCache.putData(cacheKey, result);
                log.info("Data update success: {}", cacheKey);
            }
        }).exceptionally(e -> {
            log.error("Error during task: {}", e);
            return null;
        });
    }

    private boolean isDataExpired(LocalDateTime createdDate) {
        // 기준 시간 createdDate이 비교대상보다 이전인지? .isBefore
        return createdDate.isBefore(LocalDateTime.now().minusWeeks(2));
    }
}
