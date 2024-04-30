package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.CoordinateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.RecommendedDesignCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.response.CropGetResponseDto;
import com.cg.farmirang.farm.feature.design.entity.*;
import com.cg.farmirang.farm.feature.design.repository.*;
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
    @Autowired
    CropRepository cropRepository;

    @Test
    @Rollback(value = false)
    public void 작물DB저장(){
        List<Crop> list=new ArrayList<>();

        list.add(Crop.builder().name("감자").ridgeSpacing(50).cropSpacing(20).companionPlant("15").competetivePlant("4,7,14").sowingTime("3,4").harvestingTime("6").isRepeated(false).height(100).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("고구마").ridgeSpacing(80).cropSpacing(30).companionPlant("15").competetivePlant("").sowingTime("5").harvestingTime("10").isRepeated(true).height(20).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("청양고추").ridgeSpacing(80).cropSpacing(40).companionPlant("").competetivePlant("").sowingTime("5").harvestingTime("7,8,9,10").isRepeated(false).height(100).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("당근").ridgeSpacing(60).cropSpacing(20).companionPlant("7,10,12,13").competetivePlant("").sowingTime("8").harvestingTime("11").isRepeated(true).height(30).difficulty(Difficulty.MEDIUM).build());
        list.add(Crop.builder().name("딸기").ridgeSpacing(40).cropSpacing(30).companionPlant("12").competetivePlant("").sowingTime("6,7").harvestingTime("4,5").isRepeated(true).height(30).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("땅콩").ridgeSpacing(40).cropSpacing(20).companionPlant("").competetivePlant("").sowingTime("5").harvestingTime("10").isRepeated(false).height(30).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("방울토마토").ridgeSpacing(100).cropSpacing(50).companionPlant("6,8,5,12,14").competetivePlant("15,1").sowingTime("3").harvestingTime("6,7,8,9").isRepeated(false).height(150).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("부추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,4,7").competetivePlant("").sowingTime("4,5,6,7,8,9").harvestingTime("4,5,6,7,8,9").isRepeated(true).height(30).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("블루베리").ridgeSpacing(180).cropSpacing(150).companionPlant("").competetivePlant("").sowingTime("3").harvestingTime("6").isRepeated(false).height(100).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("상추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,12,4").competetivePlant("8").sowingTime("1,4,8").harvestingTime("2,5,9").isRepeated(false).height(30).difficulty(Difficulty.MEDIUM).build());
        list.add(Crop.builder().name("생강").ridgeSpacing(40).cropSpacing(40).companionPlant("14").competetivePlant("").sowingTime("4").harvestingTime("10").isRepeated(false).height(200).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("양파").ridgeSpacing(20).cropSpacing(10).companionPlant("4,10,5,7").competetivePlant("").sowingTime("11").harvestingTime("5").isRepeated(true).height(50).difficulty(Difficulty.MEDIUM).build());
        list.add(Crop.builder().name("열무").ridgeSpacing(20).cropSpacing(20).companionPlant("4,10,14").competetivePlant("").sowingTime("4,6,8").harvestingTime("5,7,10,11").isRepeated(false).height(20).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("오이").ridgeSpacing(90).cropSpacing(50).companionPlant("12,10,15,7").competetivePlant("1").sowingTime("4,5").harvestingTime("6,7,8").isRepeated(false).height(400).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("옥수수").ridgeSpacing(90).cropSpacing(50).companionPlant("14,16").competetivePlant("7").sowingTime("5").harvestingTime("7").isRepeated(true).height(200).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("참외").ridgeSpacing(180).cropSpacing(100).companionPlant("15").competetivePlant("").sowingTime("5").harvestingTime("6").isRepeated(false).height(30).difficulty(Difficulty.HARD).build());

        for (Crop crop : list) {
            cropRepository.save(crop);
        }
    }

    // db에 design 저장
    @Test
