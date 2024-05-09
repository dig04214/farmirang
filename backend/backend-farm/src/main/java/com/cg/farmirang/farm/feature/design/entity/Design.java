package com.cg.farmirang.farm.feature.design.entity;

import com.cg.farmirang.farm.feature.design.dto.RecommendedDesignInfoDto;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "design_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "design", orphanRemoval = true)
    private List<FarmCoordinate> farmCoordinates;

    private String arrangementId;
    private Integer totalArea;
    private Integer ridgeArea;

    @Column(columnDefinition = "varchar(255) default '제목 없음'")
    private String name;

    private Integer startMonth;
    private Integer ridgeWidth;
    private Integer furrowWidth;
    private Boolean isHorizontal;
    private Boolean isThumbnail;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Getter
    @OneToMany(mappedBy = "design", orphanRemoval = true)
    private List<CropSelection> cropSelections;

    @Builder
    public Design(Member member, String arrangementId, Integer totalArea, Integer ridgeArea, String name, Integer startMonth, Integer ridgeWidth, Integer furrowWidth, Boolean isHorizontal, Boolean isThumbnail) {
        this.member = member;
        this.arrangementId = arrangementId;
        this.totalArea = totalArea;
        this.ridgeArea = ridgeArea;
        this.name = name;
        this.startMonth = startMonth;
        this.ridgeWidth = ridgeWidth;
        this.furrowWidth = furrowWidth;
        this.isHorizontal = isHorizontal;
        this.isThumbnail = isThumbnail;
        this.farmCoordinates=new ArrayList<>();
        this.cropSelections = new ArrayList<>();
    }

    public void addFarmCoordinate(FarmCoordinate farmCoordinate){
        this.farmCoordinates.add(farmCoordinate);
    }
    public void addCropSelection(CropSelection cropSelection){
        this.cropSelections.add(cropSelection);
    }
    public RecommendedDesignInfoDto getDesignInfo(){
        return RecommendedDesignInfoDto.builder()
                .ridgeWidth(this.ridgeWidth)
                .furrowWidth(this.furrowWidth)
                .isHorizontal(this.isHorizontal)
                .startMonth(this.startMonth)
                .build();
    }
    public void updateArrangementIdAndRidgeArea(String arrangementId, Integer ridgeArea){
        this.arrangementId=arrangementId;
        this.ridgeArea=ridgeArea;
    }

    public void updateName(String name) {
        this.name=name;
    }
    public void updateIsThumbnail(){this.isThumbnail=!isThumbnail;}
}
