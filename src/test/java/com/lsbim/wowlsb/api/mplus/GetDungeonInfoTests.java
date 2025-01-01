package com.lsbim.wowlsb.api.mplus;

import com.lsbim.wowlsb.enums.Mplus;
import com.lsbim.wowlsb.enums.dungeons.Dungeons;
import com.lsbim.wowlsb.service.DungeonService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
public class GetDungeonInfoTests {

    @Autowired
    private DungeonService dungeonService;

    @Test
    public void getDungeonIdByMplusTest1() {

        for(Mplus mplus : Mplus.values()) {
            Dungeons dungeons = Arrays.stream(Dungeons.values())
                    .filter(d -> d.name().equals(mplus.name()))
                    .findFirst()
                    .get();

            log.info("name: " + dungeons.getName());
            log.info("id: " + dungeons.getId());
        }
    }
}
