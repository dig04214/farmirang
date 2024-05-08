package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CropIdAndQuantityDto {
    private Integer cropId;
    private Integer quantity;
}
