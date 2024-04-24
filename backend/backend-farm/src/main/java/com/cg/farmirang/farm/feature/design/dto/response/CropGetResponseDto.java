package com.cg.farmirang.farm.feature.design.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Setter @Getter
public class CropGetResponseDto {
    private String name;
    private Boolean isRecommended;
    private Integer cellQuantity;
}
