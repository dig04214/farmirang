package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropNumberAndNameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder @Getter @Setter
public class RecommendedDesignCreateResponseDto {
    private int[][] designArray;
    private List<CropNumberAndNameDto> cropNumberAndNameList;
}
