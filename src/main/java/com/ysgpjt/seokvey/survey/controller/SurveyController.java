package com.ysgpjt.seokvey.survey.controller;

import com.ysgpjt.seokvey.common.dto.ApiResponse;
import com.ysgpjt.seokvey.common.dto.PagedResponse;
import com.ysgpjt.seokvey.common.util.CookieUtil;
import com.ysgpjt.seokvey.common.util.SecurityUtil;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Survey", description = "Survey API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(summary = "설문 전체 조회", description = "설문을 전체 조회합니다.")
    @GetMapping("/all")
    public ApiResponse<PagedResponse<SurveyResponse>> getAllSurvey(@RequestBody SurveyReadRequest surveyReadRequest) {
        return ApiResponse.ok(surveyService.getAllSurvey(surveyReadRequest),"survey/all");
    }

    @Operation(summary = "설문 상세 조회", description = "설문의 문항과 옵션을 조회합니다.")
    @GetMapping
    public ApiResponse<SurveyResponse> getSurvey(@RequestBody SurveyReadRequest surveyReadRequest) {
        return ApiResponse.ok(surveyService.getSurvey(surveyReadRequest),"/survey");
    }

    @Operation(summary = "문항 조회", description = "설문의 문항과 옵션을 조회합니다.")
    @GetMapping("/question")
    public ApiResponse<List<QuestionResponse>> getQuestion(@RequestBody SurveyReadRequest surveyReadRequest) {
        return ApiResponse.ok(surveyService.getQuestion(surveyReadRequest),"/survey/question");
    }

    @Operation(summary = "설문 생성", description = "설문을 생성합니다.")
    @PostMapping
    public ApiResponse<SurveyResponse> createSurvey(@RequestBody SurveyCreateRequest surveyCreateRequest) {
        return ApiResponse.ok(surveyService.createSurvey(surveyCreateRequest),"/survey");
    }

    @Operation(summary = "설문 수정", description = "설문을 수정합니다.")
    @PutMapping
    public ApiResponse<SurveyResponse> updateSurvey(@RequestBody SurveyUpdateRequest surveyUpdateRequest) {
        return ApiResponse.ok(surveyService.updateSurvey(surveyUpdateRequest),"/survey");
    }

    @Operation(summary = "설문 삭제", description = "설문을 삭제합니다.")
    @DeleteMapping
    public ApiResponse<SurveyResponse> deleteSurvey(@RequestBody SurveyReadRequest surveyReadRequest) {
        return ApiResponse.ok(surveyService.deleteSurvey(surveyReadRequest),"/survey");
    }

    @Operation(summary = "설문 참여 조회", description = "참여한 설문을 조회합니다.")
    @GetMapping("/participation")
    public ApiResponse<PagedResponse<SurveyParticipationResponse>> getSurveyParticipation(@RequestBody SurveyParticipationReadRequest surveyParticipationReadRequest) {
        return ApiResponse.ok(surveyService.getSurveyParticipation(surveyParticipationReadRequest),"/survey/participation");
    }
    @PostMapping("/participation")
    public ApiResponse<SurveyResponse> participateSurvey(@RequestBody SurveyParticipationRequest surveyParticipationRequest
            , HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse) {

        String anonymousToken = null;
        String ipAddress = null;

        // 로그인한 사용자가 아닌경우
        if (!SecurityUtil.isLoggedIn()) {
            // 토큰 값과 IP 주소 가져오기
            anonymousToken = CookieUtil.getAnonymousToken(httpServletRequest, httpServletResponse);
            ipAddress = httpServletRequest.getRemoteAddr();
        }

        return ApiResponse.ok(surveyService.participateSurvey(surveyParticipationRequest,anonymousToken,ipAddress),"/survey/participation");
    }
    @GetMapping("/result")
    public ApiResponse<List<SurveyResultResponse>> getSurveyResult(@RequestBody SurveyResultReadRequest surveyResultReadRequest) {
        return ApiResponse.ok(surveyService.getSurveyResult(surveyResultReadRequest),"/survey/result");
    }


}
