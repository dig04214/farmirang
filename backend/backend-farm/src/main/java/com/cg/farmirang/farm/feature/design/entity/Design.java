package com.cg.farmirang.farm.feature.design.entity;

import com.cg.farmirang.farm.feature.design.dto.RecommendedDesignInfoDto;
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
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "design")
    private List<FarmCoordinate> farmCoordinates;

    @Getter
    @Setter
    private String arrangementId;
    private Integer area;
    private String name;
    @Getter
    private Integer startMonth;
    @Getter
    private Integer ridgeWidth;
    private Integer furrowWidth;
    private Boolean isHorizontal;

    @Getter
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
        this.cropSelections = new ArrayList<>();
    }

    public void addFarmCoordinate(FarmCoordinate farmCoordinate){
        this.farmCoordinates.add(farmCoordinate);
    }


    public RecommendedDesignInfoDto getDesignInfo(){
        return RecommendedDesignInfoDto.builder()
                .ridgeWidth(this.ridgeWidth)
                .furrowWidth(this.furrowWidth)
                .isHorizontal(this.isHorizontal)
                .startMonth(this.startMonth)
                .build();
    }
    public void addCropSelection(CropSelection cropSelection){
        this.cropSelections.add(cropSelection);
    }


}
