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
        String startMonth = Integer.toString(design.getStartMonth());

        // 시작 달이 추천 파종시기인 작물부터 정렬
        return getCropGetResponseDto(design);
    }

    /**
     * 작물 정보 리스트, 전체 넓이, 이랑 너비 불러오기
     */
    private CropGetResponseDto getCropGetResponseDto(Design design) {
        List<Object[]> results = em.createQuery("SELECT t.id,t.name, CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN true ELSE false END AS isRecommended, t.ridgeSpacing, t.cropSpacing,t.ridgeSpacing * t.cropSpacing AS area FROM Crop t ORDER BY CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN 0 ELSE 1 END, t.sowingTime")
                .setParameter("substring", design.getStartMonth().toString())
                .getResultList();

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
        // 작물 길이, 그리고 1m 기준으로 그룹 생성 후 연작 가능한 것, 파종 시기 있는 것, 우선순위 순으로 나열
        // TODO : 수확시기를 생각해 비슷한 수확시기의 작물끼리 모으기 => 이건..조금 나중에
        String jpql = "SELECT new com.cg.farmirang.farm.feature.design.dto.CropSelectionOrderedByCropDto(" +
                "cs.crop.id, cs.crop.ridgeSpacing, cs.crop.cropSpacing, " +
                "cs.crop.sowingTime, cs.crop.harvestingTime, cs.crop.isRepeated, " +
                "cs.crop.height, cs.priority, cs.quantity) " +
                "FROM CropSelection cs JOIN cs.crop c " +
                "WHERE cs.design.id = :designId AND c.height >= 100 " +
                "ORDER BY c.isRepeated DESC, c.height DESC, cs.priority ASC";

        List<CropSelectionOrderedByCropDto> cropList = em.createQuery(jpql, CropSelectionOrderedByCropDto.class)
                .setParameter("designId", designId)
                .getResultList();

        // 1m 미만
        jpql = "SELECT new com.cg.farmirang.farm.feature.design.dto.CropSelectionOrderedByCropDto(" +
                "cs.crop.id, cs.crop.ridgeSpacing, cs.crop.cropSpacing, " +
                "cs.crop.sowingTime, cs.crop.harvestingTime, cs.crop.isRepeated, " +
                "cs.crop.height, cs.priority, cs.quantity) " +
                "FROM CropSelection cs JOIN cs.crop c " +
                "WHERE cs.design.id = :designId AND c.height < 100 " +
                "ORDER BY c.isRepeated DESC, c.height DESC, cs.priority ASC";

        cropList.addAll(em.createQuery(jpql, CropSelectionOrderedByCropDto.class)
                .setParameter("designId", designId)
                .getResultList());


        char[][] arrangement = getSelectedArrangement(design).getArrangement();
        int farmHeight = arrangement.length;
        int farmWidth = arrangement[0].length;
        CropForDesignDto[][] cropArray = new CropForDesignDto[farmHeight][farmWidth];
        Boolean isHorizontal = design.getIsHorizontal();

        // 방향에 맞는 이랑의 가로, 세로
        Integer ridgeWidth = isHorizontal ? farmHeight : farmWidth;
        Integer ridgeHeight = design.getRidgeWidth() / 10;

        // 밭 가로, 세로



        /* while로 좀 해보자 */
        int w = 0;
        int h = 0;
        int lastW = 0;
        int lastH = 0;
        coor : while (h < farmHeight) {

            System.out.println("-------------while 제일 위-------------");
            System.out.println("w = " + w);
            System.out.println("h = " + h);

            // 작물 꺼내
            // TODO : 0으로 돌려내
            for (int i = 0; i < cropList.size(); i++) {
                CropSelectionOrderedByCropDto cropSelectionDto = cropList.get(i);
                Integer quantity = cropSelectionDto.getQuantity();
                Integer cropWidth = cropSelectionDto.getCropSpacing() / 10; // 포기 간격(일반적으로 가로)
                Integer cropHeight = cropSelectionDto.getRidgeSpacing() / 10; // 줄 간격(일반적으로 세로)

                System.out.println("-------------작물 뽑는 for문-------------");
                System.out.println("i = " + i);

//                if (cropWidth > ridgeWidth || cropHeight > ridgeHeight) {
//                    continue;
//                }

                int q = 0;
                outer:
                while (q < quantity) {
                    System.out.println("-------------작물개수만큼 돌리는 while-------------");
                    if (w >= ridgeWidth || h >= farmHeight) {
//                        w = lastW;
//                        h = lastH;
                        break;
                    }

                    char farmCell = arrangement[h][w];
                    CropForDesignDto cropForDesignDto = cropArray[h][w];

//                    log.info("(r,c) : ( {}, {} )", h, w);
//                    log.info("arrangement[{}][{}] = {}",h,w,farmCell);

                    // 두둑인 부분에서 for문 돌리기
                    if (farmCell == 'R' && cropForDesignDto == null) {
                        int countCells = 0;

                        for (int addHeight = 0; addHeight < cropHeight; addHeight++) {
                            for (int addWidth = 0; addWidth < cropWidth; addWidth++) {
                                int newHeight = h + addHeight;
                                int newWidth = w + addWidth;
                                // 범위 벗어난 경우
                                if (newHeight >= farmHeight || newWidth >= ridgeWidth) {
                                    h += cropHeight;
                                    w = 0;
//                                    continue outer;
                                    break;
                                }

//                                // 두둑이 아닌 경우-E
//                                if (arrangement[newHeight][newWidth] == 'E') {
//                                    w++;
//                                    continue outer;
//                                }
//                                // 두둑이 아닌 경우-F
//                                if (arrangement[newHeight][newWidth] == 'F') {
//                                    h++;
//                                    w = 0;
//                                    continue outer;
//                                }
                                if (arrangement[newHeight][newWidth] == 'G') {
                                    countCells++;

                                }

                            }
                        }

                        // 작물 범위가 전부 두둑에 있으면 추가
                        if (countCells == cropWidth * cropHeight) {
                            for (int addHeight = 0; addHeight <= cropHeight; addHeight++) {
                                for (int addWidth = 0; addWidth <= cropWidth; addWidth++) {
                                    int newHeight = h + addHeight;
                                    int newWidth = w + addWidth;
                                    cropArray[newHeight][newWidth] = CropForDesignDto.builder().cropId(cropSelectionDto.getCropId()).number(q).build();
//                                    lastW = newWidth;
//                                    lastH = newHeight;
                                }
                            }
                            q++;

                            if (w >= (ridgeWidth - cropWidth)) {
                                w = 0;
                                h += cropHeight;
                            } else {
                                w++;
                            }
                        }


                    } else if (farmCell == 'F') {
                        h++;
                        w = 0;
                    } else {
                        if (w >= (ridgeWidth - cropWidth)) {
                            w = 0;
                            h += cropHeight;
                        } else {
                            w++;
                        }
                    }
                }
            }
        }

        return cropArray;
    }


    /**
     * 이랑 초기화
     */
    /*private TotalRidgeDto[]getTotalRidge(int farmWidthCell, int farmHeightCell, int ridgeWidthCell, Integer furrowWidth, int totalRidgeLength, Boolean isHorizontal) {
        TotalRidgeDto[] totalRidges;

        // 세로로 자른 밭
        if (isHorizontal) {
            totalRidges = new TotalRidgeDto[(farmWidthCell * 10) / totalRidgeLength];

            for (int i = 0; i < totalRidges.length; i++) {
                totalRidges[i] = TotalRidgeDto.builder()
                        .ridge(RidgeDto.builder().grid(new int[farmHeightCell][ridgeWidthCell]).build())
                        .furrow(FurrowDto.builder().width(furrowWidth).height(farmHeightCell * 10).build())
                        .build();
            }
        }
        // 가로로 자른 밭
        else {
            totalRidges = new TotalRidgeDto[(farmHeightCell * 10) / totalRidgeLength];

            for (int i = 0; i < totalRidges.length; i++) {
                totalRidges[i] = TotalRidgeDto.builder()
                        .ridge(RidgeDto.builder().grid(new int[ridgeWidthCell][farmWidthCell]).build())
                        .furrow(FurrowDto.builder().width(farmWidthCell * 10).height(furrowWidth).build())
                        .build();
            }
        }
        return totalRidges;
    }*/
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
                .name(design.getName())
                .savedTime(design.getUpdatedAt())
                .cropList(cropList)
                .build();
    }

    @Override
    @Transactional
    public Boolean updateDesign(Long designId, DesignUpdateRequestDto request) {
        return null;
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

    @Override
    @Transactional
    public Boolean insertCustomDesign(Long designId, CustomDesignCreateRequestDto request) {
        return null;
    }

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
