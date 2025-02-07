package com.lsbim.wowlsb.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lsbim.wowlsb.enums.utils.ApiStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDTO {

    private ApiStatus status;
    private ObjectNode data;
}
