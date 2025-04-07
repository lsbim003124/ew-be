package com.lsbim.wowlsb.dto.mplus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // 매핑되지 않는 JSON 속성 무시
public class MplusResourcesGraphDTO {

    private ArrayNode damageTakenDataList;
    private ArrayNode graphData;


    public static MplusResourcesGraphDTO fromArrayNode(ObjectNode node) {
        MplusResourcesGraphDTO dto = new MplusResourcesGraphDTO();

//        node는 report부터. events, hp로 나뉨.

        dto.setGraphData(
                (ArrayNode) node.path("hp").path("data")
                        .path("series").get(0).path("data"));

        dto.setDamageTakenDataList(
                (ArrayNode) node.path("events").path("data")
        );

        return dto;
    }
}
