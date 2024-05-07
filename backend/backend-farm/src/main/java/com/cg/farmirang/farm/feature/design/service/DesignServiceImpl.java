package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.*;
import com.cg.farmirang.farm.feature.design.dto.request.*;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import com.cg.farmirang.farm.feature.design.entity.*;
import com.cg.farmirang.farm.feature.design.repository.*;
import com.cg.farmirang.farm.global.common.code.ErrorCode;
import com.cg.farmirang.farm.global.exception.BusinessExceptionHandler;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DesignServiceImpl implements DesignService {

    private final EntityManager em;
    private final DesignRepository designRepository;
    private final MemberRepository memberRepository;
    private final FarmCoordinateRepository farmCoordinateRepository;
    private final ArrangementRepository arrangementRepository;
    private final CropRepository cropRepository;
    private final CropSelectionRepository cropSelectionRepository;

    /**
     * 빈 밭 생성
     *
     * @param token
     * @param request
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public EmptyFarmCreateResponseDto insertEmptyFarm(HttpServletRequest token, EmptyFarmCreateRequestDto request) {

        // TODO : 회원 확인 -> 이후에 통신 예정
        Integer memberId = 1;
        Member member = getMember(memberId);

        // DB에 design 저장
        Design design = Design.builder()
                .member(member)
                .totalArea(request.getArea())
                .startMonth(request.getStartMonth())
                .ridgeWidth(request.getRidgeWidth())
                .furrowWidth(request.getFurrowWidth())
                .isHorizontal(request.getIsHorizontal())
                .build();

        Design savedDesign = designRepository.save(design);

        // 배열 생성
        List<CoordinateRequestDto> coordinates = request.getCoordinates();
        int minX = 100;
        int maxX = 0;
        int minY = 100;
        int maxY = 0;

        // X, Y 최대 최소 구하기
        for (CoordinateRequestDto coordinate : coordinates) {
            minX = Math.min(minX, coordinate.getX());
            maxX = Math.max(maxX, coordinate.getX());
            minY = Math.min(minY, coordinate.getY());
            maxY = Math.max(maxY, coordinate.getY());
        }

        int row = maxY - minY;
        int column = maxX - minX;

        char[][] farm = new char[row][column];
        Polygon polygon = new Polygon();

        // 좌표값에서 최소값 빼고 좌표 DB에 저장
        for (CoordinateRequestDto coordinate : coordinates) {
            int x = coordinate.getX() - minX;
            int y = coordinate.getY() - minY;

            polygon.addPoint(x, Math.abs(row - y));
            FarmCoordinate farmCoordinate = FarmCoordinate.builder()
                    .design(savedDesign)
                    .x(x)
                    .y(y)
                    .sequence(coordinate.getSequence())
                    .build();
            FarmCoordinate save = farmCoordinateRepository.save(farmCoordinate);
            savedDesign.addFarmCoordinate(save);

        }

        // 이랑 배열 생성
        RecommendedDesignInfoDto designInfo = savedDesign.getDesignInfo();

        Integer furrowWidth = designInfo.getFurrowWidth();
        Integer ridgeWidth = designInfo.getRidgeWidth();
        int farmWidthCell = farm[0].length;
        int farmHeightCell = farm.length;

        farm = checkRidgeAndFurrow(farm, polygon, farmWidthCell, farmHeightCell, ridgeWidth / 10, furrowWidth / 10, designInfo.getIsHorizontal());

        // 몽고DB에 배열 저장
        Arrangement arrangement = arrangementRepository.save(Arrangement.builder().arrangement(farm).build());

        // design에 arrangementId과 두둑 넓이 추가
        Gson gson = new Gson();
        String jsonFarm = gson.toJson(arrangement.getArrangement());
        Integer count = (int) jsonFarm.chars().filter(ch -> ch == 'R').count();
        savedDesign.updateArrangementIdAndRidgeArea(arrangement.getId(), count);
        designRepository.save(savedDesign);

        return EmptyFarmCreateResponseDto.builder()
                .designId(savedDesign.getId())
                .farm(farm)
                .build();
    }


    /**
     * 이랑, 고랑, 그리고 빈칸 체크
     */
    private char[][] checkRidgeAndFurrow(char[][] farm, Polygon polygon, int farmWidthCell, int farmHeightCell, int ridgeLengthCell, int furrowLengthCell, Boolean isHorizontal) {

        int R = isHorizontal ? farmWidthCell : farmHeightCell;
        int C = isHorizontal ? farmHeightCell : farmWidthCell;
        int limit = isHorizontal ? farmWidthCell : farmHeightCell;

        int check = 0;
        int currentCount = 0;
        boolean isRidge = true;
        while (check < limit) {
            for (int i = 0; i < R && check < limit; i++) {
                for (int j = 0; j < C; j++) {
                    farm[isHorizontal ? j : i][isHorizontal ? i : j] = (isRidge) ? isRidgeOrEmpty(isHorizontal ? i : j, isHorizontal ? j : i, polygon, R, C) ? 'R' : 'E' : 'F';
                }
                check++;
                currentCount++;

                if (isRidge && currentCount == ridgeLengthCell) {
                    currentCount = 0;
                    isRidge = false;
                } else if (!isRidge && currentCount == furrowLengthCell) {
                    currentCount = 0;
                    isRidge = true;
                }


            }
        }

        return farm;
    }

    /**
     * 도형 안에 있는지 확인
     */
    private boolean isRidgeOrEmpty(int x, int y, Polygon polygon, int height, int width) {
        // 네군데 다 확인
        int[] changeX = {0, 1, 1, 0};
        int[] changeY = {0, 0, 1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = x + changeX[i];
            int newY = y + changeY[i];

            if ((0 <= newX && newX < width) && (0 <= newY && newY < height) && !polygon.contains(newX, newY)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 작물 리스트 조회 - 심는 시기에 맞는 작물 우선 정렬
     *
     * @param designId
     * @return
     */
    @Override
    public CropGetResponseDto selectCropList(Long designId) {
        Design design = getDesign(designId);

        // 시작 달이 추천 파종시기인 작물부터 정렬
        return getCropGetResponseDto(design);
    }

    /**
     * 작물 정보 리스트, 전체 넓이, 이랑 너비 불러오기
     */
    private CropGetResponseDto getCropGetResponseDto(Design design) {
        List<Object[]> results = em.createQuery("SELECT t.id,t.name, " +
                        "CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN true ELSE false END AS isRecommended, " +
                        "t.ridgeSpacing, t.cropSpacing,t.ridgeSpacing * t.cropSpacing AS area " +
                        "FROM Crop t " +
                        "ORDER BY " +
                        "CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN 0 ELSE 1 END, " +
                        "t.sowingTime")
                .setParameter("substring", design.getStartMonth().toString())
                .getResultList();

        // TODO : NoSuchMethodError 뜸...이유를 모르겠네
//        List<Object[]> results = cropRepository.findCropInfoAndCropArea(design.getStartMonth().toString());
        List<CropForGetResponseDto> list = new ArrayList<>();

        for (Object[] result : results) {
            CropForGetResponseDto cropDto = CropForGetResponseDto.builder()
                    .cropId((Integer) result[0])
                    .name((String) result[1])
                    .isRecommended((boolean) result[2])
                    .cropLengthAndAreaDto(CropLengthAndAreaDto.builder()
                            .ridgeSpacing((Integer) result[3])
                            .cropSpacing((Integer) result[4])
                            .area((Integer) result[5])
                            .build()
                    )
                    .build();
            list.add(cropDto);
        }
        // TODO : NoSuchMethodError 뜨는데 어케 해결해보기
//        List<CropForGetResponseDto> list = cropRepository.findCropInfoAndCropArea(design.getStartMonth().toString());

        return CropGetResponseDto.builder()
                .cropList(list)
                .totalRidgeArea(design.getRidgeArea())
                .ridgeWidth(design.getRidgeWidth())
                .build();
    }

    /**
     * 디자인 추천 생성
     *
     * @param designId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RecommendedDesignCreateResponseDto insertRecommendedDesign(Long designId, List<RecommendedDesignCreateRequestDto> request) {
        Design design = getDesign(designId);
        // 밭 불러오기
        Arrangement selectedArrangement = getSelectedArrangement(design);
        List<Integer> cropIds = new ArrayList<>();

        // 선택작물 DB 저장
        for (RecommendedDesignCreateRequestDto selectedCrop : request) {
            cropIds.add(selectedCrop.getCropId());
            Crop crop = cropRepository.findById(selectedCrop.getCropId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.CROP_NOT_FOUND));
            CropSelection cropSelection = CropSelection.builder()
                    .crop(crop)
                    .quantity(selectedCrop.getQuantity())
                    .priority(selectedCrop.getPriority())
                    .design(design)
                    .build();
            CropSelection savedCropSelection = cropSelectionRepository.save(cropSelection);
            design.addCropSelection(savedCropSelection);
        }

        // 두둑에서 알고리즘으로 배치하기
        CropForDesignDto[][] designArray = createDesign(design);


        // 몽고디비에 다시 업데이트
        selectedArrangement.setDesignArrangement(designArray);
        arrangementRepository.save(selectedArrangement);

        return RecommendedDesignCreateResponseDto.builder().designArray(designArray).build();
    }

    /**
     * 디자인 추천 배치
     */
    private CropForDesignDto[][] createDesign(Design design) {
        Long designId = design.getId();
        // TODO : 수확시기를 생각해 비슷한 수확시기의 작물끼리 모으기 => 이건..조금 나중에
        // 작물 길이, 그리고 1m 기준으로 그룹 생성 후 연작 가능한 것, 파종 시기 있는 것, 우선순위 순으로 나열
        List<CropSelectionOrderedByCropDto> cropList = cropSelectionRepository.findByCropHeightGreaterThanEqual100(designId,design.getStartMonth().toString());

        // 1m 미만
        cropList.addAll(cropSelectionRepository.findByCropHeightLesserThan100(designId,design.getStartMonth().toString()));


        char[][] arrangement = getSelectedArrangement(design).getArrangement();

        // 밭 가로, 세로
        int farmHeight = arrangement.length;
        int farmWidth = arrangement[0].length;

        CropForDesignDto[][] cropArray = new CropForDesignDto[farmHeight][farmWidth];

        // 방향에 맞는 이랑의 가로, 세로
        int number=0;
        // 작물을 꺼냄
        for (CropSelectionOrderedByCropDto crop : cropList) {
            Integer quantity = crop.getQuantity();
            Integer cropWidth = crop.getCropSpacing()/10;
            Integer cropHeight = crop.getRidgeSpacing()/10;

            // 좌표 이동
            outer : for (int h = 0; h <= farmHeight - cropHeight; h++) {
                for (int w = 0; w <= farmWidth - cropWidth; w++) {
                    // 작물 개수만큼 다 심거나 다 안 줄어도 좌표가 끝으로 갔으면 break
                    if (quantity<=0) break outer;

                    if(canPlantCrop(arrangement,cropArray,w,h,crop)){
                        plantCrop(crop, cropHeight, cropWidth, h, w, cropArray, number);
                        number++;
                        quantity--;
                    }

                }

            }
        }

        return cropArray;
    }

    private static void plantCrop(CropSelectionOrderedByCropDto crop, Integer cropHeight, Integer cropWidth, int h, int w, CropForDesignDto[][] cropArray, int number) {
        for (int addHeight = 0; addHeight< cropHeight; addHeight++){
            for (int addWidth = 0; addWidth < cropWidth; addWidth++) {
                int newHeight= h +addHeight;
                int newWidth= w +addWidth;
                cropArray[newHeight][newWidth]=CropForDesignDto.builder()
                        .cropId(crop.getCropId())
                        .number(number)
                        .build();
            }
        }
    }

    /**
     * 배치 가능한 지 확인
     */
    private boolean canPlantCrop(char[][] arrangement, CropForDesignDto[][] cropArray, int w, int h, CropSelectionOrderedByCropDto crop) {
        int farmHeight = arrangement.length;
        int farmWidth = arrangement[0].length;
        Integer cropWidth = crop.getCropSpacing()/10;
        Integer cropHeight = crop.getRidgeSpacing()/10;


        if(w+cropWidth>= farmWidth || h+cropHeight>=farmHeight) return false;

        for (int addHeight=0; addHeight<cropHeight; addHeight++){
            for (int addWidth = 0; addWidth < cropWidth; addWidth++) {
                int newHeight=h+addHeight;
                int newWidth=w+addWidth;
                if(arrangement[newHeight][newWidth]!='R' || cropArray[newHeight][newWidth]!=null) return false;
            }
        }
        return true;
    }


    @Override
    @Transactional
    public Boolean insertDesign(DesignUpdateRequestDto request) {
        return null;
    }

    /**
     * 유저 디자인 리스트 불러오기
     *
     * @param memberId
     * @return
     */
    @Override
    public List<DesignListResponseDto> selectDesignList(Integer memberId) {
        Member member = getMember(memberId);
        List<DesignListResponseDto> list = new ArrayList<>();

        for (Design design : member.getDesigns()) {
            Arrangement selectedArrangement = getSelectedArrangement(design);
            list.add(DesignListResponseDto.builder()
                    .arrangement(selectedArrangement.getArrangement())
                    .name(design.getName())
                    .savedTime(design.getUpdatedAt())
                    .build()
            );
        }
        return list;
    }

    /**
     * 디자인 상세보기
     *
     * @param designId
     * @return
     */
    @Override
    public DesignDetailResponseDto selectDesign(Long designId) {
        Design design = getDesign(designId);
        Arrangement selectedArrangement = getSelectedArrangement(design);
        List<String> cropList = new ArrayList<>();

        List<CropSelection> cropSelections = design.getCropSelections();

        for (CropSelection cropSelection : cropSelections) {
            cropList.add(cropSelection.getCrop().getName());
        }

        return DesignDetailResponseDto.builder()
                .arrangement(selectedArrangement.getArrangement())
                .designArray(selectedArrangement.getDesignArrangement())
                .name(design.getName())
                .savedTime(design.getUpdatedAt())
                .cropList(cropList)
                .build();
    }

    /**
     * 디자인 이름, 배치 수정
     * 
     * @param designId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Boolean updateDesign(Long designId, DesignUpdateRequestDto request) {
        Design design = getDesign(designId);
        design.updateName(request.getName());

        Arrangement selectedArrangement = getSelectedArrangement(design);
        selectedArrangement.setDesignArrangement(request.getDesignArray());

        // TODO : CropSelection 수정해야함 => 어떤 작물 넣었는지와 개수 필요


        try {
            designRepository.save(design);
            arrangementRepository.save(selectedArrangement);
            return true;
        } catch (Exception e) {
            throw new BusinessExceptionHandler(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional
    public Boolean deleteDesign(Long designId) {
        Design design = getDesign(designId);
        String arrangementId = design.getArrangementId();

        try {
            designRepository.delete(design);
            arrangementRepository.deleteById(arrangementId);
            return true;
        } catch (Exception e) {
            throw new BusinessExceptionHandler(ErrorCode.DELETE_ERROR);
        }

    }

    @Override
    @Transactional
    public Boolean insertPesticideAndFertilizerSelection(PesticideAndFertilizerCreateDto request) {
        return null;
    }


    /**
     * 밭 조회
     *
     * @param designId
     * @return
     */
    @Override
    public EmptyFarmGetResponseDto selectEmptyFarm(Long designId) {
        Design design = getDesign(designId);

        Arrangement selectedArrangement = getSelectedArrangement(design);
        return EmptyFarmGetResponseDto.builder()
                .farm(selectedArrangement.getArrangement())
                .crops(getCropGetResponseDto(design))
                .build();
    }

    /**
     * 커스텀 디자인 추가
     * 
     * @param designId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Boolean insertCustomDesign(Long designId, CustomDesignCreateRequestDto request) {
        Design design = getDesign(designId);
        Arrangement selectedArrangement = getSelectedArrangement(design);
        selectedArrangement.setDesignArrangement(request.getDesignArray());

        // TODO : CropSelection 수정해야함 => 어떤 작물 넣었는지와 개수 필요

        try {
            arrangementRepository.save(selectedArrangement);
            return true;
        } catch (Exception e) {
            throw new BusinessExceptionHandler(ErrorCode.INSERT_ERROR);
        }

    }

    /**
     * 디자인 이름 수정
     *
     * @param designId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Boolean updateDesignName(Long designId, DesignNameUpdateRequestDto request) {
        Design design = getDesign(designId);
        design.updateName(request.getName());
        try {
            designRepository.save(design);
            return true;
        } catch (Exception e) {
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public ChemicalGetResponseDto selectChemical(Long designId) {
        return null;
    }

    /**
     * 유저 불러오기
     */
    private Member getMember(Integer memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 배치 불러오기
     */
    private Arrangement getSelectedArrangement(Design design) {
        return arrangementRepository.findById(design.getArrangementId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.ARRANGEMENT_NOT_FOUND));
    }

    /**
     * 디자인 불러오기
     */
    private Design getDesign(Long designId) {
        return designRepository.findById(designId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.DESIGN_NOT_FOUND));
    }
}
