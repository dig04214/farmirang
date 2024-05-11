package com.cg.farmirang.farm.feature.design.entity;

import com.cg.farmirang.farm.feature.design.dto.CropNumberAndCropIdDto;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@ToString
@Document(collection="design_arrangement")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Arrangement {
    @Id
    private String id;
    private char[][] arrangement;
    private int[][] designArrangement;
    private List<CropNumberAndCropIdDto> cropNumberAndCropIdDtoList;

    @Builder
    public Arrangement(char[][] arrangement) {
        this.arrangement = arrangement;
    }
}
