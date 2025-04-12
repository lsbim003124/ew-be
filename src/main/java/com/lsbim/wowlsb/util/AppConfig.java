package com.lsbim.wowlsb.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;

// 스프링 레벨의 싱글톤 (Bean에 할당)
@Configuration
@Log4j2
public class AppConfig {
    
  /*
  Bean 관리 클래스
  */

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate getRestTemplate() {
/*        // ReadTimeout
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(Timeout.ofSeconds(5)).build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create() // 커넥션 풀 사용
                        .setDefaultSocketConfig(socketConfig)
                        .setMaxConnTotal(50) // 최대 동시 연결 수
                        .setMaxConnPerRoute(10) // 특정 호스트(경로) 당 연결 수
                        .build()).build();


        // HttpClient를 사용하는 RequestFactory 생성
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        ConnectTimeout
        factory.setConnectTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);*/

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new LoggingRequestInterceptor()));

        return restTemplate;
    }

    // WCL API에게 요청할때 쓰이는 인터셉터. 안쓰면 응답이 오지 않는다..
    public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(
                HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            ClientHttpResponse response = execution.execute(request, body); // 응답 처리
            return response;
        }
    }
}
