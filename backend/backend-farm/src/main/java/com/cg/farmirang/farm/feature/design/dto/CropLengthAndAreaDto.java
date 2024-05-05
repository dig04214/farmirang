package com.cg.farmirang.farm.feature.design.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class CropLengthAndAreaDto {
    private Integer ridgeSpacing;
    private Integer cropSpacing;
    private Integer area;
}
