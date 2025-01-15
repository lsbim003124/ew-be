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

    private final Map<String, Map<Integer, String>> timelineData = new HashMap<>();

    public String getData(String key, int dungeonId){
        return Optional.ofNullable(timelineData.get(key)) // className-specName으로 데이터 꺼내기
                .map(m -> m.get(dungeonId)) // dungeonId로 이중맵 데이터 꺼내기
                .orElse(null); // 없으면 null
    }

    public void putData(String key, int dungeonId, String data){
        timelineData.computeIfAbsent(key, k -> new HashMap<>()) // key로 맵 내 검색하고 없으면 새 맵 생성
                .put(dungeonId, data); // 맵 안에 dungeonId 맵 put
    }

    public void timelineLog(){
        timelineData.forEach((key, value) -> {
            log.info("Key: " + key + " has " + value.toString().length() + " size value.");
        });
    }
}
