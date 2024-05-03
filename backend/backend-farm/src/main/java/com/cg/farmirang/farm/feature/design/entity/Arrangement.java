package com.cg.farmirang.farm.feature.design.entity;

import com.cg.farmirang.farm.feature.design.dto.TotalRidgeDto;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
@ToString
@Document(collection="design_arrangement")
@Getter @Setter
public class Arrangement {
    @Id
    private String id;
    private TotalRidgeDto[] arrangement;

    @Builder
    public Arrangement(TotalRidgeDto[] arrangement) {
        this.arrangement = arrangement;
    }
}
