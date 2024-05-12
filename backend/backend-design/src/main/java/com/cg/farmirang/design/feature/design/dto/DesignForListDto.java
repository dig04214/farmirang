package com.cg.farmirang.design.feature.design.dto;

import com.cg.farmirang.design.feature.design.entity.Arrangement;
import com.cg.farmirang.design.feature.design.entity.Design;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data @Builder
public class DesignForListDto {
    private Long designId;
    private int[][] designArray;
    private List<CropNumberAndCropIdDto> cropNumberAndCropIdDtoList;
    private String name;
    private String savedTime;
    private Boolean isThumbnail;

    public static DesignForListDto toDto(Design design, Arrangement arrangement) {
        return DesignForListDto.builder()
                .designId(design.getId())
                .designArray(arrangement.getDesignArrangement())
                .cropNumberAndCropIdDtoList(arrangement.getCropNumberAndCropIdDtoList())
                .name(design.getName())
                .savedTime(design.getModifiedAt().format(DateTimeFormatter.ofPattern("YYYY.MM.dd")))
                .isThumbnail(design.getIsThumbnail())
                .build();
    }
}
