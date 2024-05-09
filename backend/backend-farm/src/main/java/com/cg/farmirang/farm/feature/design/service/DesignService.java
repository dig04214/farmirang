package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.*;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface DesignService {
    public EmptyFarmCreateResponseDto insertEmptyFarm(HttpServletRequest token, EmptyFarmCreateRequestDto request);

    public RecommendedDesignCreateResponseDto insertRecommendedDesign(Long designId, List<RecommendedDesignCreateRequestDto> request);

    List<DesignListResponseDto> selectDesignList(Integer memberId);

    DesignDetailResponseDto selectDesign(Long designId);

    Boolean updateDesign(Long designId, DesignUpdateRequestDto request);

    Boolean deleteDesign(Long designId);

    CropGetResponseDto selectCropList(Long designId);

    EmptyFarmGetResponseDto selectEmptyFarm(Long designId);

    Boolean insertCustomDesign(Long designId, CustomDesignCreateRequestDto request);

    Boolean updateDesignName(Long designId, DesignNameUpdateRequestDto request);

    Boolean updateThumbnailDesign(Long designId);

    ThumbnailDesignResponseDto selectThumbnailDesign(int memberId);
}
