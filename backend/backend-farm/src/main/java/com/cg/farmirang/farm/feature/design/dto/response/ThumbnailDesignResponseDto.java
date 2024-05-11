package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropNumberAndCropIdDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class ThumbnailDesignResponseDto {
    private int[][] designArray;
    private List<CropNumberAndCropIdDto> cropNumberAndCropIdDtoList;
}
