package com.cg.farmirang.farm.feature.design.dto;

import com.cg.farmirang.farm.feature.design.dto.CropLengthAndAreaDto;
import lombok.*;

@Builder @Data
public class CropDataDto {
    private Integer cropId;
    private String name;
    private Boolean isRecommended;
    private CropLengthAndAreaDto cropLengthAndAreaDto;
}
