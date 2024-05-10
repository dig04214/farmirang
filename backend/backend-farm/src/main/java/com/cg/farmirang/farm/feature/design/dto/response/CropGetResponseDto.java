package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropDataDto;
import lombok.*;

import java.util.List;

@Builder @Data
public class CropGetResponseDto {
    private List<CropDataDto> cropList;
    private Integer totalRidgeArea;
    private Integer ridgeWidth;
}
