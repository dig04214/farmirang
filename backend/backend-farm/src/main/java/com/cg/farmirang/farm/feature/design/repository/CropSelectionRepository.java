package com.cg.farmirang.farm.feature.design.repository;

import com.cg.farmirang.farm.feature.design.dto.CropSelectionOrderedByCropDto;
import com.cg.farmirang.farm.feature.design.entity.CropSelection;
import com.cg.farmirang.farm.feature.design.entity.Design;
import com.cg.farmirang.farm.feature.design.repository.querydsl.CropSelectionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CropSelectionRepository extends JpaRepository<CropSelection, Integer>, CropSelectionRepositoryCustom {
    List<CropSelection> findAllByDesign(Design design);

    @Query("SELECT new com.cg.farmirang.farm.feature.design.dto.CropSelectionOrderedByCropDto(cs.crop.id, cs.crop.ridgeSpacing, cs.crop.cropSpacing, cs.crop.sowingTime, cs.crop.harvestingTime, cs.crop.isRepeated, cs.crop.height, cs.priority, cs.quantity) " +
            "FROM CropSelection cs JOIN cs.crop c " +
            "WHERE cs.design.id = :designId AND c.height >= 100 " +
            "ORDER BY c.isRepeated DESC, c.height DESC, " +
            "CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', c.sowingTime, ','))) THEN 0 ELSE 1 END, " +
            "cs.priority ASC")
    List<CropSelectionOrderedByCropDto> findByCropHeightGreaterThanEqual(@Param("designId") Long designId);
}
