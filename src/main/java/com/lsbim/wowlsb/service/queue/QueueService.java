package com.lsbim.wowlsb.service.queue;

import com.lsbim.wowlsb.service.ProcessingService;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class QueueService {

    private final ProcessingService processingService;
    //    공유자원 큐
    private final BlockingQueue<TaskRequest> taskQueue = new LinkedBlockingQueue<>(20);
    private final Set<TaskKey> taskSet = new HashSet<>();
    //    스레드 상태
    private volatile boolean isRunning = false;
    //        단일 스레드
    private Thread workerThread;

    //    큐 작업에만 쓰일 객체
    @Getter
    @AllArgsConstructor
    public static class TaskRequest {
        private String className;
        private String specName;
        private int dungeonId;
        private CompletableFuture<String> future; // 비동기로 응답하기 위한 Future
    }

    // 중복 파라미터 판별용 객체
    @Getter
    @AllArgsConstructor
    public static class TaskKey {
        private String className;
        private String specName;
        private int dungeonId;
    }

    // 새로운 작업을 큐에 추가 (생산자)**
    public String enqueueTask(String className, String specName, int dungeonId) {
        TaskKey newKey = new TaskKey(className, specName, dungeonId);
//        중복체크
        if (taskSet.contains(newKey)) {
            log.warn("Duplicate task data: {}", newKey);
            return null;
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        TaskRequest task = new TaskRequest(className, specName, dungeonId, future);

        if (!taskQueue.offer(task)) { // 큐에 비동기 작업 추가, taskQueue.offer(task)가 참이면 queue에 task가 들어감
            log.warn("Queue is full, reject this: {}", newKey);
            return null;
        }
        taskSet.add(newKey);
        startWorker();

        try {
            return future.get(); // complete(result)로 값이 채워지는 순간 리턴
        } catch (Exception e) {
            taskSet.remove(newKey);
            throw new RuntimeException("작업 처리 중 에러 발생", e);
        }
    }

    // 작업자 스레드가 필요한 경우 시작
    private synchronized void startWorker() {
        if (!isRunning) {
            isRunning = true;
            workerThread = new Thread(this::processQueue);
            workerThread.setDaemon(true);
            workerThread.start();
        }
    }

    // 큐에서 doProcessing 파라미터를 꺼내 작업을 처리 (소비자)**
    private void processQueue() {
        log.info("===============================");
        while (isRunning) {
            try {
                TaskRequest task = taskQueue.poll(1, TimeUnit.SECONDS);
                if (task == null) {
                    // 1초 동안 새 작업이 없으면 스레드 종료
                    synchronized (this) {
                        if (taskQueue.isEmpty()) {
                            isRunning = false;
                            break;
                        }
                    }
                    continue;
                }
                log.info("Queue is currently {}", taskQueue);
                log.info("Queue Poll {}", task);
                try {
                    Thread.sleep(1000); // 1초 지연 (필요에 따라 조정)

//                    WCL API로부터 데이터 가져오기
                    String result = processingService.doProcessing(
                            task.getClassName(),
                            task.getSpecName(),
                            task.getDungeonId()
                    ).toString();

                    task.getFuture().complete(result); // 해당 코드 호출하는 순간 enqueueTask 리턴
                } catch (Exception e) {
                    log.error("Error processing task: ", e);
                    task.getFuture().completeExceptionally(e); // 에러를 future에 전달?
                } finally {
//                    작업 종료 시 해당 파라미터 Set에서 제거
                    TaskKey completeTaskKey = new TaskKey(
                            task.className,
                            task.specName,
                            task.dungeonId
                    );
                    taskSet.remove(completeTaskKey);
                }
            } catch (InterruptedException e) {
                log.error("Worker thread interrupted: ", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // 어플리케이션 서비스 종료 시 정리
    @PreDestroy
    public void shutdown() {
        isRunning = false;
        if (workerThread.isAlive()) {
            workerThread.interrupt();
        }
    }
}
