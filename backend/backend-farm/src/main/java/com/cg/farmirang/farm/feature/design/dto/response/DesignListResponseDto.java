package com.cg.farmirang.farm.feature.design.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class DesignListResponseDto {
    private char[][] arrangement;
    private String name;
    private LocalDateTime savedTime;
}
