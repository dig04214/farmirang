package com.cg.farmirang.farm.feature.design.dto;

import lombok.*;

import java.util.List;
@Builder
@Setter
@Getter
@ToString
public class RidgeDto {
    private int[][] grid;
    private List<CropArrangementCoordinateDto> arrangedCrops;
}
