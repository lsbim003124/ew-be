package com.lsbim.wowlsb.service.queue;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.service.ProcessingService;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class QueueService {

    private final ProcessingService processingService;
    //    공유자원 큐
    private final BlockingQueue<TaskRequest> taskQueue = new LinkedBlockingQueue<>(100);
    //    동시성 안전한 Set. synchronized 없애도 됨
    private final Set<String> taskSet = ConcurrentHashMap.newKeySet();

    @Qualifier("dataUpdateExecutor") // Bean을 명시적으로 주입
    private final Executor dataUpdateExecutor;

    //    큐 작업에만 쓰일 객체
    @Getter
    @AllArgsConstructor
    public static class TaskRequest {
        private String className;
        private String specName;
        private int dungeonId;
        private CompletableFuture<ObjectNode> future; // 비동기로 응답하기 위한 Future
    }

    private String createTaskKey(String className, String specName, int dungeonId) {
        return className + "-" + specName + "-" + dungeonId;
    }

    // 새로운 작업을 큐에 추가 (생산자)**
    public ObjectNode enqueueTask(String className, String specName, int dungeonId) {
        String newKey = createTaskKey(className, specName, dungeonId);
//        중복체크
        if (taskSet.contains(newKey)) {
            log.warn("Duplicate task data: {}", newKey);
            return null;
        }

        CompletableFuture<ObjectNode> future = new CompletableFuture<>();
        TaskRequest task = new TaskRequest(className, specName, dungeonId, future);

        if (!taskQueue.offer(task)) { // 큐에 비동기 작업 추가, taskQueue.offer(task)가 참이면 queue에 task가 들어감
            log.warn("Queue is full, reject this: {}", newKey);
            return null;
        }

        taskSet.add(newKey);
        // 전용 쓰레드 풀로 processQueue를 실행
        dataUpdateExecutor.execute(this::processQueue);

        try {
            return future.get(); // complete(result)로 값이 채워지는 순간 리턴
        } catch (Exception e) {
            taskSet.remove(newKey);
            throw new RuntimeException("작업 처리 중 에러 발생", e);
        }
    }

    // 큐에서 doProcessing 파라미터를 꺼내 작업을 처리 (소비자)**
    private void processQueue() {
        log.info("===============================");
        while (true) {
            TaskRequest task = null; // finally에서 쓰기 위해 먼저 선언

            try {
                log.info("Queue is currently {}", taskQueue);

                task = taskQueue.poll(1, TimeUnit.SECONDS);
                if (task == null) {
                    // 1초 동안 새 작업이 없으면 스레드 종료
                    if (taskQueue.isEmpty()) {
                        log.info("Queue work end.");
                        break;
                    }
                    continue;
                }

                log.info("Queue Poll {}", task);

//                    WCL API로부터 데이터 가져오기(갱신)
                ObjectNode result = processingService.mplusTimelineProcessing(
                        task.getClassName(),
                        task.getSpecName(),
                        task.getDungeonId()
                );

                task.getFuture().complete(result); // 해당 코드 호출하는 순간 enqueueTask 리턴

            } catch (InterruptedException e) {
                log.warn("Worker thread interrupted: ", e);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.warn("Error queue processing task", e);
                if(task != null){
                    task.getFuture().completeExceptionally(e);
                }
            } finally {
                if (task != null) {
                    String completeTaskKey = createTaskKey(
                            task.className,
                            task.specName,
                            task.dungeonId
                    );
                    taskSet.remove(completeTaskKey);
                }
            }
        }
    }

    public boolean isTaskInSet(String className, String specName, int dungeonId) {
        return taskSet.contains(createTaskKey(className, specName, dungeonId));
    }

}
