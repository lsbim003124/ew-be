package com.lsbim.wowlsb.controller;

import com.lsbim.wowlsb.service.ProcessingService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/mplus/")
@Log4j2
public class MplusController {

    private final MplusTimelineDataService mplusTimelineDataService;

    @GetMapping("timeline")
    public ResponseEntity<?> get(@RequestParam String className,
                                 @RequestParam String specName,
                                 @RequestParam int dungeonId) {
        try {
            String data = mplusTimelineDataService.getTimelineData(className, specName, dungeonId);

            log.info("get timeline... class: {}, spec: {}, dungeonId: {}", className, specName, dungeonId);

            return ResponseEntity.ok(data);
        } catch (NullPointerException e) {
            log.warn("timeline data is null");
            return ResponseEntity.status(HttpStatus.OK)
                    .body("timeline data is null");
        } catch (Exception e) {
            log.warn("get timeline data error: {}", e);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Failed to fetch timeline data");
        }
    }
}
