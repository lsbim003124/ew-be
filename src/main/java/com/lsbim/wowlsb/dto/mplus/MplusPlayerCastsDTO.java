package com.lsbim.wowlsb.dto.mplus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MplusPlayerCastsDTO {

    private long timestamp;
    private String type;
    private int abilityGameID;
    private String skillName;

}
