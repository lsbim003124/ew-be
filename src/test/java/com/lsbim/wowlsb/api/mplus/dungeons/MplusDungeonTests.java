package com.lsbim.wowlsb.api.mplus.dungeons;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.enums.dungeons.Dungeons;
import com.lsbim.wowlsb.service.ProcessingService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@SpringBootTest
public class MplusDungeonTests {

    @Autowired
    private ProcessingService processingService;

    @Test
    public void mplusDungeonTest1() {
        List<Integer> arr =  Arrays.stream(Dungeons.values())
                .map(d -> d.getId())
                .collect(Collectors.toList());

        log.info(arr);

        String className = "Druid";
        String spec = "Balance";
        for(int dungeonId : arr){
            ObjectNode ob = processingService.doProcessing(className, spec, dungeonId);
            log.info(ob.toString().length());
        }
    }
}
