package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor
public class CropNumberAndCropIdDto {
    private Integer cropId;
    private Integer number;

    public CropNumberAndCropIdDto(Integer cropId, Integer number) {
        this.cropId = cropId;
        this.number = number;
    }
}

