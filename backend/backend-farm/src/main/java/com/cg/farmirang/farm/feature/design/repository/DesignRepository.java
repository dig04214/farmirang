package com.cg.farmirang.farm.feature.design.repository;

import com.cg.farmirang.farm.feature.design.entity.Design;
import com.cg.farmirang.farm.feature.design.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesignRepository  extends JpaRepository<Design, Long> {
    Optional<Design> findByMemberIdAndIsThumbnailTrue(Integer memberId);

    Optional<Design> findByMemberIdAndId(Integer memberId, Long designId);

    Optional<List<Design>> findAllByMemberId(Integer memberId);
}
