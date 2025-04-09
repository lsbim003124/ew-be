package com.lsbim.wowlsb.dto.mplus.pamameter;

import com.lsbim.wowlsb.dto.mplus.MplusRankingsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CodeAndFightIdDTO {
    private String code;
    private int fightId;

    public static CodeAndFightIdDTO fromRankings(MplusRankingsDTO.Ranking ranking) {

        String code = ranking.getReport().getCode();
        int fightId = ranking.getReport().getFightID();

        CodeAndFightIdDTO dto = new CodeAndFightIdDTO(code, fightId);

        return dto;
    }
}
