package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;

@Entity
public class Coordinate {
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
}
