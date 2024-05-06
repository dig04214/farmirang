package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropForDesignDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class RecommendedDesignCreateResponseDto {
    private CropForDesignDto[][] designArray;
}
