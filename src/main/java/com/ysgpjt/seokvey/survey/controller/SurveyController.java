package com.ysgpjt.seokvey.survey.controller;

import com.ysgpjt.seokvey.survey.dto.SurveyCreateRequest;
import com.ysgpjt.seokvey.survey.dto.SurveyResponse;
import com.ysgpjt.seokvey.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public SurveyResponse createSurvey(@RequestBody SurveyCreateRequest surveyCreateRequest) {
        return surveyService.createSurvey(surveyCreateRequest);
    }

}
