package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crop_id")
    private Integer id;

    private String name;
    private Integer ridgeSpacing;
    private Integer cropSpacing;
    private String companionPlant;
    private String competetivePlant;
    private String sowingTime;
    private String harvestingTime;
    private Boolean isRepeated;
    private Integer height;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
}
