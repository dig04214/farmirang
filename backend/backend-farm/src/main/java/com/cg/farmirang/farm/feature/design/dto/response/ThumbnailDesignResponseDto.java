package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropCoordinateAndCropIdDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class ThumbnailDesignResponseDto {
    private int[][] designArray;
    private List<CropCoordinateAndCropIdDto> cropCoordinateAndCropIdDtoList;
}
