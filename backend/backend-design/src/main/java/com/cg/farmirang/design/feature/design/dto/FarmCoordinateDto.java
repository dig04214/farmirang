package com.cg.farmirang.design.feature.design.dto;

import com.cg.farmirang.design.feature.design.entity.FarmCoordinate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class FarmCoordinateDto {
    private Integer row;
    private Integer column;
    private Integer sequence;

    public static FarmCoordinateDto toDto(FarmCoordinate farmCoordinate){
        return FarmCoordinateDto.builder()
                .row(farmCoordinate.getRow())
                .column(farmCoordinate.getCol())
                .sequence(farmCoordinate.getSequence())
                .build();
    }

}
