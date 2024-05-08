package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.dto.CropNumberAndNameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder @Setter @Getter
public class CustomDesignCreateRequestDto {
    private int[][] designArray;
    private List<CropNumberAndNameDto> cropNumberAndNameList;
}
