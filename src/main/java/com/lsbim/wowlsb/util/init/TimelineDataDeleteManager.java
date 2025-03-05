package com.lsbim.wowlsb.util.init;

import com.lsbim.wowlsb.service.repository.MplusTimelineDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class TimelineDataDeleteManager {

    private final MplusTimelineDataService mplusTimelineDataService;

    // 0초, 0분, 2시, * 매일, * 매월, ? 요일 무관
    @Scheduled(cron = "0 0 4 * * ?")
    public void initDeleteTimelineData() {
        try {
            int delCount = mplusTimelineDataService.deleteTimelineData();

            log.info("Delete Timeline Data. Count: {}", delCount);
        } catch (Exception e) {
            log.warn("Failed Delete Timeline Data... {}", e);
        }
    }
}
