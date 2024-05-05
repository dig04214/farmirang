package com.cg.farmirang.farm.feature.design.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmptyFarmCreateResponseDto {
    private Long designId;
    private String arrangement;
    private char[][] farm; // TODO : test 확인용이니 완성되면 지울 것
}
