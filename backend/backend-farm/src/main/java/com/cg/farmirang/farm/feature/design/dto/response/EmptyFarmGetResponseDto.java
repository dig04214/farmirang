package com.cg.farmirang.farm.feature.design.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder @Getter @Setter
public class EmptyFarmGetResponseDto {
    private char[][] farm;
    private CropGetResponseDto crops;
}
