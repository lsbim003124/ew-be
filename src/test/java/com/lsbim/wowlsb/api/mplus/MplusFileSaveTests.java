package com.lsbim.wowlsb.api.mplus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.entity.WowSpec;
import com.lsbim.wowlsb.enums.dungeons.Dungeons;
import com.lsbim.wowlsb.service.ProcessingService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import com.lsbim.wowlsb.service.repository.WowClassService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

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

    @Autowired
    private WowClassService wowClassService;
/*    @Autowired
    private MplusTimelineDataService mplusTimelineDataService;*/

/*    @Test
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

    }*/

/*    @Test
    public void fileSaveTest2() {
        List<WowSpec> arr = wowClassService.getSpecAll();
        List<Integer> dungeonArr = Arrays.stream(Dungeons.values())
                .map(Dungeons::getId)
                .collect(Collectors.toList());
        log.info(dungeonArr);

        for (int si = 0; si < arr.size(); ) {
            try {

                String className = arr.get(si).getWowClass().getClassName();
                String specName = arr.get(si).getSpecName();

                log.info("className: {}, specName: {}", className, specName);

                try {
                    // 기본 저장 경로 설정 (파일 객체 생성)
                    File baseDir = new File(basePath);
                    if (!baseDir.exists()) {
                        baseDir.mkdir();
                    }

                    for (int di = 0; di < dungeonArr.size(); ) {
                        try {
                            ObjectNode ob = processingService.mplusTimelineProcessing(className, specName, dungeonArr.get(di));
                            if (ob == null) {
                                log.warn("{}, {} object is null, retrying index: {}", className, specName, di);
                                try {
                                    Thread.sleep(120000); // 2분 대기
                                } catch (InterruptedException ie) {
                                    log.warn("Sleep interrupted: ", ie);
                                    Thread.currentThread().interrupt();
                                }
                                continue;
                            }

                            // 던전 폴더 생성(dungeonId)
                            File dungeonDir = new File(baseDir, String.valueOf(dungeonArr.get(di)));
                            if (!dungeonDir.exists()) {
                                dungeonDir.mkdir();
                            }

                            // 직업 폴더 생성 ex)Priest
                            File classDir = new File(dungeonDir, className);
                            if (!classDir.exists()) {
                                classDir.mkdir();
                            }

                            // json 파일 생성 ex)Discipline.json
                            File outputFile = new File(classDir, specName + ".json");

                            // 파일 저장
                            om.writerWithDefaultPrettyPrinter()
                                    .writeValue(outputFile, ob);

                            log.info("Rankings data has been saved for dungeon {} to {}",
                                    dungeonArr.get(di), outputFile.getAbsolutePath());


                            Thread.sleep(1000);
                            di++;
                        } catch (HttpClientErrorException.TooManyRequests e) {
                            log.warn("Too Many Requests index {}, Waiting for 1m before retrying.", di);
                            log.warn(e.getMessage());
                            try {
                                Thread.sleep(120000); // 2분 대기
                            } catch (InterruptedException ie) {
                                log.warn("Sleep interrupted: ", ie);
                                Thread.currentThread().interrupt();
                            }
                        } catch (Exception e) {
                            log.warn("Exception... index: {}", di);
                            log.warn(e.getMessage());
                            try {
                                Thread.sleep(120000); // 2분 대기
                            } catch (InterruptedException ie) {
                                log.warn("Sleep interrupted: ", ie);
                                Thread.currentThread().interrupt();
                            }
                        }

                    } // 던전순회 종료

                } catch (Exception e) {
                    log.error("Failed to process dungeons data: {}", e.getMessage());
                }

                si++;
            } catch (Exception e) {
                log.warn("Exception... index: {}", si);
                log.warn(e.getMessage());
                try {
                    Thread.sleep(120000); // 2분 대기
                } catch (InterruptedException ie) {
                    log.warn("Sleep interrupted: ", ie);
                    Thread.currentThread().interrupt();
                }
            }
        } // 전문화 순회 종료

    }*/
}
