package com.ysgpjt.seokvey.survey.repository.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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
    public Long findAllSurveyTotalCount() {
        QSurvey survey = QSurvey.survey;
        return mainQueryFactory.select(survey.id.count()).from(survey).fetchOne();
    }

    @Override
    public Survey findSurvey(SurveyReadRequest surveyReadRequest) {
        QSurvey survey = QSurvey.survey;
        return mainQueryFactory.selectFrom(survey)
                .where(survey.id.eq(surveyReadRequest.getSurveyId()))
                .fetchOne();
    }

    @Override
    public List<QuestionQuery> findQuestion(SurveyReadRequest surveyReadRequest) {
        QQuestion question = QQuestion.question;
        QQuestionOption option = QQuestionOption.questionOption;

        // 페이징 처리한 문항 아이디 리스트 조회
        List<Long> questionIdList = mainQueryFactory.select(question.id)
                .from(question)
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
    public Long findQuestionTotalCount(SurveyReadRequest surveyReadRequest) {
        QQuestion question = QQuestion.question;
        Long totalCount = mainQueryFactory.select(question.id.count())
                .from(question)
                .where(question.survey.id.eq(surveyReadRequest.getSurveyId()))
                .fetchOne();
        return totalCount;

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

    @Override
    public List<SurveyResultQuery> findSurveyResult(SurveyResultReadRequest surveyResultReadRequest) {
        QSurvey s = QSurvey.survey;
        QQuestion q = QQuestion.question;
        QQuestionOption o = QQuestionOption.questionOption;
        QSurveyAnswer sa = QSurveyAnswer.surveyAnswer;
        QQuestion qSub = new QQuestion("qSub");

        // 서브쿼리: 각 문항별 총 선택 수
        Expression<Long> totalCountSubQuery = JPAExpressions
                .select(sa.id.count())
                .from(qSub)
                .join(o).on(o.question.eq(qSub))
                .leftJoin(sa).on(sa.questionOption.eq(o)) // option -> answers
                .where(qSub.id.eq(q.id));

        // 메인 쿼리
        return mainQueryFactory
                .select(Projections.constructor(SurveyResultQuery.class
                        ,s.id
                        ,s.title
                        ,q.id
                        ,q.content
                        ,o.id
                        ,o.content
                        ,sa.id.count().as("selected_count")
                        ,sa.id.count().divide(totalCountSubQuery).multiply(100).as("selected_ratio")
                        ))
                .from(s)
                .join(q).on(q.survey.eq(s))
                .join(o).on(o.question.eq(q))
                .leftJoin(sa).on(sa.questionOption.eq(o))
                .where(s.id.in(surveyResultReadRequest.getSurveyIdList()))
                .groupBy(s.id, s.title, q.id, q.content, o.id, o.content)
                .offset((long) surveyResultReadRequest.getPage() * surveyResultReadRequest.getSize())
                .limit(surveyResultReadRequest.getSize())
                .fetch();
    }
}
