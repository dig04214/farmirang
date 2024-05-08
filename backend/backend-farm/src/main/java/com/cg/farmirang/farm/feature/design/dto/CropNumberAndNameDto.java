package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CropNumberAndNameDto {
    private Integer number;
    private Integer cropId;

}
