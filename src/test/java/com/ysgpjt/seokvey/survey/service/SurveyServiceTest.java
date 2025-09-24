package com.ysgpjt.seokvey.survey.service;

import com.ysgpjt.seokvey.survey.dto.QuestionCreateRequest;
import com.ysgpjt.seokvey.survey.dto.QuestionOptionCreateRequest;
import com.ysgpjt.seokvey.survey.dto.SurveyCreateRequest;
import com.ysgpjt.seokvey.survey.dto.SurveyResponse;
import com.ysgpjt.seokvey.survey.entity.Survey;
import com.ysgpjt.seokvey.survey.repository.SurveyRepository;
import com.ysgpjt.seokvey.type.SeletionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyRepository surveyRepository;


    // --- 테스트 유틸 메서드 ---
    private SurveyCreateRequest createSurvey(String title, String description, List<QuestionCreateRequest> questions) {
        return SurveyCreateRequest.builder()
                .title(title)
                .description(description)
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusDays(1))
                .questions(questions)
                .build();
    }

    private QuestionCreateRequest createQuestion(String content, SeletionType type, int orderNo, List<QuestionOptionCreateRequest> options) {
        return QuestionCreateRequest.builder()
                .content(content)
                .selectionType(type)
                .orderNo(orderNo)
                .options(options)
                .build();
    }

    private QuestionOptionCreateRequest createOption(String content, int orderNo) {
        return QuestionOptionCreateRequest.builder()
                .content(content)
                .orderNo(orderNo)
                .build();
    }


    @Test
    @DisplayName("문항이 없는 설문 생성 불가")
    void createSurveyNoQuestion() {
        // given
        SurveyCreateRequest surveyCreateRequest = createSurvey("문항이 없는 설문", "설명", null);

        // when
        SurveyResponse survey = surveyService.createSurvey(surveyCreateRequest);

        // then
        Assertions.assertEquals(survey, null);

    }
    @DisplayName("문항 옵션이 없는 설문 생성 불가")
    @Test
    void createSurveyNoQuestionOption() {
        // given
        QuestionCreateRequest question = createQuestion("문항 내용",SeletionType.SINGLE,1,null);
        SurveyCreateRequest surveyCreateRequest = createSurvey("문항 옵션이 없는 설문", "설명", Collections.singletonList(question));

        // when
        SurveyResponse survey = surveyService.createSurvey(surveyCreateRequest);

        // then
        Assertions.assertEquals(survey, null);

    }

    @Test
    @DisplayName("문항 옵션이 1개인 설문 생성 불가")
    void createSurveyOneQuestionOption() {
        // given
        QuestionOptionCreateRequest questionOptionCreateRequest = createOption("문항 옵션 내용", 1);
        QuestionCreateRequest question = createQuestion("문항 내용",SeletionType.SINGLE,1, Collections.singletonList(questionOptionCreateRequest));
        SurveyCreateRequest surveyCreateRequest = createSurvey("문항 옵션이 1개인 설문", "설명", Collections.singletonList(question));

        // when
        SurveyResponse survey = surveyService.createSurvey(surveyCreateRequest);

        // then
        Assertions.assertEquals(survey, null);

    }

    @Test
    @DisplayName("정상 설문 생성")
    void createSurvey() {
        // given
        List<QuestionOptionCreateRequest> questionOptionCreateRequestList = List.of(
                createOption("문항 옵션 내용", 1),
                createOption("문항 옵션 내용2", 2)
        );
        QuestionCreateRequest questionCreateRequest = createQuestion("문항 내용",SeletionType.SINGLE,1,questionOptionCreateRequestList);
        SurveyCreateRequest surveyCreateRequest = createSurvey("정상 설문", "설명", Collections.singletonList(questionCreateRequest));

        // when
        SurveyResponse savedSurvey = surveyService.createSurvey(surveyCreateRequest);

        // then
        Survey survey = surveyRepository.findById(savedSurvey.getSurveyId()).get();
        Assertions.assertEquals(survey.getId(), savedSurvey.getSurveyId());

    }


}