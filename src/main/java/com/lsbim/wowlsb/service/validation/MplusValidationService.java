package com.lsbim.wowlsb.service.validation;

import com.lsbim.wowlsb.dto.mplus.MplusRankingsDTO;
import com.lsbim.wowlsb.dto.mplus.MplusTimelineDataDTO;
import com.lsbim.wowlsb.service.wcl.RankingsService;
import com.lsbim.wowlsb.util.validation.WowEnumValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
@RequiredArgsConstructor
public class MplusValidationService {

    private final RankingsService rankingsService;

    public boolean validateTimelineParams(String className, String specName, int dungeonId) {
        if (!WowEnumValidator.isValidClassName(className)) {
            log.warn("Invalid class name: {} AND specName: {} dungeonId: {}", className, specName, dungeonId);
            return false;
        }
        if (!WowEnumValidator.isValidSpecName(specName)) {
            log.warn("Invalid spec name: {} AND className: {} dungeonId: {}", specName, className, dungeonId);
            return false;
        }
        if (!WowEnumValidator.isValidDungeonId(dungeonId)) {
            log.warn("Invalid dungeon id: {} AND className: {} specName: {}", dungeonId, className, specName);
            return false;
        }

        return true;
    }

    public boolean isDataExpired(LocalDateTime createdDate) {
        // 기준 시간 createdDate이 비교대상보다 이전인지? .isBefore
        return createdDate.isBefore(LocalDateTime.now().minusDays(3));
    }

//    기존 데이터와 새 랭킹목록의 중복 체크
    public boolean isDuplicateTimelineData(MplusTimelineDataDTO dto, String className, String specName, int dungeonId){
        MplusRankingsDTO newDTO = rankingsService.getMplusRankings(dungeonId, className, specName);

        if(dto.getTimelineData().path("rankings").size() != newDTO.getRankings().size()){
            return false;
        }

        for (int i = 0; i < newDTO.getRankings().size(); i++) {
            long newDuration = newDTO.getRankings().get(i).getDuration();
            long oldDuration = dto.getTimelineData().path("rankings").get(i).path("duration").asLong();

            if(newDuration != oldDuration){
                log.info("need update data... oldDuration: {}, newDuration: {}", oldDuration, newDuration);
                return false;
            }
        }

        return true;
    }
}
