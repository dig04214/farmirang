package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "design_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "design")
    private List<Coordinate> coordinates;

    private String arrangementId;
    private Integer area;
    private String name;

    @Enumerated(EnumType.STRING)
    private StartMonth startMonth;

    @Enumerated(EnumType.STRING)
    private Location location;

    private Integer ridgeWidth;
    private Boolean isHorizontal;
    private Integer furrowWidth;

    @OneToMany(mappedBy = "design")
    private List<CropSelection> cropSelections;
}
