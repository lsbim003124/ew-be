package com.lsbim.wowlsb.service.repository;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.cache.TimelineCache;
import com.lsbim.wowlsb.dto.ApiResponseDTO;
import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import com.lsbim.wowlsb.entity.MplusTimelineData;
import com.lsbim.wowlsb.enums.utils.ApiStatus;
import com.lsbim.wowlsb.repository.MplusTimelineDataRepository;
import com.lsbim.wowlsb.service.queue.QueueService;
import com.lsbim.wowlsb.service.validation.MplusValidationService;
import jakarta.transaction.Transactional;
import lombok.Builder;
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
    private final MplusValidationService mplusValidationService;

    @Transactional
    private void addTimelineData(String className, String specName, int dungeonId, ObjectNode timelineData) {
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
        ObjectNode timelineData = timelineCache.getData(cacheKey);

        // 캐시에 데이터가 있으면 반환
        if (timelineData != null) {
//            캐시데이터 날짜체크
            LocalDateTime dataTime = timelineCache.getDataTime(cacheKey);

            if(dataTime != null){
//                2주이상 지난 데이터인지?
                if (mplusValidationService.isDataExpired(dataTime)) {
                    log.info("Old Data CreatedDate: {}",dataTime);
                } else {
                    return new ApiResponseDTO(ApiStatus.COMPLETE, timelineData);
                }
            }
        }

        // DB에서 데이터 조회
        MplusTimelineDataDTO dto = findTimelineData(className, specName, dungeonId);

        // 데이터가 없는 경우
        if (dto == null) {
            scheduleDataUpdate(className, specName, dungeonId, cacheKey);
            return new ApiResponseDTO(ApiStatus.UPDATING, null);
        }

        // 데이터가 오래된 경우, DTO의 데이터를 반환하고 비동기 데이터 갱신
        timelineData = dto.getTimelineData();
        if(!mplusValidationService.isDataExpired(dto.getCreatedDate())){
            return new ApiResponseDTO(ApiStatus.COMPLETE, timelineData);
        }

//        갱신해야하는 오래된 데이터의 중복 검증, 중복 시 새로 저장하여 createDate갱신, 중복이 아니면 새 데이터 생성
        if(mplusValidationService.isDuplicateTimelineData(dto,className,specName,dungeonId)){
            log.info("Timeline Data is Duplicate");
            addTimelineData(className, specName, dungeonId, timelineData);
            dto.setCreatedDate(LocalDateTime.now());
            timelineCache.putData(cacheKey, dto);
        } else {
            log.info("Timeline Data is not Duplicate");
            scheduleDataUpdate(className, specName, dungeonId, cacheKey); // 중복이 아니면 새 데이터 저장 후 불러오기
        }

        return new ApiResponseDTO(ApiStatus.COMPLETE, timelineData);
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
                MplusTimelineDataDTO dto = MplusTimelineDataDTO.builder()
                        .timelineData(result)
                        .createdDate(LocalDateTime.now())
                        .build();
                timelineCache.putData(cacheKey, dto);
                log.info("Data update success: {}", cacheKey);
            }
        }).exceptionally(e -> {
            log.error("Error during task: {}", e);
            return null;
        });
    }
}
