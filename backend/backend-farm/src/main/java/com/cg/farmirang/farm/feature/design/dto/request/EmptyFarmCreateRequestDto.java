package com.cg.farmirang.farm.feature.design.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class EmptyFarmCreateRequestDto {
    private List<CoordinateRequestDto> coordinates;
    @NotNull
    private Integer area;
    @NotNull
    private Integer startMonth;
    @NotNull
    private Integer ridgeWidth;
    @NotNull
    private Integer furrowWidth;
    @NotNull
    private Boolean isHorizontal;
}
