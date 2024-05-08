package com.cg.farmirang.farm.feature.design.controller;

import com.cg.farmirang.farm.feature.design.dto.request.*;
import com.cg.farmirang.farm.feature.design.dto.response.*;
import com.cg.farmirang.farm.feature.design.service.DesignService;
import com.cg.farmirang.farm.global.common.code.SuccessCode;
import com.cg.farmirang.farm.global.common.response.ErrorResponse;
import com.cg.farmirang.farm.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/design")
@Tag(name = "field", description = "텃밭 디자인 API")
public class DesignController {

    private final DesignService designService;

    @PostMapping
    @Operation(summary = "디자인용 텃밭 생성", description = "입력된 내용으로 디자인용 텃밭을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "디자인용 텃밭 생성을 성공하였습니다.", content = {@Content(schema = @Schema(implementation = EmptyFarmCreateResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "없는 배열입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> createEmptyFarm(@Validated @RequestBody EmptyFarmCreateRequestDto request, @Nullable HttpServletRequest token){
        EmptyFarmCreateResponseDto response=designService.insertEmptyFarm(token, request);

        return SuccessResponse.builder().data(response).status(SuccessCode.INSERT_SUCCESS).build();
    }

    @GetMapping("/{designId}/crop")
    @Operation(summary = "작물 정보 조회", description = "작물 선택을 위해 작물 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작물 정보 조회에 성공했습니다.", content = {@Content(array=@ArraySchema(schema = @Schema(implementation = CropGetResponseDto.class)))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> getCrops(@PathVariable Long designId){
        CropGetResponseDto response=designService.selectCropList(designId);
        return SuccessResponse.builder().data(response).status(SuccessCode.SELECT_SUCCESS).build();
    }

    @PostMapping("/{designId}/recommend")
    @Operation(summary = "추천 디자인 생성", description = "입력된 내용으로 추천 디자인을 생성합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "추천 디자인 생성을 성공하였습니다.",
            content = {@Content(schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> createRecommendedDesign(@PathVariable Long designId, @Validated @RequestBody List<RecommendedDesignCreateRequestDto> request){
        RecommendedDesignCreateResponseDto response= designService.insertRecommendedDesign(designId, request);

        return SuccessResponse.builder().data(response).status(SuccessCode.INSERT_SUCCESS).build();
    }


    @GetMapping("/{designId}/custom")
    @Operation(summary = "커스텀용 빈 밭 조회", description = "밭 커스텀을 위해 빈 밭을 조회합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "빈 밭 조회에 성공했습니다.",
            content = {@Content(schema = @Schema(implementation = EmptyFarmGetResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> getFarm(@PathVariable Long designId){
        EmptyFarmGetResponseDto response=designService.selectEmptyFarm(designId);
        return SuccessResponse.builder().data(response).status(SuccessCode.SELECT_SUCCESS).build();
    }

    @PostMapping("/{designId}/custom")
    @Operation(summary = "커스텀 디자인 생성", description = "입력된 내용으로 커스텀 디자인을 생성합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "커스텀 디자인 생성을 성공하였습니다.",
            content = {@Content(schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> createCustomDesign(@PathVariable Long designId, @Validated @RequestBody CustomDesignCreateRequestDto request){
        Boolean response= designService.insertCustomDesign(designId, request);

        // 임시, MongoDB 공부 후 그 데이터 넘겨줄 예정
        return SuccessResponse.builder().data(response).status(SuccessCode.INSERT_SUCCESS).build();
    }

    @PutMapping("/{designId}/name")
    @Operation(summary = "디자인 이름 수정", description = "디자인 이름을 수정합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "디자인 이름 수정에 성공했습니다.",
            content = {@Content(schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> updateDesignName(@PathVariable Long designId, DesignNameUpdateRequestDto request){
        Boolean result = designService.updateDesignName(designId, request);
        return SuccessResponse.builder().data(result).status(SuccessCode.UPDATE_SUCCESS).build();
    }

    @GetMapping("/list")
    @Operation(summary = "디자인 리스트 조회", description = "회원의 디자인 리스트를 조회합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "디자인 리스트 조회에 성공했습니다.",
            content = {@Content(array=@ArraySchema(schema = @Schema(implementation = CropGetResponseDto.class)))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> getDesignList(/*회원정보 뽑아오기*/){
        Integer memberId=0;

        List<DesignListResponseDto> response=designService.selectDesignList(memberId);
        return SuccessResponse.builder().data(response).status(SuccessCode.SELECT_SUCCESS).build();
    }

    @GetMapping("/{designId}")
    @Operation(summary = "디자인 상세조회", description = "선택된 디자인을 상세조회합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "디자인 상세조회에 성공했습니다.",
            content = {@Content(schema = @Schema(implementation = DesignListResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> getDesignDetail(@PathVariable Long designId){
        DesignDetailResponseDto response = designService.selectDesign(designId);
        return SuccessResponse.builder().data(response).status(SuccessCode.SELECT_SUCCESS).build();
    }

    @PutMapping("/{designId}/update")
    @Operation(summary = "디자인 수정", description = "디자인을 수정합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "디자인 수정에 성공했습니다.",
            content = {@Content(schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> updateDesign(@PathVariable Long designId, DesignUpdateRequestDto request){
        Boolean result = designService.updateDesign(designId, request);
        return SuccessResponse.builder().data(result).status(SuccessCode.UPDATE_SUCCESS).build();
    }

    @DeleteMapping("/{designId}")
    @Operation(summary = "디자인 삭제", description = "선택된 디자인을 삭제합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "205", description = "디자인 수정에 성공했습니다.",
            content = {@Content(schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> deleteDesign(@PathVariable Long designId){
        Boolean result = designService.deleteDesign(designId);
        return SuccessResponse.builder().data(result).status(SuccessCode.DELETE_SUCCESS).build();
    }

}



