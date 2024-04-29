package com.cg.farmirang.farm.feature.design.repository;

import com.cg.farmirang.farm.feature.design.entity.Arrangement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArrangementRepository extends MongoRepository<Arrangement, String> {

}
