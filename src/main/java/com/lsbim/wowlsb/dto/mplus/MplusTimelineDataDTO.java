package com.lsbim.wowlsb.dto.mplus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MplusTimelineDataDTO {

    private String timelineData;
    private LocalDateTime createdDate;
}
