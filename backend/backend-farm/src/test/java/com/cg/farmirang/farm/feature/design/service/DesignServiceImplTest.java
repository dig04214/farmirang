package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.entity.Design;
import com.cg.farmirang.farm.feature.design.entity.Location;
import com.cg.farmirang.farm.feature.design.entity.Member;
import com.cg.farmirang.farm.feature.design.entity.StartMonth;
import com.cg.farmirang.farm.feature.design.repository.DesignRepository;
import com.cg.farmirang.farm.feature.design.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DesignServiceImplTest {

    @Autowired
    EntityManager em;
    @Autowired DesignService designService;
    @Autowired
    DesignRepository designRepository;
    @Autowired
    MemberRepository memberRepository;

    /*빈 밭 생성*/

    // db에 design 저장
    @Test
//    @Rollback(value = false)
    public void 디자인저장(){
        // given
        Member member = memberRepository.save(Member.builder().nickname("test").build());
        Design design = Design.builder()
                .member(member)
                .area(100)
                .startMonth(StartMonth.APRIL)
                .location(Location.CHUNGCHEONG)
                .ridgeWidth(10)
                .furrowWidth(20)
                .isHorizontal(false)
                .build();

        // when
        Design savedDesign = designRepository.save(design);

        // then
        Assertions.assertThat(savedDesign).isEqualTo(design);
    }
}