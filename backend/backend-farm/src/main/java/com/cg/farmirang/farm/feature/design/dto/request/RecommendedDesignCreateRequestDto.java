package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.entity.Location;
import com.cg.farmirang.farm.feature.design.entity.StartMonth;
import lombok.Getter;

@Getter
public class RecommendedDesignCreateRequestDto {
    private EmptyFarmCreateRequestDto emptyFarmDto;
    private Integer area;
    private StartMonth startMonth;
    private Location location;
    private Integer furrowWidth;
    private Boolean isHorizontal;


}
