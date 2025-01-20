package com.lsbim.wowlsb.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class TimelineCache {

    private final Map<String, String> timelineData = new HashMap<>();

    public String getData(String key){
        return Optional.ofNullable(timelineData.get(key)) // className-specName으로 데이터 꺼내기
                .orElse(null); // 없으면 null
    }

    public void putData(String key, String data){
        timelineData.put(key, data);
    }

    public void timelineLog(){
        timelineData.forEach((key, value) -> {
            log.info("Key: " + key + " has " + value.toString().length() + " size value.");
        });
    }
}
