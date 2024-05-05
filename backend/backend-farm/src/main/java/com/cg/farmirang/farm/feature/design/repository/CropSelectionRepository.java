package com.cg.farmirang.farm.feature.design.repository;

import com.cg.farmirang.farm.feature.design.entity.CropSelection;
import com.cg.farmirang.farm.feature.design.entity.Design;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropSelectionRepository extends JpaRepository<CropSelection, Integer> {
    List<CropSelection> findAllByDesign(Design design);
}
