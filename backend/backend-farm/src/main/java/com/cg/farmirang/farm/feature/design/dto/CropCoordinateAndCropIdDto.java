package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CropCoordinateAndCropIdDto {
    private Integer row;
    private Integer column;
    private Integer cropId;
}
