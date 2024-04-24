package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.*;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import com.cg.farmirang.farm.feature.design.entity.Crop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignServiceImpl implements DesignService {
    @Override
    public EmptyFarmCreateResponseDto insertEmptyFarm(EmptyFarmCreateRequestDto request) {
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
    public List<DesignDetailResponseDto> selectDesignList(Integer memberId) {
        return null;
    }

    @Override
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
    public List<CropGetResponseDto> selectCropList(Long designId) {
        return null;
    }

    @Override
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
    public ChemicalGetResponseDto selectChemical(Long designId) {
        return null;
    }
}
