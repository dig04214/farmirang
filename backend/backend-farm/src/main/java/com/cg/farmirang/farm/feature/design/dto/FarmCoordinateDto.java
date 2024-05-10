package com.cg.farmirang.farm.feature.design.dto;

import com.cg.farmirang.farm.feature.design.entity.FarmCoordinate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class FarmCoordinateDto implements Comparable<FarmCoordinateDto> {
    private Integer row;
    private Integer column;
    private Integer sequence;

    public static FarmCoordinateDto toDto(FarmCoordinate farmCoordinate){
        return FarmCoordinateDto.builder()
                .row(farmCoordinate.getRow())
                .column(farmCoordinate.getColumn())
                .sequence(farmCoordinate.getSequence())
                .build();
    }

    @Override
    public int compareTo(FarmCoordinateDto o) {
        return this.sequence-o.sequence;
    }
}
