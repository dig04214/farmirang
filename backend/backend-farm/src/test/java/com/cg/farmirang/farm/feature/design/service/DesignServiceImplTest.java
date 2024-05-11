package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.*;
import com.cg.farmirang.farm.feature.design.dto.request.DesignNameUpdateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.EmptyFarmCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.RecommendedDesignCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import com.cg.farmirang.farm.feature.design.entity.*;
import com.cg.farmirang.farm.feature.design.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DesignServiceImplTest {

    @Autowired
    EntityManager em;
    @Autowired
    DesignService designService;
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
    @Autowired
    CropSelectionRepository cropSelectionRepository;

    private Long designId;

    @Test
    @Rollback(value = false)
    public void 작물DB저장() {
        List<Crop> list = new ArrayList<>();

        list.add(Crop.builder().name("감자").ridgeSpacing(50).cropSpacing(20).companionPlant("15").competitivePlant("4,7,14").sowingTime("3,4").harvestingTime("6").isRepeated(false).height(100).minTemperature(15.0).maxTemperature(25.0).minHumidity(60.0).maxHumidity(80.0).minFieldHumidity(70.0).maxFieldHumidity(90.0).build());
        list.add(Crop.builder().name("고구마").ridgeSpacing(80).cropSpacing(30).companionPlant("15").competitivePlant("").sowingTime("5").harvestingTime("10").isRepeated(true).height(20).minTemperature(15.0).maxTemperature(30.0).minHumidity(60.0).maxHumidity(70.0).minFieldHumidity(60.0).maxFieldHumidity(70.0).build());
        list.add(Crop.builder().name("청양고추").ridgeSpacing(80).cropSpacing(40).companionPlant("").competitivePlant("").sowingTime("5").harvestingTime("7,8,9,10").isRepeated(false).height(100).minTemperature(15.0).maxTemperature(23.0).minHumidity(70.0).maxHumidity(80.0).minFieldHumidity(70.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("당근").ridgeSpacing(60).cropSpacing(20).companionPlant("7,10,12,13").competitivePlant("").sowingTime("8").harvestingTime("11").isRepeated(true).height(30).minTemperature(15.0).maxTemperature(25.0).minHumidity(50.0).maxHumidity(70.0).minFieldHumidity(70.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("딸기").ridgeSpacing(40).cropSpacing(30).companionPlant("12").competitivePlant("").sowingTime("6,7").harvestingTime("16,17").isRepeated(true).height(30).minTemperature(8.0).maxTemperature(23.0).minHumidity(60.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(70.0).build());
        list.add(Crop.builder().name("땅콩").ridgeSpacing(40).cropSpacing(20).companionPlant("").competitivePlant("").sowingTime("5").harvestingTime("10").isRepeated(false).height(30).minTemperature(12.0).maxTemperature(30.0).minHumidity(40.0).maxHumidity(50.0).minFieldHumidity(60.0).maxFieldHumidity(70.0).build());
        list.add(Crop.builder().name("방울토마토").ridgeSpacing(100).cropSpacing(50).companionPlant("6,8,5,12,14").competitivePlant("15,1").sowingTime("3").harvestingTime("6,7,8,9").isRepeated(false).height(150).minTemperature(15.0).maxTemperature(28.0).minHumidity(65.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("부추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,4,7").competitivePlant("").sowingTime("4,5,6,7,8,9").harvestingTime("4,5,6,7,8,9").isRepeated(true).height(30).minTemperature(18.0).maxTemperature(25.0).minHumidity(40.0).maxHumidity(50.0).minFieldHumidity(80.0).maxFieldHumidity(90.0).build());
        list.add(Crop.builder().name("블루베리").ridgeSpacing(180).cropSpacing(150).companionPlant("").competitivePlant("").sowingTime("3").harvestingTime("6").isRepeated(false).height(100).minTemperature(7.0).maxTemperature(25.0).minHumidity(65.0).maxHumidity(75.0).minFieldHumidity(90.0).maxFieldHumidity(100.0).build());
        list.add(Crop.builder().name("상추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,12,4").competitivePlant("8").sowingTime("1,4,8").harvestingTime("2,5,9").isRepeated(false).height(30).minTemperature(15.0).maxTemperature(23.0).minHumidity(60.0).maxHumidity(70.0).minFieldHumidity(65.0).maxFieldHumidity(75.0).build());
        list.add(Crop.builder().name("생강").ridgeSpacing(40).cropSpacing(40).companionPlant("14").competitivePlant("").sowingTime("4").harvestingTime("10").isRepeated(false).height(200).minTemperature(20.0).maxTemperature(30.0).minHumidity(50.0).maxHumidity(60.0).minFieldHumidity(90.0).maxFieldHumidity(95.0).build());
        list.add(Crop.builder().name("양파").ridgeSpacing(20).cropSpacing(10).companionPlant("4,10,5,7").competitivePlant("").sowingTime("11").harvestingTime("17").isRepeated(true).height(50).minTemperature(15.0).maxTemperature(25.0).minHumidity(65.0).maxHumidity(70.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("열무").ridgeSpacing(20).cropSpacing(20).companionPlant("4,10,14").competitivePlant("").sowingTime("4,6,8").harvestingTime("5,7,10,11").isRepeated(false).height(20).minTemperature(15.0).maxTemperature(20.0).minHumidity(60.0).maxHumidity(80.0).minFieldHumidity(30.0).maxFieldHumidity(40.0).build());
        list.add(Crop.builder().name("오이").ridgeSpacing(90).cropSpacing(50).companionPlant("12,10,15,7").competitivePlant("1").sowingTime("4,5").harvestingTime("6,7,8").isRepeated(false).height(400).minTemperature(17.0).maxTemperature(28.0).minHumidity(70.0).maxHumidity(90.0).minFieldHumidity(50.0).maxFieldHumidity(60.0).build());
        list.add(Crop.builder().name("옥수수").ridgeSpacing(90).cropSpacing(50).companionPlant("14,16").competitivePlant("7").sowingTime("5").harvestingTime("7").isRepeated(true).height(200).minTemperature(17.0).maxTemperature(32.0).minHumidity(55.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("참외").ridgeSpacing(180).cropSpacing(100).companionPlant("15").competitivePlant("").sowingTime("5").harvestingTime("6").isRepeated(false).height(30).minTemperature(13.0).maxTemperature(30.0).minHumidity(70.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());

        for (Crop crop : list) {
            cropRepository.save(crop);
        }
    }


    @Test
    @DisplayName("가로 밭 생성")
//    @BeforeEach
//    @Rollback(value = false)
    public void insert_emptyFarm_horizontal() {
        // given

        Integer memberId=10;

        List<XYCoordinateDto> list = new ArrayList<>();
        list.add(XYCoordinateDto.builder().x(1).y(0).sequence(0).build());
        list.add(XYCoordinateDto.builder().x(10).y(0).sequence(1).build());
        list.add(XYCoordinateDto.builder().x(9).y(9).sequence(2).build());
        list.add(XYCoordinateDto.builder().x(1).y(10).sequence(3).build());

        EmptyFarmCreateRequestDto request = EmptyFarmCreateRequestDto.builder()
                .coordinates(list)
                .area(2500)
                .isVertical(false)
                .ridgeWidth(40)
                .furrowWidth(10)
                .startMonth(5)
                .build();

        // when
        EmptyFarmCreateResponseDto response = designService.insertEmptyFarm(memberId, request);

        this.designId = response.getDesignId();

        // then
        assertNotNull(response.getFarm());
        for (char[] chars : response.getFarm()) {
            System.out.println(Arrays.toString(chars));

        }
    }

    @Test
    @DisplayName("세로 밭 생성")
//    @BeforeEach
    @Rollback(value = false)
    public void insert_emptyFarm_vertical() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);

        List<XYCoordinateDto> list = new ArrayList<>();
        list.add(XYCoordinateDto.builder().x(1).y(0).sequence(0).build());
        list.add(XYCoordinateDto.builder().x(10).y(0).sequence(1).build());
        list.add(XYCoordinateDto.builder().x(9).y(9).sequence(2).build());
        list.add(XYCoordinateDto.builder().x(1).y(10).sequence(3).build());

        EmptyFarmCreateRequestDto request = EmptyFarmCreateRequestDto.builder()
                .coordinates(list)
                .area(10000)
                .isVertical(true)
                .ridgeWidth(40)
                .furrowWidth(10)
                .startMonth(5)
                .build();

        // when
        EmptyFarmCreateResponseDto response = designService.insertEmptyFarm(savedMember.getId(), request);

        this.designId = response.getDesignId();

        // then
        assertNotNull(response.getFarm());
        for (char[] chars : response.getFarm()) {
            System.out.println(Arrays.toString(chars));

        }
    }



    @Test
    @DisplayName("작물 리스트 조회")
    public void select_cropList() {
        // given
        Integer memberId=1;
        // when
        CropGetResponseDto response = designService.selectCropList(memberId, designId);

        // then
        assertEquals(16, response.getCropList().size());
    }

    @Test
    @DisplayName("추천 디자인 생성-성공")
    public void insert_recommended_design_success() {

        // given
        List<CropIdAndQuantityAndPriorityDto> cropList = new ArrayList<>();
        cropList.add(CropIdAndQuantityAndPriorityDto.builder().cropId(10).quantity(5).priority(1).build());
        cropList.add(CropIdAndQuantityAndPriorityDto.builder().cropId(5).quantity(5).priority(1).build());
        cropList.add(CropIdAndQuantityAndPriorityDto.builder().cropId(6).quantity(5).priority(1).build());
        cropList.add(CropIdAndQuantityAndPriorityDto.builder().cropId(13).quantity(5).priority(1).build());

        RecommendedDesignCreateRequestDto request = RecommendedDesignCreateRequestDto.builder().cropList(cropList).build();
        Integer memberId=1;
        designId = 2L;
        Design design = designRepository.findById(designId).get();

        // when
        RecommendedDesignCreateResponseDto response = designService.insertRecommendedDesign(memberId, designId, request);

        // then
        int[][] designArray = response.getDesignArray();
        for (int[] ints : designArray) {
            System.out.println(Arrays.toString(ints));
        }
        System.out.println("============================");
        for (CropNumberAndCropIdDto dto : response.getCropNumberAndCropIdDtoList()) {
            System.out.println(dto.toString());
        }
        System.out.println("============================");
        for (FarmCoordinateDto farmCoordinateDto : response.getFarmCoordinateList()) {
            System.out.printf("row : %d, col : %d\n", farmCoordinateDto.getRow(),farmCoordinateDto.getColumn());
        }

    }

    @Test
    @DisplayName("추천 디자인 생성-실패")
    public void insert_recommended_design_fail() {
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            // given
            designId = 1L;
            Integer memberId=1;
            // when
            RecommendedDesignCreateResponseDto response = designService.insertRecommendedDesign(memberId, designId, null);


        });
        // then
        assertEquals("해당 디자인을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("커스텀용 밭 조회")
    public void select_emptyFarm(){
        // given
        Integer memberId=1;
        // when
        EmptyFarmGetResponseDto response = designService.selectEmptyFarm(memberId, designId);
        char[][] farm = response.getFarm();
        List<CropDataDto> cropList = response.getCropList();
        Integer ridgeWidth = response.getRidgeWidth();
        Integer totalRidgeArea = response.getTotalRidgeArea();

        // then
        assertNotNull(farm);
        for (char[] chars : farm) {
            System.out.println(Arrays.toString(chars));
        }
        System.out.println("=======================");
        assertEquals(16, cropList.size());
        for (CropDataDto cropDataDto : cropList) {
            System.out.println(cropDataDto.toString());
        }
        System.out.println("=======================");
        System.out.println("totalRidgeArea = " + totalRidgeArea);
        System.out.println("ridgeWidth = " + ridgeWidth);


    }

    @Test
    @DisplayName("디자인 리스트 조회")
    public void select_designList(){
        // given
        Integer memberId=10;

        // when
        DesignListResponseDto response = designService.selectDesignList(memberId);
        List<Design> designList = designRepository.findAllByMemberIdOrderByModifiedAtDesc(memberId).get();

        // then
        assertEquals(designList.size(),response.getDesignList().size());
    }



    @Test
    public void 이름수정() {
        // given
        Long designId = 31L;
        Integer memberId=10;
        String updatedName = "updated name";
        DesignNameUpdateRequestDto dto = DesignNameUpdateRequestDto.builder().name(updatedName).build();

        // when
        designService.updateDesignName(memberId, designId, dto);

        // then
        Design design = designRepository.findById(designId).get();
        assertEquals(updatedName, design.getName());
    }


    @Test
    public void 디자인_상세보기() {
        // given
        Long designId = 10L;
        Integer memberId=10;

        // when
        DesignDetailResponseDto response = designService.selectDesign(memberId, designId);

        // then
        assertEquals("2024.05.08", response.getSavedTime());
    }

    @Test
    @DisplayName("대표 디자인 수정")
    void 대표디자인_수정() {
        // given
//        Integer memberId=1;
//        Design design = designRepository.findById(10L).get();
//        Optional<List<Design>> designList = designRepository.findAllByMember(memberRepository.findById(memberId).get());
//        int count=0;
//        Design oldDesign = designRepository.findById(2L).get();
//
//        // when
//        designService.updateThumbnailDesign(10L);
//        for (Design design1 : designList) {
//            if (design1.getIsThumbnail()==true) count++;
//        }
//
//
//        // then
//        assertEquals(true,design.getIsThumbnail());
//        assertEquals(false,oldDesign.getIsThumbnail());
//        assertEquals(count,1);

    }

    @Test
    @DisplayName("대표 디자인 조회")
    void 대표디자인_조회() {
        // given
        int memberId = 1;

        // when
        ThumbnailDesignResponseDto reponse = designService.selectThumbnailDesign(memberId);

        // then
        for (int[] ints : reponse.getDesignArray()) {
            System.out.println(Arrays.toString(ints));
        }

        for (CropNumberAndCropIdDto dto : reponse.getCropNumberAndCropIdDtoList()) {
        }

    }

    @Test
    public void 폴리곤테스트() {
        Polygon polygon = new Polygon();

        List<FarmCoordinateDto> list = new ArrayList<>();
        list.add(FarmCoordinateDto.builder().row(1).column(0).sequence(1).build());
        list.add(FarmCoordinateDto.builder().row(10).column(0).sequence(2).build());
        list.add(FarmCoordinateDto.builder().row(8).column(7).sequence(3).build());
        list.add(FarmCoordinateDto.builder().row(1).column(7).sequence(4).build());
        int minX = 100;
        int maxX = 0;
        int minY = 100;
        int maxY = 0;

        // X, Y 최대 최소 구하기
        for (FarmCoordinateDto coordinate : list) {
            minX = Math.min(minX, coordinate.getRow());
            maxX = Math.max(maxX, coordinate.getRow());
            minY = Math.min(minY, coordinate.getColumn());
            maxY = Math.max(maxY, coordinate.getColumn());
        }

        int row = maxY - minY;
        int column = maxX - minX;

        char[][] farm = new char[row][column];

        // 좌표값에서 최소값 빼고 좌표 DB에 저장
        for (FarmCoordinateDto coordinate : list) {
            int x = coordinate.getRow() - minX;
            int y = coordinate.getColumn() - minY;

//            polygon.addPoint(x,y);
            polygon.addPoint(x, Math.abs(row - y));

        }

        System.out.println("polygon = " + polygon.toString());

    }

    @Test
    @Rollback(value = false)
    @DisplayName("회원 생성")
    public void create_member(){
        Member member = Member.builder().build();
        memberRepository.save(member);
    }
}