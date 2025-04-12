package com.lsbim.wowlsb.controller.advice;

import io.github.bucket4j.Bucket;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Log4j2
public class RateLimit {

    //    전역 이용자 카운터
    private final AtomicInteger userIpCounter = new AtomicInteger(0);

    //    IP 안에 버킷 보관
    private final ConcurrentHashMap<String, Bucket> ipBucket = new ConcurrentHashMap<>();

    private final int TOKEN_PER_SEC = 5;
    private final int MAX_IP_COUNTER = 1000;

    private Bucket createBucket() {

        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(TOKEN_PER_SEC) // 버킷의 토큰 최대 용량
                        .refillGreedy(TOKEN_PER_SEC, Duration.ofSeconds(1)) // 1초에 n개의 토큰 리필
                        .initialTokens(TOKEN_PER_SEC) // 초기 토큰 n개 발급
                )
                .build();
    }

    //    토큰 소비
    public boolean tryConsume(String ip) {

        Bucket bucket = ipBucket.computeIfAbsent(ip, k -> {
//            새 IP 등록 시 카운트 증가
            if (userIpCounter.incrementAndGet() > MAX_IP_COUNTER) {
//                제한 초과 시 버킷 생성 거부
                log.warn("New IP limit exceeded: {}", ip);
                userIpCounter.decrementAndGet();
                return null;
            }

            log.info("New bucket create IP: {}", ip);
            return createBucket();
        });

//        버킷이 null이 아니고 토큰 소비가 가능할 경우 true 반환
        boolean consume = bucket != null && bucket.tryConsume(1);

        if (!consume) {
            log.info("Rate Limit exceeded IP: {}", ip);
        }

        return consume;
    }

    //    1분마다 맵과 유저카운터 초기화
    @Scheduled(fixedRate = 60000)
    public void ipBucketCleanup() {
        int size = ipBucket.size();
        ipBucket.clear();
        userIpCounter.set(0);
        if (size > 1) {
            log.info("Clean up {} IP bucket", size);
        }
    }
}
