package com.cg.farmirang.farm.feature.design.dto.request;

import lombok.*;

@Builder @Data @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class DesignNameUpdateRequestDto {
    private String name;
}
