package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class pesticideSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pesticide_selection_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_selection_id")
    private CropSelection cropSelection;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "pesticide_id")
//    private Pesticide pesticide;


}
