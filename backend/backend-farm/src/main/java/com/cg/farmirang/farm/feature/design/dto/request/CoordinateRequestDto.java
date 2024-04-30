package com.cg.farmirang.farm.feature.design.dto.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class CoordinateRequestDto {
    private Integer x;
    private Integer y;
    // 작물 배치 시 순서는 0 : 왼쪽 위, 1 : 오른쪽 위, 2 : 오른쪽 아래, 3 : 왼쪽 아래
    private Integer sequence;
}
