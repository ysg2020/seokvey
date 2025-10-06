package com.ysgpjt.seokvey.support;

import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.Question;
import com.ysgpjt.seokvey.survey.entity.QuestionOption;
import com.ysgpjt.seokvey.survey.entity.Survey;
import com.ysgpjt.seokvey.type.SeletionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestBuilders {

    /* ======================================
     * 1) 설문 + 문항/옵션 그래프 한 번에 생성
     * ====================================== */

    /**
     * 설문 1개와 그 하위 문항/옵션들을 한 번에 구성하는 빌더.
     *
     * 사용 예:
     * SurveyGraphBuilder b = SurveyGraphBuilder.create()
     *     .surveyTitle("설문 A")
     *     .addQuestion("Q1", SeletionType.SINGLE, 1, List.of("A","B"))
     *     .addQuestion("Q2", SeletionType.MULTI,  2, List.of("X","Y","Z"));
     *
     * Survey survey = b.buildSurvey();                 // 부모만 필요할 때
     * List<Question> qs = b.getQuestions();            // 생성된 문항
     * List<QuestionOption> os = b.getOptions();        // 생성된 옵션
     *
     * // 혹은 번들로 한 번에
     * SurveyBundle bundle = b.build();
     */
    public static class SurveyBuilder {
        // 설문 필드
        private Long surveyId;
        private String title = "테스트 설문";
        private String description = "테스트 설명";
        private LocalDateTime startDt = LocalDateTime.now();
        private LocalDateTime endDt = LocalDateTime.now().plusDays(1);
        private Boolean resultGenerated = false;

        // 문항, 옵션 id 1부터 증가
        Long questionId = 1L;
        Long optId = 1L;

        // 내부 조립 상태
        private Survey survey; // lazy ensure
        private final List<Question> questions = new ArrayList<>();
        private final List<QuestionOption> options = new ArrayList<>();

        public static SurveyBuilder create() { return new SurveyBuilder(); }

        private Survey ensureSurvey() {
            if (survey == null) {
                survey = Survey.builder()
                        .id(surveyId)
                        .title(title)
                        .description(description)
                        .startDt(startDt)
                        .endDt(endDt)
                        .resultGenerated(resultGenerated)
                        .build();
            }
            return survey;
        }

        /* ----- 설문 설정 ----- */
        public SurveyBuilder surveyId(Long id) { this.surveyId = id; return this; }
        public SurveyBuilder surveyTitle(String title) { this.title = title; return this; }
        public SurveyBuilder surveyDescription(String description) { this.description = description; return this; }
        public SurveyBuilder surveyStartDt(LocalDateTime startDt) { this.startDt = startDt; return this; }
        public SurveyBuilder surveyEndDt(LocalDateTime endDt) { this.endDt = endDt; return this; }
        public SurveyBuilder surveyResultGenerated(Boolean resultGenerated) { this.resultGenerated = resultGenerated; return this; }

        /* ----- 문항/옵션 추가 ----- */
        public SurveyBuilder addQuestion(String content,
                                              SeletionType selectionType,
                                              int orderNo,
                                              List<String> optionContents) {
            Survey s = ensureSurvey(); // 먼저 설문을 만들어 둠
            Question q = Question.builder()
                    .id(questionId++)
                    .survey(s) // FK 주입 (세터/의도메서드 없이)
                    .content(content)
                    .selectionType(selectionType)
                    .orderNo(orderNo)
                    .build();
            questions.add(q);

            // 옵션 순서 1부터 증가
            int optOrder = 1;
            for (String oc : optionContents) {
                QuestionOption opt = QuestionOption.builder()
                        .id(optId++)
                        .question(q) // FK 주입
                        .content(oc)
                        .orderNo(optOrder++)
                        .build();
                options.add(opt);
            }
            return this;
        }

        /* ----- 결과 조회/생성 ----- */
        public Survey buildSurvey() {
            return ensureSurvey();
        }

        public List<Question> getQuestions() {
            return List.copyOf(questions);
        }

        public List<QuestionOption> getOptions() {
            return List.copyOf(options);
        }

        /** 설문 + 문항 + 옵션을 한 번에 넘겨받고 싶을 때 */
        public SurveyBundle build() {
            return new SurveyBundle(buildSurvey(), getQuestions(), getOptions());
        }
    }

    /* 번들 DTO(테스트에서 편하게 쓰라고 제공) */
    public record SurveyBundle(Survey survey, List<Question> questions, List<QuestionOption> options) {}

    public static class SurveyCreateRequestGraphBuilder {

        private SurveyCreateRequest surveyCreateRequest;
        private List<QuestionCreateRequest> questionCreateRequests = new ArrayList<>();

        public static SurveyCreateRequestGraphBuilder create() {return new SurveyCreateRequestGraphBuilder();}

        public SurveyCreateRequest ensureSurveyCreateRequest() {
            if (surveyCreateRequest == null) {
                surveyCreateRequest = SurveyCreateRequest.builder()
                        .title("<UNK> <UNK>")
                        .description("<UNK> <UNK>")
                        .startDt(LocalDateTime.now())
                        .endDt(LocalDateTime.now().plusDays(1))
                        .questions(questionCreateRequests)
                        .build();

            }
            return surveyCreateRequest;
        }

        public SurveyCreateRequestGraphBuilder addQuestion(String content,
                                                           SeletionType selectionType,
                                                           int orderNo,
                                                           List<String> optionContents) {
            List<QuestionOptionCreateRequest> questionOptionCreateRequests = new ArrayList<>();

            for (String oc : optionContents) {
                QuestionOptionCreateRequest questionOptionCreateRequest = QuestionOptionCreateRequest.builder()
                        .content(oc)
                        .orderNo(orderNo)
                        .build();
                questionOptionCreateRequests.add(questionOptionCreateRequest);
            }

            QuestionCreateRequest questionCreateRequest = QuestionCreateRequest.builder()
                    .content(content)
                    .selectionType(selectionType)
                    .orderNo(orderNo)
                    .options(questionOptionCreateRequests)
                    .build();
            questionCreateRequests.add(questionCreateRequest);
            return this;

        }
        public SurveyCreateRequest build() {
            return ensureSurveyCreateRequest();
        }

        
    }


    public static class SurveyParticipationBuilder {
        private Long surveyId;
        private LocalDateTime surveyDt;
        private final List<SurveyAnswerRequest> answers = new ArrayList<>();

        public static SurveyParticipationBuilder create() { return new SurveyParticipationBuilder(); }

        public SurveyParticipationBuilder surveyId(Long surveyId) {
            this.surveyId = surveyId;
            return this;
        }

        public SurveyParticipationBuilder surveyDt(LocalDateTime surveyDt) {
            this.surveyDt = surveyDt;
            return this;
        }

        public SurveyParticipationBuilder addAnswers(Long questionId, List<Long> questionOptionIds) {
            SurveyAnswerRequest answerRequest = SurveyAnswerRequest.builder()
                    .questionId(questionId)
                    .questionOptionIds(questionOptionIds)
                    .build();
            answers.add(answerRequest);
            return this;
        }
        public SurveyParticipationRequest build() {
            return SurveyParticipationRequest.builder()
                    .surveyId(surveyId)
                    .surveyDt(surveyDt)
                    .answers(answers)
                    .build();
        }

    }

}
