package com.cg.farmirang.farm.feature.design.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "design_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "design")
    private List<FarmCoordinate> farmCoordinates;

    private String arrangementId;
    private Integer area;
    private String name;
    private Integer startMonth;
    private Integer ridgeWidth;
    private Integer furrowWidth;
    private Boolean isHorizontal;

    @OneToMany(mappedBy = "design")
    private List<CropSelection> cropSelections;

    @Builder
    public Design(Member member, Integer area, Integer startMonth, Integer ridgeWidth, Integer furrowWidth, Boolean isHorizontal) {
        this.member = member;
        this.area = area;
        this.startMonth = startMonth;
        this.ridgeWidth = ridgeWidth;
        this.furrowWidth = furrowWidth;
        this.isHorizontal = isHorizontal;
        this.farmCoordinates=new ArrayList<>();
    }

    public void addFarmCoordinate(FarmCoordinate farmCoordinate){
        this.farmCoordinates.add(farmCoordinate);
    }
    public void setArrangementId(String arrangementId){ this.arrangementId=arrangementId;}
    public Long getDesignId(){ return this.id;}
    public Integer getStartMonth(){ return this.startMonth;}
    public String getArrangementId(){ return this.arrangementId;}

}
