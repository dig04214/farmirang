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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
     * @param memberId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public EmptyFarmCreateResponseDto insertEmptyFarm(@NotBlank Integer memberId, EmptyFarmCreateRequestDto request) {

        Member member = getMember(memberId);

        // DB에 design 저장
        Design design = Design.builder()
                .member(member)
                .totalArea(request.getArea())
                .startMonth(request.getStartMonth())
                .ridgeWidth(request.getRidgeWidth())
                .furrowWidth(request.getFurrowWidth())
                .isVertical(request.getIsVertical())
                .build();

        Design savedDesign = designRepository.save(design);

        // 배열 생성
        List<FarmCoordinateDto> coordinates = request.getCoordinates();
        Collections.sort(coordinates);

        int minX = 100;
        int maxX = 0;
        int minY = 100;
        int maxY = 0;

        // X, Y 최대 최소 구하기
        for (FarmCoordinateDto coordinate : coordinates) {
            minX = Math.min(minX, coordinate.getRow());
            maxX = Math.max(maxX, coordinate.getRow());
            minY = Math.min(minY, coordinate.getColumn());
            maxY = Math.max(maxY, coordinate.getColumn());
        }

        int row = maxY - minY;
        int column = maxX - minX;

        char[][] farm = new char[row][column];
        Polygon polygon = new Polygon();

        // 좌표값에서 최소값 빼고 좌표 DB에 저장
        for (FarmCoordinateDto coordinate : coordinates) {
            int x = coordinate.getRow() - minX;
            int y = coordinate.getColumn() - minY;

            polygon.addPoint(x, Math.abs(row - y));
            FarmCoordinate farmCoordinate = FarmCoordinate.builder()
                    .design(savedDesign)
                    .column(x)
                    .row(Math.abs(row - y))
                    .sequence(coordinate.getSequence())
                    .build();
            savedDesign.addFarmCoordinate(farmCoordinate);
        }
        designRepository.save(savedDesign);
        // 이랑 배열 생성
        RecommendedDesignInfoDto designInfo = savedDesign.getDesignInfo();

        Integer furrowWidth = designInfo.getFurrowWidth();
        Integer ridgeWidth = designInfo.getRidgeWidth();
        int farmWidthCell = farm[0].length;
        int farmHeightCell = farm.length;
        List<FarmCoordinate> farmCoordinates = savedDesign.getFarmCoordinates();

        farm = checkRidgeAndFurrow(farm, polygon, farmWidthCell, farmHeightCell, ridgeWidth / 10, furrowWidth / 10, designInfo.getIsVertical(), farmCoordinates);

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
    private char[][] checkRidgeAndFurrow(char[][] farm, Polygon polygon, int farmWidthCell, int farmHeightCell, int ridgeLengthCell, int furrowLengthCell, Boolean isVertical, List<FarmCoordinate> farmCoordinates) {

        int R = isVertical ? farmWidthCell : farmHeightCell;
        int C = isVertical ? farmHeightCell : farmWidthCell;
        int limit = isVertical ? farmWidthCell : farmHeightCell;

        int check = 0;
        int currentCount = 0;
        boolean isRidge = true;
        while (check < limit) {
            for (int i = 0; i < R && check < limit; i++) {
                for (int j = 0; j < C; j++) {
                    farm[isVertical ? j : i][isVertical ? i : j] = (isRidge) ? isRidgeOrEmpty(isVertical ? i : j, isVertical ? j : i, polygon, R, C, farmCoordinates) ? 'R' : 'E' : 'F';
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
    private boolean isRidgeOrEmpty(int x, int y, Polygon polygon, int height, int width, List<FarmCoordinate> farmCoordinates) {
        // 네군데 다 확인
        int[] changeX = {0, 1, 1, 0};
        int[] changeY = {0, 0, 1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = x + changeX[i];
            int newY = y + changeY[i];

            if ((0 <= newX && newX <= width) && (0 <= newY && newY < height) && !polygon.contains(newX, newY) && !isPointInPolygon(newX, newY, farmCoordinates)) {
                return false;

            }
        }
        return true;
    }

    /**
     * 꼭지점에 있는지 확인
     */
    private boolean isPointInPolygon(int newX, int newY, List<FarmCoordinate> farmCoordinates) {
        for (FarmCoordinate farmCoordinate : farmCoordinates) {
            if (farmCoordinate.getColumn() == newX && farmCoordinate.getRow() == newY) return true;
        }
        return false;
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

//        List<Object[]> results = cropRepository.findCropInfoAndCropArea(String.valueOf(design.getStartMonth()));
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
    public RecommendedDesignCreateResponseDto insertRecommendedDesign(Long designId, @NotBlank List<RecommendedDesignCreateRequestDto> request) {
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

            design.addCropSelection(cropSelection);
        }

        designRepository.save(design);
        // 두둑에서 알고리즘으로 배치하기
        RecommendedDesignCreateResponseDto response = createDesign(design);

        // 밭 좌표 추가
        List<FarmCoordinateDto> farmCoordinateList = new ArrayList<>();
        for (FarmCoordinate farmCoordinate : design.getFarmCoordinates()) {
            farmCoordinateList.add(FarmCoordinateDto.toDto(farmCoordinate));
        }
        response.setFarmCoordinateList(farmCoordinateList);

        // 몽고디비에 다시 업데이트
        selectedArrangement.setDesignArrangement(response.getDesignArray());
        selectedArrangement.setCropNumberAndCropIdDtoList(response.getCropNumberAndCropIdDtoList());
        arrangementRepository.save(selectedArrangement);

        return response;
    }

    /**
     * 디자인 추천 배치
     */
    private RecommendedDesignCreateResponseDto createDesign(Design design) {
        Long designId = design.getId();
        // TODO : 수확시기를 생각해 비슷한 수확시기의 작물끼리 모으기 => 이건..조금 나중에
        // 작물 길이, 그리고 1m 기준으로 그룹 생성 후 연작 가능한 것, 파종 시기 있는 것, 우선순위 순으로 나열
        List<CropSelectionOrderedByCropDto> cropList = cropSelectionRepository.findByCropHeightGreaterThanEqual100(designId, design.getStartMonth().toString());
        // 1m 미만
        cropList.addAll(cropSelectionRepository.findByCropHeightLesserThan100(designId, design.getStartMonth().toString()));

        char[][] arrangement = getSelectedArrangement(design).getArrangement();

        Boolean isVertical = design.getIsVertical();

        // 밭 가로, 세로
        int farmHeight = arrangement.length;
        int farmWidth = arrangement[0].length;

        int[][] cropArray = new int[farmHeight][farmWidth];
        List<CropNumberAndCropIdDto> cropNumberAndCropIdDtoList = new ArrayList<>();

        // 방향에 맞는 이랑의 가로, 세로
        int number = 1;
        // 작물을 꺼냄
        for (CropSelectionOrderedByCropDto crop : cropList) {
            Integer quantity = crop.getQuantity();
            Integer cropWidth = crop.getCropSpacing() / 10;
            Integer cropHeight = crop.getRidgeSpacing() / 10;

            // 좌표 이동
            int height = isVertical ? farmWidth - cropHeight : farmHeight - cropHeight;
            int width = isVertical ? farmHeight - cropWidth : farmWidth - cropWidth;
            outer:
            for (int i = 0; i <= height; i++) {
                for (int j = 0; j <= width; j++) {
                    // 작물 개수만큼 다 심거나 다 안 줄어도 좌표가 끝으로 갔으면 break
                    if (quantity <= 0) break outer;

                    int h = isVertical ? j : i;
                    int w = isVertical ? i : j;

                    if (canPlantCrop(arrangement, farmWidth, farmHeight, cropArray, cropWidth, cropHeight, w, h, crop, isVertical)) {
                        plantCrop(crop, cropHeight, cropWidth, w, h, cropArray, number, cropNumberAndCropIdDtoList, isVertical);
                        number++;
                        quantity--;
                    }

                }

            }
        }

        return RecommendedDesignCreateResponseDto.builder()
                .designArray(cropArray)
                .cropNumberAndCropIdDtoList(cropNumberAndCropIdDtoList)
                .build();
    }

    /**
     * 작물 배치
     */
    private static void plantCrop(CropSelectionOrderedByCropDto crop, Integer cropHeight, Integer cropWidth, int w, int h, int[][] cropArray, int number, List<CropNumberAndCropIdDto> cropNumberAndCropIdDtoList, Boolean isVertical) {
        cropNumberAndCropIdDtoList.add(
                CropNumberAndCropIdDto.builder()
                        .cropId(crop.getCropId())
                        .number(number)
                        .build()
        );
        for (int addHeight = 0; addHeight < cropHeight; addHeight++) {
            for (int addWidth = 0; addWidth < cropWidth; addWidth++) {
                int newHeight = h + addHeight;
                int newWidth = w + addWidth;
                cropArray[isVertical ? newWidth : newHeight][isVertical ? newHeight : newWidth] = number;
            }
        }
    }

    /**
     * 배치 가능한 지 확인
     */
    private boolean canPlantCrop(char[][] arrangement, int farmWidth, int farmHeight, int[][] cropArray, Integer cropWidth, Integer cropHeight, int w, int h, CropSelectionOrderedByCropDto crop, Boolean isVertical) {
        if (w + cropWidth >= farmWidth || h + cropHeight >= farmHeight) return false;

        for (int addHeight = 0; addHeight < cropHeight; addHeight++) {
            for (int addWidth = 0; addWidth < cropWidth; addWidth++) {
                int newHeight = h + addHeight;
                int newWidth = w + addWidth;

                if (arrangement[isVertical ? newWidth : newHeight][isVertical ? newHeight : newWidth] != 'R' || cropArray[isVertical ? newWidth : newHeight][isVertical ? newHeight : newWidth] != 0)
                    return false;
            }
        }
        return true;
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


        Optional<List<Design>> optionalDesignList = designRepository.findAllByMember(member);

        // 디자인 리스트 있는 경우만 추가
        if (optionalDesignList.isPresent()) {
            List<Design> designList = optionalDesignList.get();
            for (Design design : designList) {
                Arrangement selectedArrangement = getSelectedArrangement(design);
                list.add(
                        DesignListResponseDto.builder()
                                .arrangement(selectedArrangement.getArrangement())
                                .name(design.getName())
                                .savedTime(design.getUpdatedAt().format(DateTimeFormatter.ofPattern("YYYY.MM.dd")))
                                .build()
                );
            }
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

        LocalDateTime savedTime = design.getUpdatedAt();

        return DesignDetailResponseDto.builder()
                .arrangement(selectedArrangement.getArrangement())
                .designArray(selectedArrangement.getDesignArrangement())
                .cropNumberAndCropIdDtoList(selectedArrangement.getCropNumberAndCropIdDtoList())
                .name(design.getName())
                .savedTime(savedTime.format(DateTimeFormatter.ofPattern("YYYY.MM.dd")))
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
        // 이름 수정
        Design design = getDesign(designId);
        design.updateName(request.getName());

        // 배치 수정
        Arrangement selectedArrangement = getSelectedArrangement(design);
        selectedArrangement.setDesignArrangement(request.getDesignArray());
        selectedArrangement.setCropNumberAndCropIdDtoList(request.getCropNumberAndNameList());

        // CropSelection 기존 목록 삭제
        cropSelectionRepository.deleteAllByDesign(design);

        // CropSelection 새로 추가
        List<CropSelection> newCropSelectionList = getNewCropSelectionList(request.getCropIdAndQuantityDtoList(), design);
        // TODO : 디버깅하기
        try {
            designRepository.save(design);
            arrangementRepository.save(selectedArrangement);
            cropSelectionRepository.saveAll(newCropSelectionList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionHandler(ErrorCode.INSERT_ERROR);
        }
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
        selectedArrangement.setCropNumberAndCropIdDtoList(request.getCropNumberAndCropIdDtoList());

        List<CropSelection> newCropSelectionList = getNewCropSelectionList(request.getCropIdAndQuantityDtoList(), design);
        // TODO : 디버깅 하기
        try {
            arrangementRepository.save(selectedArrangement);
            cropSelectionRepository.saveAll(newCropSelectionList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionHandler(ErrorCode.INSERT_ERROR);
        }

    }

    /**
     * CropSelection 추가
     */
    private List<CropSelection> getNewCropSelectionList(List<CropIdAndQuantityDto> list, Design design) {
        List<CropSelection> cropSelections = new ArrayList<>();

        for (CropIdAndQuantityDto cropIdAndQuantityDto : list) {
            Crop crop = cropRepository.findById(cropIdAndQuantityDto.getCropId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.CROP_NOT_FOUND));

            cropSelections.add(CropSelection.builder()
                    .design(design)
                    .crop(crop)
                    .quantity(cropIdAndQuantityDto.getQuantity())
                    .build());
        }
        return cropSelections;
    }

    /**
     * 디자인 삭제
     *
     * @param designId
     * @return
     */
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
            e.printStackTrace();
            throw new BusinessExceptionHandler(ErrorCode.DELETE_ERROR);
        }

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
            e.printStackTrace();
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 대표 디자인 수정
     *
     * @param designId
     * @param memberId
     * @return
     */
    @Override
    @Transactional
    public Boolean updateThumbnailDesign(Long designId, @NotBlank Integer memberId) {
        Member member = getMember(memberId);

        // 선택한 디자인 찾아오기
        Design newThumbnailDesign = designRepository.findByMemberAndId(member, designId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.DESIGN_NOT_FOUND));

        // 기존 대표 디자인 찾기
        Optional<Design> oldThumbnailOptional = getThumbnailDesign(member);

        try {
            if (oldThumbnailOptional.isPresent()) {
                Design oldThumbnailDesign = oldThumbnailOptional.get();
                if (!oldThumbnailDesign.getId().equals(newThumbnailDesign.getId())) {
                    // 대표 이미지가 있는데 다른 걸 누른 경우는 그걸로 대표이미지 변경
                    oldThumbnailDesign.updateIsThumbnail();
                    newThumbnailDesign.updateIsThumbnail();
                } else {
                    // 이미 선택된 대표 이미지를 취소하는 경우
                    oldThumbnailDesign.updateIsThumbnail();
                }
            } else {
                // 대표 이미지가 없는 경우면 바로 대표이미지 설정을 하기
                newThumbnailDesign.updateIsThumbnail();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_DEIGN_THUMBNAIL_ERROR);
        }
    }


    /**
     * 대표 디자인 조회
     *
     * @param memberId
     * @return
     */
    @Override
    public ThumbnailDesignResponseDto selectThumbnailDesign(@NotNull Integer memberId) {
        Optional<Design> thumbnailDesign = getThumbnailDesign(getMember(memberId));
        if (thumbnailDesign.isEmpty()) return null;
        else {
            Design design = thumbnailDesign.get();
            Arrangement selectedArrangement = getSelectedArrangement(design);
            return ThumbnailDesignResponseDto.builder()
                    .designArray(selectedArrangement.getDesignArrangement())
                    .cropNumberAndCropIdDtoList(selectedArrangement.getCropNumberAndCropIdDtoList())
                    .build();
        }
    }

    /**
     * 대표 디자인 Optional 불러오기
     */
    private Optional<Design> getThumbnailDesign(Member member) {
        return designRepository.findByMemberAndIsThumbnailTrue(member);
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
