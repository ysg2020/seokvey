package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.Survey;

import java.util.List;

public interface SurveyQueryRepository {

    List<Survey> findAllSurvey(SurveyReadRequest surveyReadRequest);
    List<SurveyQuery> findSurvey(SurveyReadRequest surveyReadRequest);
    List<QuestionQuery> findQuestion(SurveyReadRequest surveyReadRequest);
    List<SurveyParticipationQuery> findSurveyParticipation(SurveyParticipationReadRequest surveyParticipationReadRequest);
    List<SurveyResultQuery> findSurveyResult(SurveyResultReadRequest surveyResultReadRequest);
}
