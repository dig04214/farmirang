package com.cg.farmirang.farm.feature.design.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder @Getter @Setter
public class EmptyFarmGetResponseDto {
    private String arrangement;
    List<CropGetResponseDto> crops;
}
