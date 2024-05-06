package com.cg.farmirang.farm.feature.design.repository.querydsl;

import com.cg.farmirang.farm.feature.design.dto.CropSelectionOrderedByCropDto;
import com.cg.farmirang.farm.feature.design.entity.CropSelection;

import java.util.List;

public interface CropSelectionRepositoryCustom {
    List<CropSelectionOrderedByCropDto> findByCropHeightGreaterThanEqual(Long designId);
    List<CropSelectionOrderedByCropDto> findByCropHeightLesserThan(Long designId);
}
