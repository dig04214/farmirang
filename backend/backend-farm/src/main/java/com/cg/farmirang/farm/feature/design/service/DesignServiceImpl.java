package com.cg.farmirang.farm.feature.design.service;

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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DesignServiceImpl implements DesignService {

    private final DesignRepository designRepository;
    private final MemberRepository memberRepository;
    private final FarmCoordinateRepository farmCoordinateRepository;
    private final ArrangementRepository arrangementRepository;

    @Override
    public EmptyFarmCreateResponseDto insertEmptyFarm(HttpServletRequest token, EmptyFarmCreateRequestDto request) {

        // TODO : 회원 확인 -> 이후에 통신 예정
        Member member = memberRepository.save(Member.builder().nickname("test").build());

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
        savedDesign.addArrangementId(arrangementId);
        designRepository.save(savedDesign);

        return EmptyFarmCreateResponseDto.builder()
                .designId(savedDesign.getDesignId())
                .arrangement(gson.toJson(selectedArrangement.getArrangement()))
                .build();
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
