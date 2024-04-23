package com.cg.farmirang.farm.feature.design.controller;

import com.cg.farmirang.farm.feature.design.dto.request.DesignSaveRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.PesticideAndFertilizerCreateDto;
import com.cg.farmirang.farm.feature.design.dto.request.RecommendedDesignCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.request.EmptyFarmCreateRequestDto;
import com.cg.farmirang.farm.feature.design.dto.response.DesignDetailResponseDto;
import com.cg.farmirang.farm.feature.design.entity.Crop;
import com.cg.farmirang.farm.feature.design.entity.Design;
import com.cg.farmirang.farm.feature.design.service.DesignService;
import com.cg.farmirang.farm.global.common.code.SuccessCode;
import com.cg.farmirang.farm.global.common.response.ErrorResponse;
import com.cg.farmirang.farm.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(summary = "디자인 생성", description = "입력된 내용으로 추천 디자인을 생성합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "추천 디자인 생성을 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> createRecommendedDesign(@Validated @RequestBody RecommendedDesignCreateRequestDto request){
        Crop[][] emptyFarm=designService.insertEmptyFarm(request.getEmptyFarmDto());
        Boolean result= designService.insertRecommendedDesign(emptyFarm, request);

        // 임시, MongoDB 공부 후 그 데이터 넘겨줄 예정
        return SuccessResponse.builder().data(result).status(SuccessCode.INSERT_SUCCESS).build();
    }

    @PostMapping("/farm")
    @Operation(summary = "디자인용 텃밭 생성", description = "입력된 내용으로 디자인용 텃밭을 생성합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "디자인용 텃밭 생성을 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> createEmptyFarm(@Validated @RequestBody EmptyFarmCreateRequestDto request){
        Crop[][] emptyField=designService.insertEmptyFarm(request);

        return SuccessResponse.builder().data(emptyField).status(SuccessCode.INSERT_SUCCESS).build();
    }


    @PutMapping("/{designId}")
    @Operation(summary = "디자인 수정", description = "선택된 디자인을 수정합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "디자인 수정에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> updateDesign(@PathVariable Long designId, DesignSaveRequestDto request){
        DesignDetailResponseDto response = designService.updateDesign(designId, request);
        return SuccessResponse.builder().data(response).status(SuccessCode.UPDATE_SUCCESS).build();
    }


    @GetMapping("/list")
    @Operation(summary = "디자인 리스트 조회", description = "회원의 디자인 리스트를 조회합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "디자인 리스트 조회에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> getDesignList(/*회원정보 뽑아오기*/){
        Integer memberId=0;

        List<DesignDetailResponseDto> response=designService.selectDesignList(memberId);
        return SuccessResponse.builder().data(response).status(SuccessCode.SELECT_SUCCESS).build();
    }

    @GetMapping("/{designId}")
    @Operation(summary = "디자인 상세조회", description = "선택된 디자인을 상세조회합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "디자인 상세조회에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> getDesignDetail(@PathVariable Long designId){
        DesignDetailResponseDto response = designService.selectDesign(designId);
        return SuccessResponse.builder().data(response).status(SuccessCode.SELECT_SUCCESS).build();
    }


    @DeleteMapping("/{designId}")
    @Operation(summary = "디자인 삭제", description = "선택된 디자인을 삭제합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "205", description = "디자인 수정에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> deleteDesign(@PathVariable Long designId){
        Boolean result = designService.deleteDesign(designId);
        return SuccessResponse.builder().data(result).status(SuccessCode.DELETE_SUCCESS).build();
    }

    @PostMapping("/{designId}")
    @Operation(summary = "농약, 비료 선택", description = "선택된 농약과 비료를 생성합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "농약과 비료 생성에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 문제입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SuccessResponse<?> createPesticideAndFertilizerSelection(@PathVariable Long designId, @RequestBody PesticideAndFertilizerCreateDto request){
        Boolean result =designService.insertPesticideAndFertilizerSelection(request);
        return SuccessResponse.builder().data(result).status(SuccessCode.INSERT_SUCCESS).build();
    }


}
