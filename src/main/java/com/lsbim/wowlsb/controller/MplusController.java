package com.lsbim.wowlsb.controller;

import com.lsbim.wowlsb.dto.ApiResponseDTO;
import com.lsbim.wowlsb.service.ProcessingService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/mplus/")
@Log4j2
public class MplusController {

    private final MplusTimelineDataService mplusTimelineDataService;
    private final Bucket bucket;

    @GetMapping("timeline")
    public ResponseEntity<?> get(@RequestParam String className,
                                 @RequestParam String specName,
                                 @RequestParam int dungeonId) {
        try {
            if (bucket.tryConsume(1)) {

                ApiResponseDTO data = mplusTimelineDataService.getTimelineData(className, specName, dungeonId);

                log.info("get timeline... class: {}, spec: {}, dungeonId: {}", className, specName, dungeonId);

                return ResponseEntity.ok(data);
            } else {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
            }
        } catch (NullPointerException e) {
            log.warn("timeline data is null");
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Timeline data is null");
        } catch (Exception e) {
            log.warn("get timeline data error: {}", e);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Failed to fetch timeline data");
        }
    }
}
