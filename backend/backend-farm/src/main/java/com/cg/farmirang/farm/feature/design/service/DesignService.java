package com.cg.farmirang.farm.feature.design.service;

import com.cg.farmirang.farm.feature.design.dto.request.*;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import jakarta.validation.constraints.NotBlank;

public interface DesignService {
    public EmptyFarmCreateResponseDto insertEmptyFarm(@NotBlank Integer memberId, EmptyFarmCreateRequestDto request);

    public RecommendedDesignCreateResponseDto insertRecommendedDesign(@NotBlank Integer memberId, Long designId, RecommendedDesignCreateRequestDto request);

    DesignListResponseDto selectDesignList(@NotBlank Integer memberId);

    DesignDetailResponseDto selectDesign(@NotBlank Integer memberId, Long designId);

    Boolean updateDesign(@NotBlank Integer memberId, Long designId, DesignUpdateRequestDto request);

    Boolean deleteDesign(@NotBlank Integer memberId, Long designId);

    CropGetResponseDto selectCropList(@NotBlank Integer memberId, Long designId);

    EmptyFarmGetResponseDto selectEmptyFarm(@NotBlank Integer memberId, Long designId);

    Boolean insertCustomDesign(@NotBlank Integer memberId, Long designId, CustomDesignCreateRequestDto request);

    Boolean updateDesignName(@NotBlank Integer memberId, Long designId, DesignNameUpdateRequestDto request);

    Boolean updateThumbnailDesign(Long designId, @NotBlank Integer memberId);

    ThumbnailDesignResponseDto selectThumbnailDesign(@NotBlank Integer memberId);
}
