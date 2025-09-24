package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.dto.SurveyQuery;

import java.util.List;

public interface SurveyQueryRepository {

    List<SurveyQuery> findSurvey(Long surveyId);
}
