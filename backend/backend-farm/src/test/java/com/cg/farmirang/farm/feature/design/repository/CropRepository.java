package com.cg.farmirang.farm.feature.design.repository;

import com.cg.farmirang.farm.feature.design.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Integer> {
}
