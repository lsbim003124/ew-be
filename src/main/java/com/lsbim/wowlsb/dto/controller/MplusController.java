package com.lsbim.wowlsb.dto.controller;

import com.lsbim.wowlsb.service.task.TaskService;
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

    private final TaskService taskService;

    @GetMapping("timeline")
    public ResponseEntity<?> get(@RequestParam String className,
                      @RequestParam String specName,
                      @RequestParam int dungeonId) {
        try {
            String data = taskService.getTimelineData(className, specName, dungeonId);

            log.info("get timeline...");

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch timeline data");
        }
    }
}
