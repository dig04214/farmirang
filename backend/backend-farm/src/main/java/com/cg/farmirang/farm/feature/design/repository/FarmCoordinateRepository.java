package com.cg.farmirang.farm.feature.design.repository;

import com.cg.farmirang.farm.feature.design.entity.FarmCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmCoordinateRepository extends JpaRepository<FarmCoordinate, Long> {
}
