package com.lsbim.wowlsb.api.mplus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.enums.dungeons.Dungeons;
import com.lsbim.wowlsb.service.ProcessingService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class MplusFileSaveTests {

    @Value("${base.location}")
    private String basePath;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProcessingService processingService;

    @Test
    public void fileSaveTest1() {

        List<Integer> dungeonIds = Arrays.stream(Dungeons.values())
                .map(Dungeons::getId)
                .collect(Collectors.toList());

        log.info(dungeonIds);

        String className = "Priest";
        String spec = "Discipline";

        try {
            // 기본 저장 경로 설정 (파일 객체 생성)
            File baseDir = new File(basePath);
            if (!baseDir.exists()) {
                baseDir.mkdir();
            }

            for (int dungeonId : dungeonIds) {

                ObjectNode ob = processingService.doProcessing(className, spec, dungeonId);

                // 던전 폴더 생성(dungeonId)
                File dungeonDir = new File(baseDir, String.valueOf(dungeonId));
                if (!dungeonDir.exists()) {
                    dungeonDir.mkdir();
                }

                // 직업 폴더 생성 ex)Priest
                File classDir = new File(dungeonDir, className);
                if (!classDir.exists()) {
                    classDir.mkdir();
                }

                // json 파일 생성 ex)Discipline.json
                File outputFile = new File(classDir, spec + ".json");

                // 파일 저장
                om.writerWithDefaultPrettyPrinter()
                        .writeValue(outputFile, ob);

                log.info("Rankings data has been saved for dungeon {} to {}",
                        dungeonId, outputFile.getAbsolutePath());


                Thread.sleep(1000);
            }

        } catch (Exception e) {
            log.error("Failed to process dungeons data", e);
        }

    }
}
