package com.ysgpjt.seokvey.survey.controller;

import com.ysgpjt.seokvey.survey.dto.SurveyCreateRequest;
import com.ysgpjt.seokvey.survey.dto.SurveyQuery;
import com.ysgpjt.seokvey.survey.dto.SurveyResponse;
import com.ysgpjt.seokvey.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    @GetMapping
    public List<SurveyQuery> getSurvey(@RequestParam Long surveyId) {
        return surveyService.getSurvey(surveyId);
    }

    @PostMapping
    public SurveyResponse createSurvey(@RequestBody SurveyCreateRequest surveyCreateRequest) {
        return surveyService.createSurvey(surveyCreateRequest);
    }

}
