package com.cg.farmirang.design.feature.design.dto.response;

import com.cg.farmirang.design.feature.design.dto.CropDataDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder @Data
public class EmptyFarmGetResponseDto {
    private Boolean[][] farm;
    private List<CropDataDto> cropList;
    private Integer totalRidgeArea;
    private Integer ridgeWidth;
}
