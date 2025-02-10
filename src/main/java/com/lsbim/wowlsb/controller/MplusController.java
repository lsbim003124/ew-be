package com.lsbim.wowlsb.controller;

import com.lsbim.wowlsb.controller.advice.RateLimit;
import com.lsbim.wowlsb.dto.ApiResponseDTO;
import com.lsbim.wowlsb.service.ProcessingService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import com.lsbim.wowlsb.service.validation.MplusValidationService;
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

    @GetMapping("timeline")
    public ResponseEntity<?> get(HttpServletRequest request,
                                 @RequestParam String className,
                                 @RequestParam String specName,
                                 @RequestParam int dungeonId) {

        if (!mplusValidationService.validateTimelineParams(className, specName, dungeonId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid params");
        }

        String ip = getClientIp(request);

        if (ip == null || ip.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ip not found");
        }

        try {
            if (rateLimit.tryConsume(ip)) {
                ApiResponseDTO data = mplusTimelineDataService.getTimelineData(className, specName, dungeonId);

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

        } catch (Exception e) {
            log.warn("get timeline data error: class={}, spec={}, dungeon={}",
                    className, specName, dungeonId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch timeline data");
        }
    }


    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                log.warn("Unknown Host?: {}", e);
                throw new RuntimeException(e);
            }
            ip = address.getHostName() + "/" + address.getHostAddress();
        }

        return ip;
    }
}
