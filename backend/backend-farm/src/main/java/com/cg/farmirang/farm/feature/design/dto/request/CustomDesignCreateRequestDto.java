package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.dto.CropNumberAndCropIdDto;
import com.cg.farmirang.farm.feature.design.dto.CropIdAndQuantityDto;
import lombok.*;

import java.util.List;

@Builder @Data @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class CustomDesignCreateRequestDto {
    private int[][] designArray;
    private List<CropNumberAndCropIdDto> cropNumberAndCropIdDtoList;
    private List<CropIdAndQuantityDto> cropIdAndQuantityDtoList;
}
