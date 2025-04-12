package com.lsbim.wowlsb.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class ThreadConfig {

//    Queue Service에서 작업을 하나씩 처리하기 위한 싱글스레드
    @Bean(name = "dataUpdateExecutor")
    public Executor dataUpdateExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean(name = "imageUploadExecutor")
    public Executor imageUploadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(30);
        executor.setThreadNamePrefix("ImageUpload-");
        executor.initialize();
        return executor;
    }
}
