package com.lsbim.wowlsb.common;

import com.lsbim.wowlsb.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootTest
@Log4j2
@EnableScheduling
public class Schedultests {

//    @Autowired
//    private TaskService taskService;
//
//    @Test
//    public void schedulTest1() {
////        스케쥴 메소드는 굳이 실행하지 않아도 자동으로 전부 수행하는듯?
////        taskService.testSchedul();
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
