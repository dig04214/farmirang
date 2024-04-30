package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class RecommendedDesignInfoDto {
    private Integer ridgeWidth;
    private Integer furrowWidth;
    private Boolean isHorizontal;
}
