package com.ysgpjt.seokvey.survey.controller;

import com.ysgpjt.seokvey.common.dto.PagedResponse;
import com.ysgpjt.seokvey.common.util.CookieUtil;
import com.ysgpjt.seokvey.common.util.SecurityUtil;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.service.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping("/all")
    public PagedResponse<SurveyResponse> getAllSurvey(@RequestBody SurveyReadRequest surveyReadRequest) {
        return surveyService.getAllSurvey(surveyReadRequest);
    }
    @GetMapping
    public SurveyResponse getSurvey(@RequestBody SurveyReadRequest surveyReadRequest) {
        return surveyService.getSurvey(surveyReadRequest);
    }
    @GetMapping("/question")
    public List<QuestionResponse> getQuestion(@RequestBody SurveyReadRequest surveyReadRequest) {
        return surveyService.getQuestion(surveyReadRequest);
    }
    @PostMapping
    public SurveyResponse createSurvey(@RequestBody SurveyCreateRequest surveyCreateRequest) {
        return surveyService.createSurvey(surveyCreateRequest);
    }
    @PutMapping
    public SurveyResponse updateSurvey(@RequestBody SurveyUpdateRequest surveyUpdateRequest) {
        return surveyService.updateSurvey(surveyUpdateRequest);
    }
    @DeleteMapping
    public SurveyResponse deleteSurvey(@RequestBody SurveyReadRequest surveyReadRequest) {
        return surveyService.deleteSurvey(surveyReadRequest);
    }
    @GetMapping("/participation")
    public PagedResponse<SurveyParticipationResponse> getSurveyParticipation(@RequestBody SurveyParticipationReadRequest surveyParticipationReadRequest) {
        return surveyService.getSurveyParticipation(surveyParticipationReadRequest);
    }
    @PostMapping("/participation")
    public SurveyResponse participateSurvey(@RequestBody SurveyParticipationRequest surveyParticipationRequest
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

        return surveyService.participateSurvey(surveyParticipationRequest,anonymousToken,ipAddress);
    }
    @GetMapping("/result")
    public List<SurveyResultResponse> getSurveyResult(@RequestBody SurveyResultReadRequest surveyResultReadRequest) {
        return surveyService.getSurveyResult(surveyResultReadRequest);
    }


}
