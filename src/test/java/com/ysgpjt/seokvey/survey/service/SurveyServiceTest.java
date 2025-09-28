package com.ysgpjt.seokvey.survey.service;

import com.ysgpjt.seokvey.common.CookieUtil;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.Question;
import com.ysgpjt.seokvey.survey.entity.QuestionOption;
import com.ysgpjt.seokvey.survey.entity.Survey;
import com.ysgpjt.seokvey.survey.entity.SurveyParticipation;
import com.ysgpjt.seokvey.survey.repository.QuestionOptionRepository;
import com.ysgpjt.seokvey.survey.repository.QuestionRepository;
import com.ysgpjt.seokvey.survey.repository.SurveyParticipationRepository;
import com.ysgpjt.seokvey.survey.repository.SurveyRepository;
import com.ysgpjt.seokvey.type.SeletionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("local")
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private SurveyParticipationRepository surveyParticipationRepository;


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
        assertEquals(survey, null);

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
        assertEquals(survey, null);

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
        assertEquals(survey, null);

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
        assertEquals(survey.getId(), savedSurvey.getSurveyId());

    }

    @Test
    @DisplayName("사용자 설문 참여 정상")
    void surveyParticipation(){
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C

        // 옵션 생성
        List<QuestionOptionCreateRequest> questionOptionCreateRequestList1 = List.of(
                createOption("옵션 1", 1),
                createOption("옵션 2", 2)
        );

        List<QuestionOptionCreateRequest> questionOptionCreateRequestList2 = List.of(
                createOption("옵션 A", 1),
                createOption("옵션 B", 2),
                createOption("옵션 C", 3)
        );
        
        // 문항 생성
        QuestionCreateRequest questionCreateRequest1 = createQuestion("첫번째 문항",SeletionType.SINGLE,1,questionOptionCreateRequestList1);
        QuestionCreateRequest questionCreateRequest2 = createQuestion("두번째 문항",SeletionType.MULTIPLE,1,questionOptionCreateRequestList2);
        
        // 설문 생성
        List<QuestionCreateRequest> questionCreateRequestList = List.of(questionCreateRequest1,questionCreateRequest2);
        SurveyCreateRequest surveyCreateRequest = createSurvey("정상 설문", "설명", questionCreateRequestList);
        SurveyResponse savedSurvey = surveyService.createSurvey(surveyCreateRequest);

        // 설문 참여 내용 생성
        // 생성한 설문 조회
        Survey survey = surveyRepository.findById(savedSurvey.getSurveyId()).get();
        List<Question> questionList = questionRepository.findBySurvey(survey);
        List<SurveyAnswerRequest> surveyAnswerRequestList = new ArrayList<>();

        for (Question question : questionList) {
            // 생성한 설문의 문항 옵션 아이디 조회
            List<Long> questionOptionList = questionOptionRepository.findByQuestion(question).stream()
                    .map(QuestionOption::getId).collect(Collectors.toList());

            // 문항이 다중선택인 경우 1개이상 랜덤 선택
            if (question.getSelectionType().equals(SeletionType.MULTIPLE)) {
                Collections.shuffle(questionOptionList);
                int selectedOptionCount = 1 + new Random().nextInt(questionOptionList.size());
                SurveyAnswerRequest surveyAnswer = SurveyAnswerRequest.builder()
                        .questionId(question.getId())
                        .questionOptionIds(questionOptionList.subList(0, selectedOptionCount))
                        .build();
                surveyAnswerRequestList.add(surveyAnswer);

            } else {
                SurveyAnswerRequest surveyAnswer = SurveyAnswerRequest.builder()
                        .questionId(question.getId())
                        .questionOptionIds(Collections.singletonList(questionOptionList.get(new Random().nextInt(questionOptionList.size()))))
                        .build();
                surveyAnswerRequestList.add(surveyAnswer);
            }
        }

        SurveyParticipationRequest request = SurveyParticipationRequest.builder()
                .surveyId(survey.getId())
                .surveyDt(LocalDateTime.now())
                .answers(surveyAnswerRequestList)
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // when
        SurveyResponse participation =
                surveyService.participateSurvey(request,anonToken,ipAddress);

        // then
        SurveyParticipation surveyParticipation = surveyParticipationRepository.findBySurvey(survey).get();
        assertNotNull(participation.getSurveyId());
        assertEquals(participation.getSurveyId(),surveyParticipation.getSurvey().getId());

    }


}