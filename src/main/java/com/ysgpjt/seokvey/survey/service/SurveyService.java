package com.ysgpjt.seokvey.survey.service;

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

    public List<SurveyQuery> getSurvey(Long surveyId) {
        List<SurveyQuery> survey = surveyQueryRepository.findSurvey(surveyId);
        return survey;

    }
}
