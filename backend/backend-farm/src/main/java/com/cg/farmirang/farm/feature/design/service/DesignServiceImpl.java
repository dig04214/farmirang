package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.DesignSaveRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.EmptyFarmCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.RecommendedDesignCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.response.DesignDetailResponseDto;
import com.cg.farmirang.farm.feature.design.entity.Crop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignServiceImpl implements DesignService {
    @Override
    public Crop[][] insertEmptyFarm(EmptyFarmCreateRequestDto request) {
        return new Crop[0][];
    }

    @Override
    public Boolean insertRecommendedDesign(Crop[][] emptyField, RecommendedDesignCreateRequestDto request) {
        return null;
    }

    @Override
    public Boolean insertDesign(DesignSaveRequestDto request) {
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
    public DesignDetailResponseDto updateDesign(Long designId, DesignSaveRequestDto request) {
        return null;
    }

    @Override
    public Boolean deleteDesign(Long designId) {
        return null;
    }
}
