package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.dto.CropCoordinateAndCropIdDto;
import com.cg.farmirang.farm.feature.design.dto.CropIdAndQuantityDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class DesignUpdateRequestDto {
    private String name;
    private int[][] designArray;
    private List<CropCoordinateAndCropIdDto> cropNumberAndNameList;
    private List<CropIdAndQuantityDto> cropIdAndQuantityDtoList;
}
