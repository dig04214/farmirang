package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor
public class CropCoordinateAndCropIdDto {
    private Integer row;
    private Integer column;
    private Integer cropId;

    public CropCoordinateAndCropIdDto(Integer row, Integer column, Integer cropId) {
        this.row = row;
        this.column = column;
        this.cropId = cropId;
    }
}

