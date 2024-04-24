package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.entity.Location;
import com.cg.farmirang.farm.feature.design.entity.StartMonth;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class EmptyFarmCreateRequestDto {
    private List<CoordinateRequestDto> coordinates;
    private Integer area;
    private StartMonth startMonth;
    private Location location;
    private Integer ridgeWidth;
    private Integer furrowWidth;
    private Boolean isHorizontal;
}
