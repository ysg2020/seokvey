package com.ysgpjt.seokvey.survey.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ysgpjt.seokvey.survey.dto.SurveyQuery;
import com.ysgpjt.seokvey.survey.entity.QQuestion;
import com.ysgpjt.seokvey.survey.entity.QQuestionOption;
import com.ysgpjt.seokvey.survey.entity.QSurvey;
import com.ysgpjt.seokvey.survey.repository.SurveyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyQueryRepositoryImpl implements SurveyQueryRepository {

    private final JPAQueryFactory mainQueryFactory;

    @Override
    public List<SurveyQuery> findSurvey(Long surveyId) {
        QSurvey survey = QSurvey.survey;
        QQuestion question = QQuestion.question;
        QQuestionOption option = QQuestionOption.questionOption;

        return mainQueryFactory.select(Projections.constructor(SurveyQuery.class
                        ,survey.id
                        ,survey.title
                        ,survey.description
                        ,survey.startDt
                        ,survey.endDt
                        ,question.id
                        ,question.content
                        ,question.selectionType
                        ,question.orderNo
                        ,option.id
                        ,option.content
                        ,option.orderNo
                        ))
                .from(survey)
                .leftJoin(question).on(question.survey.eq(survey))
                .leftJoin(option).on(option.question.eq(question))
                .where(survey.id.eq(surveyId))
                .fetch();
    }
}
