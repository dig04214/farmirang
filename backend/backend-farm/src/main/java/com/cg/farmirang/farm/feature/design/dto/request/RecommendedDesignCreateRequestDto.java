package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.entity.Crop;
import com.cg.farmirang.farm.feature.design.entity.Location;
import com.cg.farmirang.farm.feature.design.entity.StartMonth;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class RecommendedDesignCreateRequestDto {
    private Integer cropId;
    private Integer quantity;
}
