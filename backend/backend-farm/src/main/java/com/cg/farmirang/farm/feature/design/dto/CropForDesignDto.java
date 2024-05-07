package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CropForDesignDto {
    private Integer cropId;
    private Integer number;

    public CropForDesignDto(Integer cropId, Integer number) {
        this.cropId = cropId;
        this.number = number;
    }
}
