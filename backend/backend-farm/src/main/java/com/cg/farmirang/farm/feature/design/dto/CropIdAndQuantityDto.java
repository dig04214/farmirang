package com.cg.farmirang.farm.feature.design.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class CropIdAndQuantityDto {
    private Integer cropId;
    private Integer quantity;
}
