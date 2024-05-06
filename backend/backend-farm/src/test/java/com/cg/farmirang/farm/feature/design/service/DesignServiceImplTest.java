package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.CoordinateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.DesignNameUpdateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.EmptyFarmCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.RecommendedDesignCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.response.DesignDetailResponseDto;
import com.cg.farmirang.farm.feature.design.dto.response.DesignListResponseDto;
import com.cg.farmirang.farm.feature.design.dto.response.EmptyFarmCreateResponseDto;
import com.cg.farmirang.farm.feature.design.entity.*;
import com.cg.farmirang.farm.feature.design.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;

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
    @Autowired CropSelectionRepository cropSelectionRepository;

    @Test
    @Rollback(value = false)
    public void 작물DB저장(){
        List<Crop> list=new ArrayList<>();

        list.add(Crop.builder().name("감자").ridgeSpacing(50).cropSpacing(20).companionPlant("15").competetivePlant("4,7,14").sowingTime("3,4").harvestingTime("6").isRepeated(false).height(100).difficulty(Difficulty.EASY).minTemperature(15.0).maxTemperature(25.0).minHumidity(60.0).maxHumidity(80.0).minFieldHumidity(70.0).maxFieldHumidity(90.0).build());
        list.add(Crop.builder().name("고구마").ridgeSpacing(80).cropSpacing(30).companionPlant("15").competetivePlant("").sowingTime("5").harvestingTime("10").isRepeated(true).height(20).difficulty(Difficulty.EASY).minTemperature(15.0).maxTemperature(30.0).minHumidity(60.0).maxHumidity(70.0).minFieldHumidity(60.0).maxFieldHumidity(70.0).build());
        list.add(Crop.builder().name("청양고추").ridgeSpacing(80).cropSpacing(40).companionPlant("").competetivePlant("").sowingTime("5").harvestingTime("7,8,9,10").isRepeated(false).height(100).difficulty(Difficulty.HARD).minTemperature(15.0).maxTemperature(23.0).minHumidity(70.0).maxHumidity(80.0).minFieldHumidity(70.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("당근").ridgeSpacing(60).cropSpacing(20).companionPlant("7,10,12,13").competetivePlant("").sowingTime("8").harvestingTime("11").isRepeated(true).height(30).difficulty(Difficulty.MEDIUM).minTemperature(15.0).maxTemperature(25.0).minHumidity(50.0).maxHumidity(70.0).minFieldHumidity(70.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("딸기").ridgeSpacing(40).cropSpacing(30).companionPlant("12").competetivePlant("").sowingTime("6,7").harvestingTime("16,17").isRepeated(true).height(30).difficulty(Difficulty.HARD).minTemperature(8.0).maxTemperature(23.0).minHumidity(60.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(70.0).build());
        list.add(Crop.builder().name("땅콩").ridgeSpacing(40).cropSpacing(20).companionPlant("").competetivePlant("").sowingTime("5").harvestingTime("10").isRepeated(false).height(30).difficulty(Difficulty.EASY).minTemperature(12.0).maxTemperature(30.0).minHumidity(40.0).maxHumidity(50.0).minFieldHumidity(60.0).maxFieldHumidity(70.0).build());
        list.add(Crop.builder().name("방울토마토").ridgeSpacing(100).cropSpacing(50).companionPlant("6,8,5,12,14").competetivePlant("15,1").sowingTime("3").harvestingTime("6,7,8,9").isRepeated(false).height(150).difficulty(Difficulty.HARD).minTemperature(15.0).maxTemperature(28.0).minHumidity(65.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("부추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,4,7").competetivePlant("").sowingTime("4,5,6,7,8,9").harvestingTime("4,5,6,7,8,9").isRepeated(true).height(30).difficulty(Difficulty.EASY).minTemperature(18.0).maxTemperature(25.0).minHumidity(40.0).maxHumidity(50.0).minFieldHumidity(80.0).maxFieldHumidity(90.0).build());
        list.add(Crop.builder().name("블루베리").ridgeSpacing(180).cropSpacing(150).companionPlant("").competetivePlant("").sowingTime("3").harvestingTime("6").isRepeated(false).height(100).difficulty(Difficulty.EASY).minTemperature(7.0).maxTemperature(25.0).minHumidity(65.0).maxHumidity(75.0).minFieldHumidity(90.0).maxFieldHumidity(100.0).build());
        list.add(Crop.builder().name("상추").ridgeSpacing(20).cropSpacing(20).companionPlant("5,12,4").competetivePlant("8").sowingTime("1,4,8").harvestingTime("2,5,9").isRepeated(false).height(30).difficulty(Difficulty.MEDIUM).minTemperature(15.0).maxTemperature(23.0).minHumidity(60.0).maxHumidity(70.0).minFieldHumidity(65.0).maxFieldHumidity(75.0).build());
        list.add(Crop.builder().name("생강").ridgeSpacing(40).cropSpacing(40).companionPlant("14").competetivePlant("").sowingTime("4").harvestingTime("10").isRepeated(false).height(200).difficulty(Difficulty.EASY).minTemperature(20.0).maxTemperature(30.0).minHumidity(50.0).maxHumidity(60.0).minFieldHumidity(90.0).maxFieldHumidity(95.0).build());
        list.add(Crop.builder().name("양파").ridgeSpacing(20).cropSpacing(10).companionPlant("4,10,5,7").competetivePlant("").sowingTime("11").harvestingTime("17").isRepeated(true).height(50).difficulty(Difficulty.MEDIUM).minTemperature(15.0).maxTemperature(25.0).minHumidity(65.0).maxHumidity(70.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("열무").ridgeSpacing(20).cropSpacing(20).companionPlant("4,10,14").competetivePlant("").sowingTime("4,6,8").harvestingTime("5,7,10,11").isRepeated(false).height(20).difficulty(Difficulty.EASY).minTemperature(15.0).maxTemperature(20.0).minHumidity(60.0).maxHumidity(80.0).minFieldHumidity(30.0).maxFieldHumidity(40.0).build());
        list.add(Crop.builder().name("오이").ridgeSpacing(90).cropSpacing(50).companionPlant("12,10,15,7").competetivePlant("1").sowingTime("4,5").harvestingTime("6,7,8").isRepeated(false).height(400).difficulty(Difficulty.HARD).minTemperature(17.0).maxTemperature(28.0).minHumidity(70.0).maxHumidity(90.0).minFieldHumidity(50.0).maxFieldHumidity(60.0).build());
        list.add(Crop.builder().name("옥수수").ridgeSpacing(90).cropSpacing(50).companionPlant("14,16").competetivePlant("7").sowingTime("5").harvestingTime("7").isRepeated(true).height(200).difficulty(Difficulty.EASY).minTemperature(17.0).maxTemperature(32.0).minHumidity(55.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());
        list.add(Crop.builder().name("참외").ridgeSpacing(180).cropSpacing(100).companionPlant("15").competetivePlant("").sowingTime("5").harvestingTime("6").isRepeated(false).height(30).difficulty(Difficulty.HARD).minTemperature(13.0).maxTemperature(30.0).minHumidity(70.0).maxHumidity(80.0).minFieldHumidity(60.0).maxFieldHumidity(80.0).build());

        for (Crop crop : list) {
            cropRepository.save(crop);
        }
    }

    // db에 design 저장
    @Test
    @Rollback(value = false)
    public void 디자인저장(){
        // given
        Member member = memberRepository.save(Member.builder().nickname("test").build());
//        Design design = Design.builder()
//                .member(member)
//                .totalArea(100)
//                .startMonth(4)
//                .ridgeWidth(10)
//                .furrowWidth(20)
//                .isHorizontal(false)
//                .build();
//
//        // when
//        Design savedDesign = designRepository.save(design);
//
//        // then
//        assertThat(savedDesign).isEqualTo(design);
    }

    // 좌표 DB 저장 후 빈 밭 배열 생성
    @Test
//    @Rollback(value = false)
    public void 밭배열생성(){
        // given
//        Member member = memberRepository.save(Member.builder().nickname("test").build());

        List<CoordinateRequestDto> list=new ArrayList<>();
        list.add(CoordinateRequestDto.builder().x(1).y(0).sequence(1).build());
        list.add(CoordinateRequestDto.builder().x(10).y(0).sequence(2).build());
        list.add(CoordinateRequestDto.builder().x(10).y(10).sequence(3).build());
        list.add(CoordinateRequestDto.builder().x(1).y(10).sequence(4).build());

        EmptyFarmCreateRequestDto request = EmptyFarmCreateRequestDto.builder()
                .coordinates(list)
                .area(10000)
                .isHorizontal(false)
                .ridgeWidth(20)
                .furrowWidth(10)
                .startMonth(4)
                .build();

        // when
        EmptyFarmCreateResponseDto response = designService.insertEmptyFarm(null, request);


        // then
//        assertEquals(100, response.getArrangement().length());
//        for (char[] chars : response.getFarm()) {
//            System.out.println(Arrays.toString(chars));
//
//        }
    }


    @Test
    public void 작물리스트조회(){
//        // given
//        Member member = memberRepository.save(Member.builder().nickname("test").build());
//        Design savedDesign = designRepository.save(
//                Design.builder()
//                .member(member)
//                .area(100)
//                .startMonth(4)
//                .ridgeWidth(10)
//                .furrowWidth(20)
//                .isHorizontal(false)
//                .build());
//
//        // when
//        Design design = designRepository.findById(savedDesign.getId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.DESIGN_NOT_FOUND));
////        String startMonth = Integer.toString(design.getStartMonth());
//        String startMonth = "1";
//        List<Object[]> results = em.createQuery("SELECT t.name, CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN true ELSE false END AS isRecommended, t.ridgeSpacing * t.cropSpacing AS area FROM Crop t ORDER BY CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN 0 ELSE 1 END, t.sowingTime")
//                .setParameter("substring", startMonth)
//                .getResultList();
//
//
//
//        // then
//        for (Object[] result : results) {
//            CropGetResponseDto cropDto = CropGetResponseDto.builder()
//                    .name((String) result[0])
//                    .isRecommended((boolean) result[1])
//                    .cellQuantity((int) (Math.ceil(((Integer) result[2]).intValue()) / 100))
//                    .build();
//            System.out.println("cropDto = " + cropDto);
//        }
    }

    @Test
    public void 디자인생성(){

        List<RecommendedDesignCreateRequestDto> cropDtoList = new ArrayList<>();
        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(1).quantity(5).priority(1).build());
        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(2).quantity(2).priority(2).build());
        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(3).quantity(2).priority(3).build());
        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(4).quantity(2).priority(4).build());
        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(6).quantity(2).priority(5).build());
        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(15).quantity(2).priority(6).build());

        List<Integer> cropIds = new ArrayList<>();

        // 선택작물 DB 저장
        for (RecommendedDesignCreateRequestDto selectedCrop : cropDtoList) {
            cropIds.add(selectedCrop.getCropId());
        }



    }

    @Test
    @Disabled
    public void 디자인생성_옛날버전(){
        /* 밭 불러오기 */
        // given
//        Design design = designRepository.findById(1L).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.DESIGN_NOT_FOUND));
//        List<RecommendedDesignCreateRequestDto> request = new ArrayList<>();
//        TotalRidgeDto[] arrangement = arrangementRepository.findById(design.getArrangementId()).get().getArrangement();
//        RecommendedDesignInfoDto designInfo = design.getDesignInfo();
//
//        /* 이랑 생성 */
//        Integer furrowWidth = designInfo.getFurrowWidth();
//        Integer ridgeWidth = designInfo.getRidgeWidth();
//        int farmWidthCell = arrangement[0].length;
//        int farmHeightCell = arrangement.length;
//        Boolean isHorizontal=designInfo.getIsHorizontal();
//        int totalRidgeLength=furrowWidth+ridgeWidth;
//        int ridgeWidthCell=ridgeWidth/10;
//
//        TotalRidgeDto[] totalRidges;
//
//        // 세로로 자른 밭
//        if (isHorizontal) {
//            totalRidges = new TotalRidgeDto[(farmWidthCell * 10) / totalRidgeLength];
//
//            for (int i = 0; i < totalRidges.length; i++) {
//                totalRidges[i] = TotalRidgeDto.builder()
//                        .ridge(RidgeDto.builder().grid(new int[farmHeightCell][ridgeWidthCell]).build())
//                        .furrow(FurrowDto.builder().width(furrowWidth).height(farmHeightCell * 10).build())
//                        .build();
//            }
//        }
//        // 가로로 자른 밭
//        else {
//            totalRidges = new TotalRidgeDto[(farmHeightCell * 10) / totalRidgeLength];
//
//            for (int i = 0; i < totalRidges.length; i++) {
//                totalRidges[i] = TotalRidgeDto.builder()
//                        .ridge(RidgeDto.builder().grid(new int[ridgeWidthCell][farmWidthCell]).build())
//                        .furrow(FurrowDto.builder().width(farmWidthCell * 10).height(furrowWidth).build())
//                        .build();
//            }
//        }
//
//        /* 작물 배치 */
//        List<RecommendedDesignCreateRequestDto> cropDtoList = new ArrayList<>();
//        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(1).quantity(5).priority(1).build());
//        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(2).quantity(2).priority(2).build());
//        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(3).quantity(2).priority(3).build());
//        cropDtoList.add(RecommendedDesignCreateRequestDto.builder().cropId(4).quantity(2).priority(4).build());
//
//        for (RecommendedDesignCreateRequestDto selectedCrop : cropDtoList) {
//            Crop crop = cropRepository.findById(selectedCrop.getCropId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.CROP_NOT_FOUND));
//            CropSelection cropSelection = CropSelection.builder()
//                    .crop(crop)
//                    .quantity(selectedCrop.getQuantity())
//                    .priority(selectedCrop.getPriority())
//                    .design(design)
//                    .build();
//            CropSelection savedCropSelection = cropSelectionRepository.save(cropSelection);
//            design.addCropSelection(savedCropSelection);
//        }
//
//        // when
//
//        List<Crop> crops = new ArrayList<>();
//        for (CropSelection cropSelection : design.getCropSelections()) {
//            Crop crop = cropSelection.getCrop();
//            crops.add(crop);
//        }
//        Collections.sort(crops,new CropComparator());
//
//        // then
//        for (Crop crop : crops) {
//            System.out.println("crop = " + crop.toString());
//        }
//

    }

    @Test
    public void 폴리곤테스트(){
        Polygon polygon = new Polygon();

        List<CoordinateRequestDto> list=new ArrayList<>();
        list.add(CoordinateRequestDto.builder().x(1).y(0).sequence(1).build());
        list.add(CoordinateRequestDto.builder().x(10).y(0).sequence(2).build());
        list.add(CoordinateRequestDto.builder().x(8).y(7).sequence(3).build());
        list.add(CoordinateRequestDto.builder().x(1).y(7).sequence(4).build());
        int minX=100; int maxX=0; int minY=100; int maxY=0;

        // X, Y 최대 최소 구하기
        for (CoordinateRequestDto coordinate : list) {
            minX=Math.min(minX,coordinate.getX());
            maxX=Math.max(maxX,coordinate.getX());
            minY=Math.min(minY,coordinate.getY());
            maxY=Math.max(maxY,coordinate.getY());
        }

        int row=maxY-minY;
        int column=maxX-minX;

        char[][] farm=new char[row][column];

        // 좌표값에서 최소값 빼고 좌표 DB에 저장
        for (CoordinateRequestDto coordinate : list) {
            int x = coordinate.getX() - minX;
            int y = coordinate.getY() - minY;

//            polygon.addPoint(x,y);
            polygon.addPoint(x,Math.abs(row-y));

        }

        System.out.println("polygon = " + polygon.toString());

    }

    @Test
    public void 이름수정(){
        // given
        Long designId=31L;
        String updatedName = "updated name";
        DesignNameUpdateRequestDto dto = DesignNameUpdateRequestDto.builder().name(updatedName).build();

        // when
        designService.updateDesignName(designId, dto);

        // then
        Design design = designRepository.findById(designId).get();
        assertEquals(updatedName,design.getName());
    }

    @Test
    public void 디자인리스트_불러오기(){
        // given
        Integer memberId=1;

        // when
        List<DesignListResponseDto> response = designService.selectDesignList(memberId);

        // then
        for (DesignListResponseDto dto : response) {
            System.out.println("--------밭과 이름--------");
            for (char[] chars : dto.getArrangement()) {
                System.out.println(chars.toString());
            }
            System.out.println("dto.getName() = " + dto.getName());
            System.out.println("--------------------------");
        }
        assertEquals(6, response.size());
    }

    @Test
    public void 디자인_상세보기(){
        // given
        Long designId=31L;

        // when
        DesignDetailResponseDto response = designService.selectDesign(designId);

        // then
        for (char[] chars : response.getArrangement()) {
            System.out.println(Arrays.toString(chars));
        }
        System.out.println("name = " + response.getName());
    }
}