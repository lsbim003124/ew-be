package com.lsbim.wowlsb.controller;

import com.lsbim.wowlsb.controller.advice.RateLimit;
import com.lsbim.wowlsb.dto.ApiResponseDTO;
import com.lsbim.wowlsb.service.ProcessingService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import com.lsbim.wowlsb.service.validation.MplusValidationService;
import com.lsbim.wowlsb.util.validation.IpValidator;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/mplus/")
@Log4j2
public class MplusController {

    private final MplusTimelineDataService mplusTimelineDataService;
    private final MplusValidationService mplusValidationService;
    private final RateLimit rateLimit;
    private final IpValidator ipValidator;

    @GetMapping("timeline")
    public ResponseEntity<?> get(HttpServletRequest request,
                                 @RequestParam String className,
                                 @RequestParam String specName,
                                 @RequestParam int dungeonId) {

        if (!mplusValidationService.validateTimelineParams(className, specName, dungeonId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid params");
        }

        try {
            String ip = ipValidator.validateIp(request);


            if (rateLimit.tryConsume(ip)) {
                ApiResponseDTO data = mplusTimelineDataService.getTimelineData(className, specName, dungeonId);

                log.info("Response Data - className: {}, specName: {}, dungeonId: {}, ip: {}"
                        , className, specName, dungeonId, ip);
                return ResponseEntity.ok(data);
            } else {
                log.info("Rate limit exceeded request - className: {}, specName: {}, dungeonId: {}"
                        , className, specName, dungeonId);
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("Too many request");
            }

        } catch (NullPointerException e) {
            log.warn("timeline data is null: class={}, spec={}, dungeon={}",
                    className, specName, dungeonId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Timeline data is null");

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid IP");

        } catch (Exception e) {
            log.warn("get timeline data error: class={}, spec={}, dungeon={}",
                    className, specName, dungeonId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch timeline data");
        }
    }
}
