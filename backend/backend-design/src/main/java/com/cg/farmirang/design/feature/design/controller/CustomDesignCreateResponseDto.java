package com.cg.farmirang.design.feature.design.controller;

import lombok.Builder;
import lombok.Data;

@Builder @Data
public class CustomDesignCreateResponseDto {
    private Long designId;
    private Boolean[][] farm;
}
