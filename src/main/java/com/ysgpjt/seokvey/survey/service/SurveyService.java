package com.ysgpjt.seokvey.survey.service;

import com.ysgpjt.seokvey.common.HierarchyMapper;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.Question;
import com.ysgpjt.seokvey.survey.entity.QuestionOption;
import com.ysgpjt.seokvey.survey.entity.Survey;
import com.ysgpjt.seokvey.survey.repository.QuestionOptionRepository;
import com.ysgpjt.seokvey.survey.repository.QuestionRepository;
import com.ysgpjt.seokvey.survey.repository.SurveyQueryRepository;
import com.ysgpjt.seokvey.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final SurveyQueryRepository surveyQueryRepository;


    public SurveyResponse createSurvey(SurveyCreateRequest surveyCreateRequest) {
        // 설문 문항이 없는 경우
        if(surveyCreateRequest.getQuestions() == null) {
            return null;
        }

        // 문항 옵션은 무조건 2개 이상이어야함
        List<QuestionCreateRequest> questions = surveyCreateRequest.getQuestions();
        for(QuestionCreateRequest question : questions) {
            List<QuestionOptionCreateRequest> options = question.getOptions();

            // 문항 옵션이 없거나 2개보다 적은경우
            if(options == null || options.size() < 2) {
                return null;
            }
        }

        // 설문 생성 및 저장
        Survey survey = Survey.builder()
                .title(surveyCreateRequest.getTitle())
                .description(surveyCreateRequest.getDescription())
                .startDt(surveyCreateRequest.getStartDt())
                .endDt(surveyCreateRequest.getEndDt())
                .build();
        surveyRepository.save(survey);

        // 문항 & 문항 옵션 생성
        List<Question> questionList= new ArrayList<>();
        List<QuestionOption> questionOptionList= new ArrayList<>();

        // 문항 만큼 반복
        for(QuestionCreateRequest questionCreate : questions) {
            Question question = Question.builder()
                    .survey(survey)
                    .content(questionCreate.getContent())
                    .selectionType(questionCreate.getSelectionType())
                    .orderNo(questionCreate.getOrderNo())
                    .build();
            questionList.add(question);
            // 문항 옵션만큼 반복
            for(QuestionOptionCreateRequest optionCreate : questionCreate.getOptions()) {
                QuestionOption questionOption = QuestionOption.builder()
                        .question(question)
                        .content(optionCreate.getContent())
                        .orderNo(optionCreate.getOrderNo())
                        .build();
                questionOptionList.add(questionOption);
            }

        }
        // 문항 & 문항 옵션 저장
        questionRepository.saveAll(questionList);
        questionOptionRepository.saveAll(questionOptionList);

        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .build();


    }

    public List<SurveyResponse> getAllSurvey(SurveyReadRequest surveyReadRequest) {
        List<Survey> surveyList = surveyQueryRepository.findAllSurvey(surveyReadRequest);
        // Survey -> SurveyResponse 변환
        return surveyList.stream()
                .map(SurveyResponse::fromSurvey)
                .collect(Collectors.toList());
    }

    public List<SurveyResponse> getSurveyDetail(SurveyReadRequest surveyReadRequest) {
        List<SurveyQuery> getSurveyQueryList = surveyQueryRepository.findSurvey(surveyReadRequest);

        // Flat List -> 계층 구조로 변환
        List<SurveyResponse> result = HierarchyMapper.toHierarchy(
                // Param 1 : 평탄화된 데이터 리스트
                getSurveyQueryList,
                // Param 2 : 부모 ID 추출 함수
                SurveyQuery::getSurveyId,
                // Param 3 : 부모 객체 생성 함수
                p -> SurveyResponse.builder()
                        .surveyId(p.getSurveyId())
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .startDt(p.getStartDt())
                        .endDt(p.getEndDt())
                        .questions(new ArrayList<>())
                        .build(),
                // Param 4 : 부모에 자식 붙이는 함수
                (survey, row) -> {
                    // Survey 안에 Question 계층 생성
                    List<QuestionResponse> questions = survey.getQuestions();

                    // 이미 존재하는 Question 확인
                    QuestionResponse question = questions.stream()
                            .filter(q -> q.getQuestionId().equals(row.getQuestionId()))
                            .findFirst()
                            .orElseGet(() -> {
                                QuestionResponse q = QuestionResponse.builder()
                                        .questionId(row.getQuestionId())
                                        .content(row.getQuestionContent())
                                        .selectionType(row.getSelectionType())
                                        .orderNo(row.getQuestionOrderNo())
                                        .options(new ArrayList<>())
                                        .build();
                                questions.add(q);
                                return q;
                            });

                    // Question 안에 Option 추가
                    if (row.getOptionId() != null) {
                        question.addOption(QuestionOptionResponse.builder()
                                .questionOptionId(row.getOptionId())
                                .content(row.getOptionContent())
                                .orderNo(row.getOptionOrderNo())
                                .build());
                    }
                }
        );

        return result;

    }

    public List<QuestionResponse> getQuestion(SurveyReadRequest surveyReadRequest) {
        List<QuestionQuery> getQuestionQueryList = surveyQueryRepository.findQuestion(surveyReadRequest);
        // Flat List -> 계층 구조로 변환
        List<QuestionResponse> result = HierarchyMapper.toHierarchy(
                // Param 1 : 평탄화된 데이터 리스트
                getQuestionQueryList,
                // Param 2 : 부모 ID 추출 함수
                QuestionQuery::getQuestionId,
                // Param 3 : 부모 객체 생성 함수
                p -> QuestionResponse.builder()
                        .questionId(p.getQuestionId())
                        .content(p.getQuestionContent())
                        .selectionType(p.getSelectionType())
                        .orderNo(p.getQuestionOrderNo())
                        .options(new ArrayList<>())
                        .build(),
                // Param 4 : 부모에 자식 붙이는 함수
                (question, row) -> {
                    // Question 안에 Option 추가
                    if (row.getOptionId() != null) {
                        question.addOption(QuestionOptionResponse.builder()
                                .questionOptionId(row.getOptionId())
                                .content(row.getOptionContent())
                                .orderNo(row.getOptionOrderNo())
                                .build());
                    }
                });

        return result;

    }
}
