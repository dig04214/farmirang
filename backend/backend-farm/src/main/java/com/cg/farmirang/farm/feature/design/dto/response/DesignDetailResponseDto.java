package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropNumberAndNameDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class DesignDetailResponseDto {
    private char[][] arrangement;
    private int[][] designArray;
    private List<CropNumberAndNameDto> cropNumberAndNameList;
    private String name;
    private List<String> cropList;
    private LocalDateTime savedTime;
}
