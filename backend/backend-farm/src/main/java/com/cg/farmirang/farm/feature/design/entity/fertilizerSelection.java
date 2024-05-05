package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class fertilizerSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fertilizer_selection_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_selection_id")
    private CropSelection cropSelection;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "fertilizer_id")
//    private Fertilizer fertilizer;


}
