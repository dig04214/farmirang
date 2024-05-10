package com.cg.farmirang.farm.feature.design.dto;

import lombok.*;

@Builder @Data @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class XYCoordinateDto implements Comparable<XYCoordinateDto>{
    private Integer x;
    private Integer y;
    private Integer sequence;

    @Override
    public int compareTo(XYCoordinateDto o) {
        return this.sequence-this.sequence;
    }
}
