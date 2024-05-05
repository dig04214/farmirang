package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropLengthAndAreaDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class CropForGetResponseDto {
    private Integer cropId;
    private String name;
    private Boolean isRecommended;
    private CropLengthAndAreaDto cropLengthAndAreaDto;
}
