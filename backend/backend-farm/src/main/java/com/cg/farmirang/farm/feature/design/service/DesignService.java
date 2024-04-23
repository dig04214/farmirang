package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.DesignSaveRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.PesticideAndFertilizerCreateDto;
import com.cg.farmirang.farm.feature.design.dto.request.RecommendedDesignCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.EmptyFarmCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.response.DesignDetailResponseDto;
import com.cg.farmirang.farm.feature.design.entity.Crop;

import java.util.List;

public interface DesignService {
    public Crop[][] insertEmptyFarm(EmptyFarmCreateRequestDto request);

    public Boolean insertRecommendedDesign(Crop[][] emptyField, RecommendedDesignCreateRequestDto request);

    Boolean insertDesign(DesignSaveRequestDto request);


    List<DesignDetailResponseDto> selectDesignList(Integer memberId);

    DesignDetailResponseDto selectDesign(Long designId);

    DesignDetailResponseDto updateDesign(Long designId, DesignSaveRequestDto request);

    Boolean deleteDesign(Long designId);

    Boolean insertPesticideAndFertilizerSelection(PesticideAndFertilizerCreateDto request);
}
