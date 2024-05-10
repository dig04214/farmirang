package com.cg.farmirang.farm.feature.design.dto;

import com.cg.farmirang.farm.feature.design.entity.Design;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data @Builder
public class DesignForListDto {
    private char[][] arrangement;
    private String name;
    private String savedTime;

    public static DesignForListDto toDto(Design design, char[][] arrangement) {
        return DesignForListDto.builder()
                .arrangement(arrangement)
                .name(design.getName())
                .savedTime(design.getUpdatedAt().format(DateTimeFormatter.ofPattern("YYYY.MM.dd")))
                .build();
    }
}
