package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.*;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import com.cg.farmirang.farm.feature.design.entity.Design;
import com.cg.farmirang.farm.feature.design.entity.FarmCoordinate;
import com.cg.farmirang.farm.feature.design.entity.Member;
import com.cg.farmirang.farm.feature.design.repository.DesignRepository;
import com.cg.farmirang.farm.feature.design.repository.FarmCoordinateRepository;
import com.cg.farmirang.farm.feature.design.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DesignServiceImpl implements DesignService {

//    private final JwtUtil jwtUtil;
    private final DesignRepository designRepository;
    private final MemberRepository memberRepository;
    private final FarmCoordinateRepository farmCoordinateRepository;

    @Override
    public EmptyFarmCreateResponseDto insertEmptyFarm(HttpServletRequest token, EmptyFarmCreateRequestDto request) {

        // 회원 확인
        Member member=searchMember(token);
        // DB에 design 저장
        Design design = Design.builder()
                .member(member)
                .area(request.getArea())
                .startMonth(request.getStartMonth())
                .location(request.getLocation())
                .ridgeWidth(request.getRidgeWidth())
                .furrowWidth(request.getFurrowWidth())
                .isHorizontal(request.getIsHorizontal())
                .build();

        Design savedDesign = designRepository.save(design);

        // 좌표 DB 저장
        List<CoordinateRequestDto> coordinates = request.getCoordinates();
        for (CoordinateRequestDto coordinate : coordinates) {
            FarmCoordinate farmCoordinate = FarmCoordinate.builder()
                    .design(savedDesign)
                    .x(coordinate.getX())
                    .y(coordinate.getY())
                    .sequence(coordinate.getSequence())
                    .build();
            savedDesign.addFarmCoordinate(farmCoordinate);
            farmCoordinateRepository.save(farmCoordinate);
        }

        designRepository.save(savedDesign);

        // 좌표로 2차원 배열 생성


        // 몽고DB에 배열 저장


        return null;
    }

    private Member searchMember(HttpServletRequest token) {
        return null;
    }

    @Override
    public Boolean insertRecommendedDesign(Long emptyField, List<RecommendedDesignCreateRequestDto> request) {
        return null;
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
    public List<CropGetResponseDto> selectCropList(Long designId) {
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
