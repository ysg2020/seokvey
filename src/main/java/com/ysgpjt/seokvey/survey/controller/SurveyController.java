package com.ysgpjt.seokvey.survey.controller;

import com.ysgpjt.seokvey.survey.dto.*;
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
    public List<SurveyResponse> getSurveyDetail(@RequestBody SurveyReadRequest surveyReadRequest) {
        return surveyService.getSurveyDetail(surveyReadRequest);
    }
    @GetMapping("/question")
    public List<QuestionResponse> getQuestion(@RequestBody SurveyReadRequest surveyReadRequest) {
        return surveyService.getQuestion(surveyReadRequest);
    }

    @PostMapping
    public SurveyResponse createSurvey(@RequestBody SurveyCreateRequest surveyCreateRequest) {
        return surveyService.createSurvey(surveyCreateRequest);
    }

}
