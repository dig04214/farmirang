package com.cg.farmirang.farm.feature.design.dto.response;

import com.cg.farmirang.farm.feature.design.dto.DesignForListDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder @Data
public class DesignListResponseDto {
    private List<DesignForListDto> designList;
}
