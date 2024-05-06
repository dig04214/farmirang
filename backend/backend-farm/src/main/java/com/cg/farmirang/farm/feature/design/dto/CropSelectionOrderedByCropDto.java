package com.cg.farmirang.farm.feature.design.dto;

import com.cg.farmirang.farm.feature.design.entity.Crop;
import com.cg.farmirang.farm.feature.design.entity.QCrop;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class CropSelectionOrderedByCropDto {
    private Integer cropId;
    private Integer ridgeSpacing;
    private Integer cropSpacing;
    private String sowingTime;
    private String harvestingTime;
    private Boolean isRepeated;
    private Integer height;
    private Integer priority;
    private Integer quantity;

    @QueryProjection
    public CropSelectionOrderedByCropDto(Integer cropId, Integer ridgeSpacing, Integer cropSpacing, String sowingTime, String harvestingTime, Boolean isRepeated, Integer height, Integer priority, Integer quantity) {
        this.cropId = cropId;
        this.ridgeSpacing = ridgeSpacing;
        this.cropSpacing = cropSpacing;
        this.sowingTime = sowingTime;
        this.harvestingTime = harvestingTime;
        this.isRepeated = isRepeated;
        this.height = height;
        this.priority = priority;
        this.quantity = quantity;
    }

}
