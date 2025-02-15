package com.lsbim.wowlsb.util.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Log4j2
@Component
public class IpValidator {

    public String validateIp(HttpServletRequest request){
        String ip = getIpByRequest(request);

        if(!isValidIp(ip)){
            log.warn("Invalid IP: {}", ip);  // 유효하지 않은 IP 감지시 경고 로그
            throw new IllegalArgumentException("Invalid IP format: " + ip);  // 예외 발생
        }

        // localhost IP 처리
        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();  // 실제 로컬 IP 가져오기
                if (!isValidIp(ip)) {
                    log.error("Invalid localhost IP: {}", ip);  // 로컬호스트 IP가 유효하지 않을 경우 에러 로그
                    throw new IllegalArgumentException("Invalid localhost IP: " + ip);
                }
            } catch (UnknownHostException e) {
                log.error("Localhost resolution failed", e);  // 로컬호스트 확인 실패시 에러 로그
                throw new RuntimeException("Failed to resolve localhost", e);
            }
        }

        return ip;
    }

    private String getIpByRequest(HttpServletRequest request) {
        // 프록시 관련 헤더들을 순서대로 체크
        String[] headers = {
                "X-Forwarded-For",      // 프록시나 로드 밸런서가 설정하는 원본 클라이언트 IP
                "Proxy-Client-IP",      // 프록시 서버가 설정하는 클라이언트 IP
                "WL-Proxy-Client-IP",   // WebLogic 서버의 프록시 클라이언트 IP
                "HTTP_CLIENT_IP",       // 클라이언트 IP
                "HTTP_X_FORWARDED_FOR", // 포워딩된 IP
                "X-Real-IP",           // Nginx 프록시의 실제 클라이언트 IP
                "X-RealIP",            // 실제 IP의 다른 표기
                "REMOTE_ADDR"          // 직접 연결된 클라이언트의 IP
        };

        // 각 헤더를 순회하며 유효한 IP 확인
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For의 경우 첫 번째 IP만 사용 (프록시 체인의 시작점)
                if ("X-Forwarded-For".equals(header)) {
                    return ip.split(",")[0].trim();
                }
                return ip;
            }
        }
        // 모든 헤더가 없는 경우 기본 remote address 반환
        return request.getRemoteAddr();
    }


    // IPv4 또는 IPv6 중 하나라도 유효하면 true 반환
    private boolean isValidIp(String ip) {
        return isValidIPv4(ip) || isValidIPv6(ip);
    }

    private boolean isValidIPv4(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        // 각 숫자가 0~255 사이의 값을 가지는지 검증하는 정규표현식
        String ipv4Pattern = "^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)"
                + "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}$";

        return ip.matches(ipv4Pattern);
    }

    private boolean isValidIPv6(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        // 8개의 1~4자리 16진수 블록을 콜론(:)으로 구분하는 패턴
        String ipv6Pattern = "^(?:[\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$";

        return ip.matches(ipv6Pattern);
    }
}
