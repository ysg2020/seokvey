package com.ysgpjt.seokvey.survey.repository.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ysgpjt.seokvey.common.dto.PagedResponse;
import com.ysgpjt.seokvey.common.exception.SeokveyException;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.*;
import com.ysgpjt.seokvey.survey.repository.SurveyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyQueryRepositoryImpl implements SurveyQueryRepository {

    private final JPAQueryFactory mainQueryFactory;

    @Override
    public PagedResponse<SurveyResponse> findAllSurvey(SurveyReadRequest surveyReadRequest) {
        QSurvey survey = QSurvey.survey;
        int page = surveyReadRequest.getPage();
        int size = surveyReadRequest.getSize();
        List<SurveyResponse> items = mainQueryFactory.select(Projections.bean(
                        SurveyResponse.class,
                        survey.id.as("surveyId"),
                        survey.title,
                        survey.description,
                        survey.startDt,
                        survey.endDt
                ))
                .from(survey)
                .where(surveyPredicates(surveyReadRequest, survey))
                .offset((long) page * size)
                .limit(size)
                .fetch();

        int totalCount;
        int totalPages;
        Long tc = mainQueryFactory
                .select(survey.id.count())
                .from(survey)
                .where(surveyPredicates(surveyReadRequest, survey))
                .fetchOne();
        totalCount = Math.toIntExact(tc == null ? 0L : tc);
        totalPages = totalCount == 0 ? 0 : ((totalCount + size - 1) / size);
        return new PagedResponse<>(items,page,size,totalCount,totalPages);
    }

    // 조회조건
    private BooleanExpression[] surveyPredicates(SurveyReadRequest req, QSurvey s) {
        List<BooleanExpression> list = new ArrayList<>();
        /*if (req.getTitle() != null && !req.getTitle().isBlank()) {
            list.add(s.title.containsIgnoreCase(req.getTitle()));
        }
        if (req.getFrom() != null) {
            list.add(s.startDt.goe(req.getFrom()));
        }
        if (req.getTo() != null) {
            list.add(s.endDt.loe(req.getTo()));
        }*/
        // 조건 계속 추가...
        return list.toArray(BooleanExpression[]::new);
    }


    @Override
    public Survey findSurvey(SurveyReadRequest surveyReadRequest) {
        QSurvey survey = QSurvey.survey;
        return mainQueryFactory.selectFrom(survey)
                .where(survey.id.eq(surveyReadRequest.getSurveyId()))
                .fetchOne();
    }

    @Override
    public PagedResponse<QuestionQuery> findQuestion(SurveyReadRequest surveyReadRequest) {
        QQuestion question = QQuestion.question;
        QQuestionOption option = QQuestionOption.questionOption;
        int page = surveyReadRequest.getPage();
        int size = surveyReadRequest.getSize();

        // 페이징 처리한 문항 아이디 리스트 조회
        List<Long> questionIdList = mainQueryFactory.select(question.id)
                .from(question)
                .where(question.survey.id.eq(surveyReadRequest.getSurveyId()))
                .offset((long) surveyReadRequest.getPage() * surveyReadRequest.getSize())
                .limit(surveyReadRequest.getSize())
                .fetch();

        // 총 카운트와 페이지수 조회
        Long tc = mainQueryFactory.select(question.id.count())
                .from(question)
                .where(question.survey.id.eq(surveyReadRequest.getSurveyId()))
                .fetchOne();

        int totalCount;
        int totalPages;

        totalCount = Math.toIntExact(tc == null ? 0L : tc);
        totalPages = totalCount == 0 ? 0 : ((totalCount + size - 1) / size);

        List<QuestionQuery> items = mainQueryFactory.select(Projections.constructor(QuestionQuery.class
                        , question.id
                        , question.content
                        , question.selectionType
                        , question.orderNo
                        , option.id
                        , option.content
                        , option.orderNo
                ))
                .from(question)
                .leftJoin(option).on(option.question.eq(question))
                .where(question.id.in(questionIdList))
                .orderBy(question.orderNo.asc(), option.orderNo.asc())
                .fetch();


        return new PagedResponse<>(items,page,size,totalCount,totalPages);
    }

    @Override
    public PagedResponse<SurveyParticipationQuery> findSurveyParticipation(SurveyParticipationReadRequest surveyParticipationReadRequest) {
        QSurveyParticipation surveyParticipation = QSurveyParticipation.surveyParticipation;
        QSurveyAnswer surveyAnswer = QSurveyAnswer.surveyAnswer;
        int page = surveyParticipationReadRequest.getPage();
        int size = surveyParticipationReadRequest.getSize();

        // 총 카운트와 페이지수 조회
        Long tc = mainQueryFactory.select(surveyParticipation.id.count())
                .from(surveyParticipation)
                .where(surveyParticipation.userId.eq(surveyParticipationReadRequest.getUserId()))
                .fetchOne();

        int totalCount;
        int totalPages;

        totalCount = Math.toIntExact(tc == null ? 0L : tc);
        totalPages = totalCount == 0 ? 0 : ((totalCount + size - 1) / size);

        // 설문 참여 id 리스트 조회 (페이징 처리)
        List<Long> surveyParticipationIdList = mainQueryFactory.select(surveyParticipation.id)
                .from(surveyParticipation)
                .where(surveyParticipation.userId.eq(surveyParticipationReadRequest.getUserId()))
                .offset((long) surveyParticipationReadRequest.getPage() * surveyParticipationReadRequest.getSize())
                .limit(surveyParticipationReadRequest.getSize())
                .fetch();

        List<SurveyParticipationQuery> items = mainQueryFactory.select(Projections.constructor(SurveyParticipationQuery.class
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
                .where(surveyParticipation.userId.eq(surveyParticipationReadRequest.getUserId()).and(surveyParticipation.id.in(surveyParticipationIdList)))
                .fetch();

        return new PagedResponse<>(items,page,size,totalCount,totalPages);
    }

    @Override
    public List<SurveyResultQuery> findLiveSurveyResult(SurveyResultReadRequest surveyResultReadRequest) {
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

        List<SurveyResultQuery> result = List.of();

        // 결과 조회인 경우 (단건 조회)
        if (surveyResultReadRequest.getSurveyId() != null) {
            result = mainQueryFactory
                    .select(Projections.constructor(SurveyResultQuery.class
                            , s.id
                            , s.title
                            , q.id
                            , q.content
                            , o.id
                            , o.content
                            , sa.id.count().as("selected_count")
                            , sa.id.count().divide(totalCountSubQuery).multiply(100).as("selected_ratio")
                    ))
                    .from(s)
                    .join(q).on(q.survey.eq(s))
                    .join(o).on(o.question.eq(q))
                    .leftJoin(sa).on(sa.questionOption.eq(o))
                    .where(s.id.eq(surveyResultReadRequest.getSurveyId()))
                    .groupBy(s.id, s.title, q.id, q.content, o.id, o.content)
                    .fetch();

        // 결과 생성을 위한 조회인 경우 (다건 조회)
        } else if (surveyResultReadRequest.getSurveyIdList() != null ) {
            result = mainQueryFactory
                    .select(Projections.constructor(SurveyResultQuery.class
                            , s.id
                            , s.title
                            , q.id
                            , q.content
                            , o.id
                            , o.content
                            , sa.id.count().as("selected_count")
                            , sa.id.count().divide(totalCountSubQuery).multiply(100).as("selected_ratio")
                    ))
                    .from(s)
                    .join(q).on(q.survey.eq(s))
                    .join(o).on(o.question.eq(q))
                    .leftJoin(sa).on(sa.questionOption.eq(o))
                    .where(s.id.in(surveyResultReadRequest.getSurveyIdList()))
                    .groupBy(s.id, s.title, q.id, q.content, o.id, o.content)
                    .fetch();

        }

        return result;
    }

    @Override
    public List<SurveyResultQuery> findSurveyResult(SurveyResultReadRequest surveyResultReadRequest) {
        QSurveyResult sr = QSurveyResult.surveyResult;
        QQuestionOptionResult qr = QQuestionOptionResult.questionOptionResult;

        // 메인 쿼리
        List<SurveyResultQuery> result = mainQueryFactory
                .select(Projections.constructor(SurveyResultQuery.class
                        , sr.survey.id
                        , sr.title
                        , qr.question.id
                        , qr.questionContent
                        , qr.questionOption.id
                        , qr.questionOptionContent
                        , qr.selectedCount
                        , qr.selectedRatio
                ))
                .from(sr)
                .join(qr).on(qr.surveyResult.eq(sr))
                .where(sr.id.eq(surveyResultReadRequest.getSurveyId()))
                .groupBy(sr.survey.id, sr.title, qr.question.id, qr.questionContent, qr.questionOption.id, qr.questionOptionContent,qr.selectedCount, qr.selectedRatio)
                .fetch();
        return result;
    }
}
