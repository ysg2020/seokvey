package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.common.dto.PagedResponse;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.Survey;

import java.util.List;

public interface SurveyQueryRepository {

    PagedResponse<SurveyResponse> findAllSurvey(SurveyReadRequest surveyReadRequest);
    Survey findSurvey(SurveyReadRequest surveyReadRequest);
    PagedResponse<QuestionQuery> findQuestion(SurveyReadRequest surveyReadRequest);
    PagedResponse<SurveyParticipationQuery> findSurveyParticipation(SurveyParticipationReadRequest surveyParticipationReadRequest);
    PagedResponse<SurveyResultQuery> findSurveyResult(SurveyResultReadRequest surveyResultReadRequest);
}
