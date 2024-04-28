package com.cg.farmirang.farm.feature.design.repository;

import com.cg.farmirang.farm.feature.design.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}
