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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.TooManyListenersException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Log4j2
public class MplusTimelineDataService {

    private final MplusTimelineDataRepository mplusTimelineDataRepository;
    private final TimelineCache timelineCache;
    private final QueueService queueService;
    private final MplusValidationService mplusValidationService;

    final static int DELETE_DAYS = 3;
    final static int DELETE_KEEP_COUNT = 2;


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

    //    테이블에서 시간, 데이터 컬럼만 가져옴
    @Transactional
    private MplusTimelineDataDTO findTimelineData(String className, String specName, int dungeonId) {
        MplusTimelineDataDTO findData = mplusTimelineDataRepository.findTimelineDataByClassNameAndSpecNameAndDungeonId(className, specName, dungeonId);

        return findData;
    }

    public ApiResponseDTO getTimelineData(String className, String specName, int dungeonId) {
        String cacheKey = className + "-" + specName + "-" + dungeonId;

//        해당 전문화+던전이 작업중이면 캐시/DB데이터 즉시반환
        if (queueService.isTaskInSet(className, specName, dungeonId)) {
            log.info("Data update queue already: {}", cacheKey);

            ObjectNode cachedData = timelineCache.getData(cacheKey);
//            캐시에 데이터가 있으면?
            if (cachedData != null) {
                return new ApiResponseDTO(ApiStatus.COMPLETE, cachedData);
            }

//            DB 조회 시 데이터가 존재하면?
            MplusTimelineDataDTO dbData = findTimelineData(className, specName, dungeonId);
            if (dbData != null) {
                return new ApiResponseDTO(ApiStatus.COMPLETE, dbData.getTimelineData());
            }

//            QUEUE 대기열에 존재하는 상황에 캐시된데이터도 DB에데이터도 없으면 null+UPDATING 상태 반환
            return new ApiResponseDTO(ApiStatus.UPDATING, null);
        }

        ObjectNode timelineData = timelineCache.getData(cacheKey);

        // 캐시에 데이터가 있으면 반환
        if (timelineData != null) {
            log.info("Cache data exist");
//            캐시데이터 날짜체크
            LocalDateTime dataTime = timelineCache.getDataTime(cacheKey);

            if (dataTime != null) {
//                일정기간 이상 지난 데이터인지?
                if (mplusValidationService.isDataExpired(dataTime)) {
                    log.info("Old Data CreatedDate: {}", dataTime);
                } else {
                    log.info("No Process Needed: {}", cacheKey);
                    return new ApiResponseDTO(ApiStatus.COMPLETE, timelineData);
                }
            }
        }

        // DB에서 데이터 조회
        MplusTimelineDataDTO dto = findTimelineData(className, specName, dungeonId);

        // 데이터가 없는 경우
        if (dto == null) {
            log.info("Timeline data is null. enqueue data: {}", cacheKey);
            scheduleDataUpdate(className, specName, dungeonId, cacheKey);
            return new ApiResponseDTO(ApiStatus.UPDATING, null);
        }

        // DB 데이터가 유효한 경우, DTO 캐싱 + 데이터만 반환
        if (!mplusValidationService.isDataExpired(dto.getCreatedDate())) {
            log.info("Return timeline data from db");
            timelineCache.putData(cacheKey, dto);
            return new ApiResponseDTO(ApiStatus.COMPLETE, dto.getTimelineData());
        }

//        데이터가 만료된 경우
        timelineData = dto.getTimelineData();

        boolean isDuplicate = false;

//        중복체크에서 투매니리퀘스트 발생 시 캐시에 넣어두고 추후 갱신 시도
        try {
            isDuplicate = mplusValidationService.isDuplicateTimelineData(dto, className, specName, dungeonId);
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("could not check duplicate: {}", cacheKey, e);
            timelineCache.putData(cacheKey, dto);
            return new ApiResponseDTO(ApiStatus.COMPLETE, dto.getTimelineData());
        }

//        호출횟수 초과한 상태에서 중복데이터 확인 불가능. 일단 리턴
        if (isDuplicate) {
            log.info("Timeline Data is Duplicate");
//            중복 데이터는 다시 저장하여 날짜 갱신
            addTimelineData(className, specName, dungeonId, timelineData);
            dto.setCreatedDate(LocalDateTime.now());
            timelineCache.putData(cacheKey, dto);
        } else {
            log.info("Timeline Data is not Duplicate");
            // 중복이 아니면 새 데이터 저장 후 불러오기
            scheduleDataUpdate(className, specName, dungeonId, cacheKey);
        }

        return new ApiResponseDTO(ApiStatus.COMPLETE, timelineData);
    }

    private void scheduleDataUpdate(String className, String specName, int dungeonId, String cacheKey) {
        // 이미 업데이트 중인 경우 중복 업데이트 방지
        synchronized (queueService) {
            if (queueService.isTaskInSet(className, specName, dungeonId)) {
                log.info("This data updating now: {}, {}, {}", className, specName, dungeonId);
                return;
            }
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

    @Transactional
    public int deleteTimelineData() {

//        얼마나 지워졌는지 리턴
        return mplusTimelineDataRepository.deleteOldTimelineData(DELETE_DAYS, DELETE_KEEP_COUNT);
    }
}
