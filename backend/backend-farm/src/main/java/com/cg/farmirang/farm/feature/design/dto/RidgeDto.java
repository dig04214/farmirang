package com.cg.farmirang.farm.feature.design.dto;

import lombok.Data;

import java.util.List;
@Data
public class RidgeDto {
    private int[][] grid;
    private List<CropArrangementCoordinateDto> arrangedCrops;
}
