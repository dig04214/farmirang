package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "design")
public class FarmCoordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinate_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "design_id")
    private Design design;

    private int x;
    private int y;
    private int sequence;

    @Builder
    public FarmCoordinate(Design design, int x, int y, int sequence) {
        this.design = design;
        this.x = x;
        this.y = y;
        this.sequence = sequence;
    }
}
