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
        return Optional.ofNullable(timelineData.get(key))
                .map(m -> m.get(dungeonId))
                .orElse(null);
    }

    public void putData(String key, int dungeonId, String data){
        timelineData.computeIfAbsent(key, k -> new HashMap<>())
                .put(dungeonId, data);
    }

    public void timelineLog(){
        timelineData.forEach((key, value) -> {
            log.info("Key: " + key + " has " + value.toString().length() + " size value.");
        });
    }
}
