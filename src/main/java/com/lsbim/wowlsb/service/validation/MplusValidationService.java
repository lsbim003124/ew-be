package com.lsbim.wowlsb.service.validation;

import com.lsbim.wowlsb.util.validation.WowEnumValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MplusValidationService {


    public boolean validateTimelineParams(String className, String specName, int dungeonId) {
        if (!WowEnumValidator.isValidClassName(className)) {
            log.warn("Invalid class name: {}", className);
            return false;
        }
        if (!WowEnumValidator.isValidSpecName(specName)) {
            log.warn("Invalid spec name: {}", specName);
            return false;
        }
        if (!WowEnumValidator.isValidDungeonId(dungeonId)) {
            log.warn("Invalid dungeon id: {}", dungeonId);
            return false;
        }

        return true;
    }
}
