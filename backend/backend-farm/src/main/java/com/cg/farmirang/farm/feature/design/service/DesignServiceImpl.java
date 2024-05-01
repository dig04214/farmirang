package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.FurrowDto;
import com.cg.farmirang.farm.feature.design.dto.RecommendedDesignInfoDto;
import com.cg.farmirang.farm.feature.design.dto.RidgeDto;
import com.cg.farmirang.farm.feature.design.dto.TotalRidgeDto;
import com.cg.farmirang.farm.feature.design.dto.request.*;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import com.cg.farmirang.farm.feature.design.entity.Arrangement;
import com.cg.farmirang.farm.feature.design.entity.Design;
import com.cg.farmirang.farm.feature.design.entity.FarmCoordinate;
import com.cg.farmirang.farm.feature.design.entity.Member;
import com.cg.farmirang.farm.feature.design.repository.ArrangementRepository;
import com.cg.farmirang.farm.feature.design.repository.DesignRepository;
import com.cg.farmirang.farm.feature.design.repository.FarmCoordinateRepository;
import com.cg.farmirang.farm.feature.design.repository.MemberRepository;
import com.cg.farmirang.farm.global.common.code.ErrorCode;
import com.cg.farmirang.farm.global.exception.BusinessExceptionHandler;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DesignServiceImpl implements DesignService {

    private final DesignRepository designRepository;
    private final MemberRepository memberRepository;
    private final FarmCoordinateRepository farmCoordinateRepository;
    private final ArrangementRepository arrangementRepository;
    private final EntityManager em;

    /**
     * 빈 밭 생성
     *
     * @param token
     * @param request
     * @return
     */
    @Override
    public EmptyFarmCreateResponseDto insertEmptyFarm(HttpServletRequest token, EmptyFarmCreateRequestDto request) {

        // TODO : 회원 확인 -> 이후에 통신 예정
        Member member = memberRepository.findById(1).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.MEMBER_NOT_FOUND));

        // DB에 design 저장
        Design design = Design.builder()
                .member(member)
                .area(request.getArea())
                .startMonth(request.getStartMonth())
                .ridgeWidth(request.getRidgeWidth())
                .furrowWidth(request.getFurrowWidth())
                .isHorizontal(request.getIsHorizontal())
                .build();

        Design savedDesign = designRepository.save(design);

        // 배열 생성
        List<CoordinateRequestDto> coordinates = request.getCoordinates();
        int minX=100; int maxX=0; int minY=100; int maxY=0;

        // X, Y 최대 최소 구하기
        for (CoordinateRequestDto coordinate : coordinates) {
            minX=Math.min(minX,coordinate.getX());
            maxX=Math.max(maxX,coordinate.getX());
            minY=Math.min(minY,coordinate.getY());
            maxY=Math.max(maxY,coordinate.getY());
        }

        int row=maxY-minY;
        int column=maxX-minX;

        int[][] farm=new int[row][column];

        // 좌표값에서 최소값 빼고 좌표 DB에 저장
        for (CoordinateRequestDto coordinate : coordinates) {
            FarmCoordinate farmCoordinate = FarmCoordinate.builder()
                    .design(savedDesign)
                    .x(coordinate.getX()-minX)
                    .y(coordinate.getY()-minY)
                    .sequence(coordinate.getSequence())
                    .build();
            FarmCoordinate save = farmCoordinateRepository.save(farmCoordinate);
            savedDesign.addFarmCoordinate(save);

        }

        // 몽고DB에 배열 저장
        Arrangement arrangement = arrangementRepository.save(Arrangement.builder().arrangement(farm).build());
        String arrangementId = arrangement.getId();
        Gson gson=new Gson();

        Arrangement selectedArrangement = arrangementRepository.findById(arrangementId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.ARRANGEMENT_NOT_FOUND));

        // design에 arrangementId 추가
        savedDesign.setArrangementId(arrangementId);
        designRepository.save(savedDesign);

        return EmptyFarmCreateResponseDto.builder()
                .designId(savedDesign.getDesignId())
                .arrangement(gson.toJson(selectedArrangement.getArrangement()))
                .build();
    }

    /**
     * 작물 리스트 조회 - 심는 시기에 맞는 작물 우선 정렬
     *
     * @param designId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CropGetResponseDto> selectCropList(Long designId) {
        Design design = getDesign(designId);
        String startMonth = Integer.toString(design.getStartMonth());

        // 시작 달이 추천 파종시기인 작물부터 정렬
        List<Object[]> results = em.createQuery("SELECT t.name, CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN true ELSE false END AS isRecommended, t.ridgeSpacing * t.cropSpacing AS area FROM Crop t ORDER BY CASE WHEN :substring IN (SELECT UNNEST(FUNCTION('string_to_array', t.sowingTime, ',')) AS st) THEN 0 ELSE 1 END, t.sowingTime")
                .setParameter("substring", startMonth)
                .getResultList();

        List<CropGetResponseDto> list = new ArrayList<>();

        for (Object[] result : results) {
            CropGetResponseDto cropDto = CropGetResponseDto.builder()
                    .name((String) result[0])
                    .isRecommended((boolean) result[1])
                    .cellQuantity((int) (Math.ceil(((Integer) result[2]).intValue()) / 100))
                    .build();
            list.add(cropDto);
        }
        return list;
    }

    /**
     * 디자인 추천 생성
     *
     * @param designId
     * @param request
     * @return
     */
    @Override
    public RecommendedDesignCreateResponseDto insertRecommendedDesign(Long designId, List<RecommendedDesignCreateRequestDto> request) {
        Design design = getDesign(designId);

        // 밭 불러오기
        Arrangement selectedArrangement = getSelectedArrangement(design);
        int[][] arrangement = selectedArrangement.getArrangement();

        // 이랑 배열 생성
        RecommendedDesignInfoDto designInfo = design.getDesignInfo();

        Integer furrowWidth = designInfo.getFurrowWidth();
        Integer ridgeWidth = designInfo.getRidgeWidth();
        int farmWidthCell = arrangement[0].length;
        int farmHeightCell = arrangement.length;



        TotalRidgeDto[] totalRidges=getTotalRidge(farmWidthCell,farmHeightCell, ridgeWidth/10, furrowWidth, (furrowWidth + ridgeWidth),designInfo.getIsHorizontal());

        // 두둑에서 알고리즘으로 배치하기


        // 몽고디비에 다시 업데이트


        return null;
    }

    private TotalRidgeDto[] getTotalRidge(int farmWidthCell, int farmHeightCell, int ridgeWidthCell, Integer furrowWidth, int totalRidgeLength, Boolean isHorizontal) {
        TotalRidgeDto[] totalRidges;

        // 세로로 자른 밭
        if (isHorizontal) {
            totalRidges = new TotalRidgeDto[farmWidthCell * 10 % totalRidgeLength];

            for (TotalRidgeDto totalRidge : totalRidges) {
                totalRidge.setRidge(RidgeDto.builder().grid(new int[ridgeWidthCell][farmWidthCell]).build());
                totalRidge.setFurrow(FurrowDto.builder().width(furrowWidth).height(farmHeightCell*10).build());
            }

        }
        // 가로로 자른 밭
        else {
            totalRidges = new TotalRidgeDto[farmHeightCell * 10 % totalRidgeLength];

            for (TotalRidgeDto totalRidge : totalRidges) {
                totalRidge.setRidge(RidgeDto.builder().grid(new int[farmHeightCell][ridgeWidthCell]).build());
                totalRidge.setFurrow(FurrowDto.builder().width(farmWidthCell*10).height(furrowWidth).build());
            }
        }
        return totalRidges;
    }


    private Arrangement getSelectedArrangement(Design design) {
        return arrangementRepository.findById(design.getArrangementId()).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.ARRANGEMENT_NOT_FOUND));
    }

    private Design getDesign(Long designId) {
        return designRepository.findById(designId).orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.DESIGN_NOT_FOUND));
    }

    @Override
    public Boolean insertDesign(DesignUpdateRequestDto request) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignDetailResponseDto> selectDesignList(Integer memberId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public DesignDetailResponseDto selectDesign(Long designId) {
        return null;
    }

    @Override
    public Boolean updateDesign(Long designId, DesignUpdateRequestDto request) {
        return null;
    }

    @Override
    public Boolean deleteDesign(Long designId) {
        return null;
    }

    @Override
    public Boolean insertPesticideAndFertilizerSelection(PesticideAndFertilizerCreateDto request) {
        return null;
    }



    @Override
    @Transactional(readOnly = true)
    public EmptyFarmGetResponseDto selectEmptyFarm(Long designId) {
        return null;
    }

    @Override
    public Boolean insertCustomDesign(Long designId, CustomDesignCreateRequestDto request) {
        return null;
    }

    @Override
    public Boolean updateDesignName(Long designId, DesignNameUpdateRequestDto request) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ChemicalGetResponseDto selectChemical(Long designId) {
        return null;
    }
}
