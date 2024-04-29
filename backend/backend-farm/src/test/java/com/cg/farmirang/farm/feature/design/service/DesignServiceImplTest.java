package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.CoordinateRequestDto;
import com.cg.farmirang.farm.feature.design.entity.*;
import com.cg.farmirang.farm.feature.design.repository.ArrangementRepository;
import com.cg.farmirang.farm.feature.design.repository.DesignRepository;
import com.cg.farmirang.farm.feature.design.repository.FarmCoordinateRepository;
import com.cg.farmirang.farm.feature.design.repository.MemberRepository;
import com.cg.farmirang.farm.global.common.code.ErrorCode;
import com.cg.farmirang.farm.global.exception.BusinessExceptionHandler;
import com.cg.farmirang.farm.global.exception.GlobalExceptionHandler;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
    @Autowired
    FarmCoordinateRepository farmCoordinateRepository;
    @Autowired
    ArrangementRepository arrangementRepository;

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
        assertThat(savedDesign).isEqualTo(design);
    }

    // 좌표 DB 저장 후 빈 밭 배열 생성
    @Test
    public void 밭배열생성(){
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
        Design savedDesign = designRepository.save(design);

        List<CoordinateRequestDto> list=new ArrayList<>();
        list.add(CoordinateRequestDto.builder().x(1).y(4).sequence(1).build());
        list.add(CoordinateRequestDto.builder().x(3).y(6).sequence(2).build());
        list.add(CoordinateRequestDto.builder().x(7).y(8).sequence(3).build());
        list.add(CoordinateRequestDto.builder().x(9).y(5).sequence(4).build());

        int minX=100; int maxX=0; int minY=100; int maxY=0;

        for (CoordinateRequestDto coordinate : list) {
            minX=Math.min(minX,coordinate.getX());
            maxX=Math.max(maxX,coordinate.getX());
            minY=Math.min(minY,coordinate.getY());
            maxY=Math.max(maxY,coordinate.getY());
        }

        int row=maxY-minY;
        int column=maxX-minX;

        int[][] farm=new int[row][column];

        for (CoordinateRequestDto coordinate : list) {
            FarmCoordinate farmCoordinate = FarmCoordinate.builder()
                    .design(savedDesign)
                    .x(coordinate.getX()-minX)
                    .y(coordinate.getY()-minY)
                    .sequence(coordinate.getSequence())
                    .build();
            FarmCoordinate save = farmCoordinateRepository.save(farmCoordinate);
            savedDesign.addFarmCoordinate(save);

        }
        designRepository.save(savedDesign);

        // when
        Arrangement savedArrangement = arrangementRepository.save(Arrangement.builder().arrangement(farm).build());
        Gson gson=new Gson();
        Arrangement arrangement = arrangementRepository.findById(savedArrangement.getId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.ARRANGEMENT_NOT_FOUND));


        // then
        assertEquals(savedArrangement.getId(), arrangement.getId());

    }
}