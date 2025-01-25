package com.lsbim.wowlsb.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Log4j2
public class ControllerLogging {

    //    Before는 메소드 호출 전 동작
    @Before("execution(* com.lsbim.wowlsb.controller.MplusController.*(..))")
    public void controllerReqInfo(JoinPoint jp) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // ip
        String ip = request.getRemoteAddr();

        // 메소드명
        String methodName = jp.getSignature().getName();

        // 매개변수
        Object[] args = jp.getArgs();

        log.info("-======- Request -=====-");
        log.info("IP: " + ip);
        log.info("Method Name: " + methodName);
        log.info("Args: " + Arrays.toString(args));
    }
}
