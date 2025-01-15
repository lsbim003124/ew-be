package com.lsbim.wowlsb.service.task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.cache.TimelineCache;
import com.lsbim.wowlsb.entity.WowSpec;
import com.lsbim.wowlsb.enums.dungeons.Dungeons;
import com.lsbim.wowlsb.service.ProcessingService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import com.lsbim.wowlsb.service.repository.WowClassService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TaskService {

    private final WowClassService wowClassService;
    private final ProcessingService processingService;
    private final MplusTimelineDataService mplusTimelineDataService;
    private final TimelineCache timelineCache;

    @Getter
    private List<WowSpec> wowSpecList = new ArrayList<>();

    public void loadWowSpec() {
        wowSpecList = wowClassService.getSpecAll();
        log.info("get spec list...");
    }

    @PostConstruct
    public void initWowSpecList() {
        loadWowSpec();
    }

    @Getter
    private int index = 13;

    public void increaseSpecIndex() {
        index = (index + 1) % 39; // 39개의 전문화를 순회
    }


    List<Integer> dungeonIds = Arrays.stream(Dungeons.values())
            .map(Dungeons::getId)
            .collect(Collectors.toList());

    @Scheduled(fixedRate = 3900000) // 1시간 5분
    public void mplusTimelineSchedul() {


        List<WowSpec> wowSpecList = getWowSpecList();
        String thisClass = wowSpecList.get(index).getWowClass().getClassName();
        String thisSpec = wowSpecList.get(index).getSpecName();

        log.info("processing data for class: {}, spec: {}", thisClass, thisSpec);
        increaseSpecIndex(); // 인덱스++
        if (index > wowSpecList.size()) {
            index = 0;
        }

        try {
            for (int dungeonid : dungeonIds) {
                String rankingsData = processingService.doProcessing(thisClass, thisSpec, dungeonid).toString();
                // Map에 캐싱
                timelineCache.putData(thisClass + "-" + thisSpec, dungeonid, rankingsData);
                // DB에도 저장
                mplusTimelineDataService.addTimelineData(thisClass, thisSpec, dungeonid, rankingsData);
            }

            log.info("ok! get " + thisClass + "-" + thisSpec + " data...");

            timelineCache.timelineLog();
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("WCL API Too Many Request... >> {} {}", thisClass, thisSpec);

        } catch (Exception e) {
            log.error("Failed process timeline data... >> {} {}", thisClass, thisSpec, e);
        }
    }

        public String getTimelineData (String className, String specName,int dungeonId){
            String timelineData = timelineCache.getData(className + "-" + specName, dungeonId);

            if (timelineData == null) {
                timelineData = mplusTimelineDataService.findTimelineData(className, specName, dungeonId);

                timelineCache.putData(className + "-" + specName, dungeonId, timelineData);
            }

            return timelineData;
        }

    }
