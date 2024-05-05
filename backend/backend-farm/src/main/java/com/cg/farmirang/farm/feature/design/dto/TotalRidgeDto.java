package com.cg.farmirang.farm.feature.design.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder @Setter @Getter @ToString
public class TotalRidgeDto {
    private RidgeDto ridge;
    private FurrowDto furrow;
}
