package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CropSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crop_selection_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "design_id")
    private Design design;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id")
    private Crop crop;

    @OneToMany(mappedBy = "cropSelection")
    private List<CropCoordination> cropCoordinations;

    private Integer priority;
    private Integer quantity;

    @Builder
    public CropSelection(Design design, Crop crop, List<CropCoordination> cropCoordinations, Integer priority, Integer quantity) {
        this.design = design;
        this.crop = crop;
        this.cropCoordinations = cropCoordinations;
        this.priority = priority;
        this.quantity = quantity;
    }
}
