package com.cg.farmirang.farm.feature.design.dto.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class CoordinateRequestDto {
    private Integer x;
    private Integer y;
    private Integer sequence;
}
