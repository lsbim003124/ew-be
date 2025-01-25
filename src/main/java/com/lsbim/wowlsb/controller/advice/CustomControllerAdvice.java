package com.lsbim.wowlsb.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class CustomControllerAdvice {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException e) {
        HttpStatusCode code = e.getStatusCode();

//        Bucket 토큰 과소비하면 투 매니 리퀘스트 status: 429
        if (code == HttpStatusCode.valueOf(429)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Request");
        }

        // 다른 400대 상태 코드에 대한 처리
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getReason());
    }

    @ExceptionHandler(HttpServerErrorException.ServiceUnavailable.class) // status: 503
    public ResponseEntity<String> handleUnavailable(HttpServerErrorException.ServiceUnavailable e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Public API Server Error");
    }
}
