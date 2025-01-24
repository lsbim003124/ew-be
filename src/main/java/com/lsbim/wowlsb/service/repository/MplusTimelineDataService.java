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
        String timelineData = timelineCache.getData(className + "-" + specName + "-" + dungeonId);

        if (timelineData == null) {
            MplusTimelineDataDTO dto = findTimelineData(className, specName, dungeonId);

            if (dto != null && !isDataExpired(dto.getCreatedDate())) {
                timelineData = dto.getTimelineData();

            } else {
                if (queueService.isTaskInSet(className, specName, dungeonId)) {
                    log.info("This data updating now: {}, {}, {}", className, specName, dungeonId);
                    return new ApiResponseDTO("UPDATING", null);
                }
                log.info("Need new data this DTO: {}", dto);

//                비동기 작업. 즉시 클라이언트로 UPDATING 상태를 반환하기 위함
                CompletableFuture.supplyAsync(() ->
                        queueService.enqueueTask(className, specName, dungeonId)
                ).thenAccept(result -> {
                    if (result != null) {
                        addTimelineData(className, specName, dungeonId, result);
                        timelineCache.putData(className + "-" + specName + "-" + dungeonId, result);
                    }
                }).exceptionally(e -> {
                    log.error("Error during task: {}", e);
                    return null;
                });

                return new ApiResponseDTO("UPDATING", null);
            }
//              갱신한 데이터 캐싱
            timelineCache.putData(className + "-" + specName + "-" + dungeonId, timelineData);
        }

        return new ApiResponseDTO("COMPLETE", timelineData);
    }

    private boolean isDataExpired(LocalDateTime createdDate) {
        // 기준 시간 createdDate이 비교대상보다 이전인지? .isBefore
        return createdDate.isBefore(LocalDateTime.now().minusWeeks(2));
    }
}
