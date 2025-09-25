package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.dto.QuestionQuery;
import com.ysgpjt.seokvey.survey.dto.SurveyQuery;
import com.ysgpjt.seokvey.survey.dto.SurveyReadRequest;

import java.util.List;

public interface SurveyQueryRepository {

    List<SurveyQuery> findSurvey(SurveyReadRequest surveyReadRequest);
    List<QuestionQuery> findQuestion(SurveyReadRequest surveyReadRequest);
}
