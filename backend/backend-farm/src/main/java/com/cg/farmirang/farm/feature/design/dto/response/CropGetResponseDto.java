package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.CropLengthAndAreaDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder @Setter @Getter @ToString
public class CropGetResponseDto {
    private List<CropForGetResponseDto> cropList;
    private Integer totalRidgeArea;
    private Integer ridgeWidth;
}
