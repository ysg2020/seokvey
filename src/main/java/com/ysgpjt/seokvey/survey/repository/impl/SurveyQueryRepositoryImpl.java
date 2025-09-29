package com.ysgpjt.seokvey.survey.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.*;
import com.ysgpjt.seokvey.survey.repository.SurveyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyQueryRepositoryImpl implements SurveyQueryRepository {

    private final JPAQueryFactory mainQueryFactory;

    @Override
    public List<Survey> findAllSurvey(SurveyReadRequest surveyReadRequest) {
        return mainQueryFactory.selectFrom(QSurvey.survey)
                .offset((long) surveyReadRequest.getPage() * surveyReadRequest.getSize())
                .limit(surveyReadRequest.getSize())
                .fetch();

    }

    @Override
    public List<SurveyQuery> findSurvey(SurveyReadRequest surveyReadRequest) {
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
                .where(survey.id.eq(surveyReadRequest.getSurveyId()))
                .offset((long) surveyReadRequest.getPage() * surveyReadRequest.getSize())
                .limit(surveyReadRequest.getSize())
                .fetch();
    }

    @Override
    public List<QuestionQuery> findQuestion(SurveyReadRequest surveyReadRequest) {
        QQuestion question = QQuestion.question;
        QQuestionOption option = QQuestionOption.questionOption;

        // 페이징 처리한 문항 아이디 리스트 조회
        List<Long> questionIdList = mainQueryFactory.select(question.id)
                .from(question)
                .leftJoin(option).on(option.question.eq(question))
                .where(question.survey.id.eq(surveyReadRequest.getSurveyId()))
                .offset((long) surveyReadRequest.getPage() * surveyReadRequest.getSize())
                .limit(surveyReadRequest.getSize())
                .fetch();

        return mainQueryFactory.select(Projections.constructor(QuestionQuery.class
                        ,question.id
                        ,question.content
                        ,question.selectionType
                        ,question.orderNo
                        ,option.id
                        ,option.content
                        ,option.orderNo
                ))
                .from(question)
                .leftJoin(option).on(option.question.eq(question))
                .where(question.id.in(questionIdList))
                .orderBy(question.orderNo.asc(), option.orderNo.asc())
                .fetch();
    }

    @Override
    public List<SurveyParticipationQuery> findSurveyParticipation(SurveyParticipationReadRequest surveyParticipationReadRequest) {
        QSurveyParticipation surveyParticipation = QSurveyParticipation.surveyParticipation;
        QSurveyAnswer surveyAnswer = QSurveyAnswer.surveyAnswer;
        return mainQueryFactory.select(Projections.constructor(SurveyParticipationQuery.class
                        , surveyParticipation.id
                        , surveyParticipation.survey.id
                        , surveyParticipation.userId
                        , surveyParticipation.surveyDt
                        , surveyAnswer.question.id
                        , surveyAnswer.question.content
                        , surveyAnswer.questionOption.id
                        , surveyAnswer.questionOption.content
                ))
                .from(surveyParticipation)
                .leftJoin(surveyAnswer).on(surveyParticipation.eq(surveyAnswer.surveyParticipation))
                .where(surveyParticipation.userId.eq(surveyParticipationReadRequest.getUserId()))
                .offset((long) surveyParticipationReadRequest.getPage() * surveyParticipationReadRequest.getSize())
                .limit(surveyParticipationReadRequest.getSize())
                .fetch();
    }
}
