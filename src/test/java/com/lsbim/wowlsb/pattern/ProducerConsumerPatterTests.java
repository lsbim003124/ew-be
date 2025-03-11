package com.lsbim.wowlsb.pattern;

import com.lsbim.wowlsb.service.ProcessingService;
import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
@Log4j2
public class ProducerConsumerPatterTests {

/*    @Autowired
    private ProcessingService processingService;

    @Data
    @AllArgsConstructor
    public class TimelineTask {
        private String className;    // 처리할 클래스명
        private String specName;     // 특성명
        private int dungeonId;       // 던전 ID
        private CompletableFuture<String> future;  // 비동기 결과를 반환하기 위한 Future
    }

    private final BlockingQueue<TimelineTask> taskQueue = new LinkedBlockingQueue<>();

    public CompletableFuture<String> enqueueTask(String className, String specName, int dungeonId) {
        // 결과를 반환하기 위한 Future 객체 생성
        CompletableFuture<String> future = new CompletableFuture<>();

        // 새로운 작업 생성
        TimelineTask task = new TimelineTask(className, specName, dungeonId, future);

        // 큐에 작업 추가
        taskQueue.offer(task);

        // 작업자 스레드 시작 (필요한 경우)
        startWorkerIfNeeded();

        return future;
    }

    private void processQueue() {
        while (isRunning) {
            try {
                // 큐에서 작업 가져오기 (1초 대기)
                TimelineTask task = taskQueue.poll(1, TimeUnit.SECONDS);

                if (task == null) {
                    // 작업이 없으면 종료 조건 체크
                    synchronized (this) {
                        if (taskQueue.isEmpty()) {
                            isRunning = false;
                            break;
                        }
                    }
                    continue;
                }

                try {
                    // API 호출 제한을 위한 지연
                    Thread.sleep(1000);

                    // 실제 작업 수행
                    String result = processingService.doProcessing(
                            task.getClassName(),
                            task.getSpecName(),
                            task.getDungeonId()
                    ).toString();
                    log.info("Queue poll task {}, {}, {}", task.getClassName(), task.getSpecName(), task.getDungeonId());

                    // 작업 결과 설정
                    task.getFuture().complete(result);

                } catch (Exception e) {
                    // 에러 처리
                    task.getFuture().completeExceptionally(e);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // 작업자 스레드 상태 관리
    private volatile boolean isRunning = false;
    private Thread workerThread;

    private synchronized void startWorkerIfNeeded() {
        if (!isRunning) {
            isRunning = true;
            workerThread = new Thread(this::processQueue);
            workerThread.setDaemon(true);
            workerThread.start();
        }
    }

    // 종료 처리
    @PreDestroy
    public void shutdown() {
        isRunning = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    @Test
    public void pdCsPatternTest1() throws Exception {
        CompletableFuture<String> task1 = enqueueTask("Warrior", "Arms", 12660);
        CompletableFuture<String> task2 = enqueueTask("DeathKnight", "Frost", 12662);
        CompletableFuture<String> task3 = enqueueTask("Rogue", "Subtlety", 12669);

        // 모든 작업의 결과를 기다림
        String result1 = task1.get(10, TimeUnit.SECONDS);
        String result2 = task2.get(10, TimeUnit.SECONDS);
        String result3 = task3.get(10, TimeUnit.SECONDS);

        // 결과 확인
        log.info("Task 1 result: {}", result1);
        log.info("Task 2 result: {}", result2);
        log.info("Task 3 result: {}", result3);
    }*/
}
