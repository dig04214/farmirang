package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.dto.CropIdAndQuantityAndPriorityDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data @Builder
public class RecommendedDesignCreateRequestDto {
    private List<CropIdAndQuantityAndPriorityDto> cropList;
}