//    @Rollback(value = false)
    public void 디자인저장(){
        // given
        Member member = memberRepository.save(Member.builder().nickname("test").build());
        Design design = Design.builder()
                .member(member)
                .area(100)
                .startMonth(4)
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
                .startMonth(4)
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


    @Test
    public void 작물리스트조회(){
        // given
        List<Crop> list=new ArrayList<>();

        list.add(Crop.builder().name("감자").ridgeSpacing(50).cropSpacing(20).companionPlant("15").competetivePlant("4,7,14").sowingTime("3,4").harvestingTime("6").isRepeated(false).height(100).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("고구마").ridgeSpacing(80).cropSpacing(30).companionPlant("15").competetivePlant("").sowingTime("5").harvestingTime("10").isRepeated(true).height(20).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("청양고추").ridgeSpacing(80).cropSpacing(40).companionPlant("").competetivePlant("").sowingTime("5").harvestingTime("7,8,9,10").isRepeated(false).height(100).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("당근").ridgeSpacing(60).cropSpacing(20).companionPlant("7,10,12,13").competetivePlant("").sowingTime("8").harvestingTime("11").isRepeated(true).height(30).difficulty(Difficulty.MEDIUM).build());
        list.add(Crop.builder().name("딸기").ridgeSpacing(40).cropSpacing(30).companionPlant("12").competetivePlant("").sowingTime("6,7").harvestingTime("4,5").isRepeated(true).height(30).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("땅콩").ridgeSpacing(40).cropSpacing(20).companionPlant("").competetivePlant("").sowingTime("5").harvestingTime("10").isRepeated(false).height(30).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("방울토마토").ridgeSpacing(100).cropSpacing(50).companionPlant("6,8,5,12,14").competetivePlant("15,1").sowingTime("3").harvestingTime("6,7,8,9").isRepeated(false).height(150).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("부추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,4,7").competetivePlant("").sowingTime("4,5,6,7,8,9").harvestingTime("4,5,6,7,8,9").isRepeated(true).height(30).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("블루베리").ridgeSpacing(180).cropSpacing(150).companionPlant("").competetivePlant("").sowingTime("3").harvestingTime("6").isRepeated(false).height(100).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("상추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,12,4").competetivePlant("8").sowingTime("1,4,8").harvestingTime("2,5,9").isRepeated(false).height(30).difficulty(Difficulty.MEDIUM).build());
        list.add(Crop.builder().name("생강").ridgeSpacing(40).cropSpacing(40).companionPlant("14").competetivePlant("").sowingTime("4").harvestingTime("10").isRepeated(false).height(200).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("양파").ridgeSpacing(20).cropSpacing(10).companionPlant("4,10,5,7").competetivePlant("").sowingTime("11").harvestingTime("5").isRepeated(true).height(50).difficulty(Difficulty.MEDIUM).build());
        list.add(Crop.builder().name("열무").ridgeSpacing(20).cropSpacing(20).companionPlant("4,10,14").competetivePlant("").sowingTime("4,6,8").harvestingTime("5,7,10,11").isRepeated(false).height(20).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("오이").ridgeSpacing(90).cropSpacing(50).companionPlant("12,10,15,7").competetivePlant("1").sowingTime("4,5").harvestingTime("6,7,8").isRepeated(false).height(400).difficulty(Difficulty.HARD).build());
        list.add(Crop.builder().name("옥수수").ridgeSpacing(90).cropSpacing(50).companionPlant("14,16").competetivePlant("7").sowingTime("5").harvestingTime("7").isRepeated(true).height(200).difficulty(Difficulty.EASY).build());
        list.add(Crop.builder().name("참외").ridgeSpacing(180).cropSpacing(100).companionPlant("15").competetivePlant("").sowingTime("5").harvestingTime("6").isRepeated(false).height(30).difficulty(Difficulty.HARD).build());

        for (Crop crop : list) {
            cropRepository.save(crop);
        }

        Member member = memberRepository.save(Member.builder().nickname("test").build());
        Design savedDesign = designRepository.save(
                Design.builder()
                .member(member)
                .area(100)
                .startMonth(4)
                .ridgeWidth(10)
                .furrowWidth(20)
                .isHorizontal(false)
                .build());

        // when
        Design design = designRepository.findById(savedDesign.getDesignId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.DESIGN_NOT_FOUND));
//        String startMonth = Integer.toString(design.getStartMonth());
        String startMonth = "1";
        List<Object[]> results = em.createQuery("SELECT t.name, CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN true ELSE false END AS isRecommended, t.ridgeSpacing * t.cropSpacing AS area FROM Crop t ORDER BY CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN 0 ELSE 1 END, t.sowingTime")
                .setParameter("substring", startMonth)
                .getResultList();



        // then
        for (Object[] result : results) {
            CropGetResponseDto cropDto = CropGetResponseDto.builder()
                    .name((String) result[0])
                    .isRecommended((boolean) result[1])
                    .cellQuantity((int) (Math.ceil(((Integer) result[2]).intValue()) / 100))
                    .build();
            System.out.println("cropDto = " + cropDto);
        }
    }

    @Test
    public void 디자인추천생성(){
        /* 밭 불러오기 */
        // given
        Design design = designRepository.findById(1L).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.DESIGN_NOT_FOUND));
        List<RecommendedDesignCreateRequestDto> request = new ArrayList<>();



        // when


        // then

    }
}