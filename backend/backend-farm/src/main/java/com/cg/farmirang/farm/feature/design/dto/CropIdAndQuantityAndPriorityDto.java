package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CropIdAndQuantityAndPriorityDto {
    private Integer cropId;
    private Integer quantity;
    private Integer priority;
}
