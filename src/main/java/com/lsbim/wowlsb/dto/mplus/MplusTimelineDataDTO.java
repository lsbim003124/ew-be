package com.lsbim.wowlsb.dto.mplus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class MplusTimelineDataDTO {

    private ObjectNode timelineData;
    private LocalDateTime createdDate;
}
