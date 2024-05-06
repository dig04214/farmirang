package com.cg.farmirang.farm.feature.design.repository.querydsl.impl;

import com.cg.farmirang.farm.feature.design.dto.CropSelectionOrderedByCropDto;
import com.cg.farmirang.farm.feature.design.dto.QCropSelectionOrderedByCropDto;
import com.cg.farmirang.farm.feature.design.entity.CropSelection;
import com.cg.farmirang.farm.feature.design.entity.QCrop;
import com.cg.farmirang.farm.feature.design.entity.QCropSelection;
import com.cg.farmirang.farm.feature.design.repository.querydsl.CropSelectionRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cg.farmirang.farm.feature.design.entity.QCrop.crop;
import static com.cg.farmirang.farm.feature.design.entity.QCropSelection.cropSelection;

public class CropSelectionRepositoryCustomImpl implements CropSelectionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CropSelectionRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<CropSelectionOrderedByCropDto> findByCropHeightGreaterThanEqual(Long designId) {

        return queryFactory
                .select(new QCropSelectionOrderedByCropDto(cropSelection.crop.id.as("cropId"),cropSelection.crop.ridgeSpacing,cropSelection.crop.cropSpacing,cropSelection.crop.sowingTime,cropSelection.crop.harvestingTime,cropSelection.crop.isRepeated,cropSelection.crop.height,cropSelection.priority,cropSelection.quantity))
                .from(cropSelection)
                .join(cropSelection.crop, crop)
                .where(cropSelection.design.id.eq(designId)
                        .and(crop.height.goe(100)))
                .orderBy(crop.isRepeated.desc(), crop.height.desc())
                .fetch();
    }

    @Override
    public List<CropSelectionOrderedByCropDto> findByCropHeightLesserThan(Long designId) {
        return queryFactory
                .select(new QCropSelectionOrderedByCropDto(cropSelection.crop.id.as("cropId"),cropSelection.crop.ridgeSpacing,cropSelection.crop.cropSpacing,cropSelection.crop.sowingTime,cropSelection.crop.harvestingTime,cropSelection.crop.isRepeated,cropSelection.crop.height,cropSelection.priority,cropSelection.quantity))
                .from(cropSelection)
                .join(cropSelection.crop, crop)
                .where(cropSelection.design.id.eq(designId)
                        .and(crop.height.lt(100)))
                .orderBy(crop.isRepeated.desc(), crop.height.desc())
                .fetch();
    }
}
