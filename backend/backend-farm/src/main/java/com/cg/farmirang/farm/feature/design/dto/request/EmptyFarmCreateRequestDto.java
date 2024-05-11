package com.cg.farmirang.farm.feature.design.dto.request;

import com.cg.farmirang.farm.feature.design.dto.FarmCoordinateDto;
import com.cg.farmirang.farm.feature.design.dto.XYCoordinateDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
@Builder @Data @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class EmptyFarmCreateRequestDto {
    private List<XYCoordinateDto> coordinates;
    @NotNull
    private Integer area;
    @NotNull
    private Integer startMonth;
    @NotNull
    private Integer ridgeWidth;
    @NotNull
    private Integer furrowWidth;
    @NotNull
    private Boolean isVertical;

}
