package com.cg.farmirang.farm.feature.design.dto;

import com.cg.farmirang.farm.feature.design.dto.request.CoordinateRequestDto;
import lombok.Data;

import java.util.List;
@Data
public class CropArrangementCoordinateDto {
    private Integer cropId;
    private List<CoordinateRequestDto> coordinations;
}
