package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.dto.CropForDesignDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Setter @Getter
public class CustomDesignCreateRequestDto {
    private CropForDesignDto[][] designArray;
}
