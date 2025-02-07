package com.lsbim.wowlsb.cache;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class TimelineCache {

    private final Map<String, MplusTimelineDataDTO> timelineData = new HashMap<>();

    public ObjectNode getData(String key){
        return Optional.ofNullable(timelineData)  // timelineData 자체를 먼저 Optional로 감싸기
                .map(data -> data.get(key))       // key로 데이터 조회
                .map(MplusTimelineDataDTO::getTimelineData)  // TimelineData에서 실제 데이터 추출
                .orElse(null); // 없으면 null
    }

    public LocalDateTime getDataTime(String key){
        return Optional.ofNullable(timelineData)
                .map(data -> data.get(key))
                .map(MplusTimelineDataDTO::getCreatedDate)
                .orElse(null);
    }

    public void putData(String key, MplusTimelineDataDTO data){
        timelineData.put(key, data);
    }

    public void timelineLog(){
        timelineData.forEach((key, value) -> {
            log.info("Key: " + key + " has " + value.toString().length() + " size value.");
        });
    }
}
