package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.dto.QuestionQuery;
import com.ysgpjt.seokvey.survey.dto.SurveyQuery;
import com.ysgpjt.seokvey.survey.dto.SurveyReadRequest;
import com.ysgpjt.seokvey.survey.entity.Survey;

import java.util.List;

public interface SurveyQueryRepository {

    List<Survey> findAllSurvey(SurveyReadRequest surveyReadRequest);
    List<SurveyQuery> findSurvey(SurveyReadRequest surveyReadRequest);
    List<QuestionQuery> findQuestion(SurveyReadRequest surveyReadRequest);
}
