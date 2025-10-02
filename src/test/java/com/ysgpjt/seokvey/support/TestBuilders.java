package com.ysgpjt.seokvey.support;

import com.ysgpjt.seokvey.survey.entity.Question;
import com.ysgpjt.seokvey.survey.entity.QuestionOption;
import com.ysgpjt.seokvey.survey.entity.Survey;
import com.ysgpjt.seokvey.type.SeletionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestBuilders {

    /* ===========================
     * 1) 단일 엔티티 빌더들
     * =========================== */

    /** Survey 단독 생성용 */
    public static class SurveyBuilder {
        private Long id; // 필요 시 테스트에서만 수동 세팅 (운영에선 @GeneratedValue 사용)
        private String title = "테스트 설문";
        private String description = "테스트 설명";
        private LocalDateTime startDt = LocalDateTime.now();
        private LocalDateTime endDt = LocalDateTime.now().plusDays(1);
        private Boolean resultGenerated = false;

        public SurveyBuilder id(Long id) { this.id = id; return this; }
        public SurveyBuilder title(String title) { this.title = title; return this; }
        public SurveyBuilder description(String description) { this.description = description; return this; }
        public SurveyBuilder startDt(LocalDateTime startDt) { this.startDt = startDt; return this; }
        public SurveyBuilder endDt(LocalDateTime endDt) { this.endDt = endDt; return this; }
        public SurveyBuilder resultGenerated(Boolean resultGenerated) { this.resultGenerated = resultGenerated; return this; }

        public Survey build() {
            return Survey.builder()
                    .id(id)
                    .title(title)
                    .description(description)
                    .startDt(startDt)
                    .endDt(endDt)
                    .resultGenerated(resultGenerated)
                    .build();
        }
    }

    /** Question 단독 생성용 (반드시 survey를 넘겨야 함) */
    public static class QuestionBuilder {
        private Long id;
        private Survey survey; // FK (필수)
        private String content = "문항 내용";
        private SeletionType selectionType = SeletionType.SINGLE; // 프로젝트 enum에 맞춰 사용
        private Integer orderNo = 1;

        public QuestionBuilder id(Long id) { this.id = id; return this; }
        public QuestionBuilder survey(Survey survey) { this.survey = survey; return this; }
        public QuestionBuilder content(String content) { this.content = content; return this; }
        public QuestionBuilder selectionType(SeletionType type) { this.selectionType = type; return this; }
        public QuestionBuilder orderNo(Integer orderNo) { this.orderNo = orderNo; return this; }

        public Question build() {
            if (survey == null) {
                throw new IllegalStateException("QuestionBuilder: survey must be provided");
            }
            return Question.builder()
                    .id(id)
                    .survey(survey)
                    .content(content)
                    .selectionType(selectionType)
                    .orderNo(orderNo)
                    .build();
        }
    }

    /** QuestionOption 단독 생성용 (반드시 question을 넘겨야 함) */
    public static class QuestionOptionBuilder {
        private Long id;
        private Question question; // FK (필수)
        private String content = "옵션 내용";
        private Integer orderNo = 1;

        public QuestionOptionBuilder id(Long id) { this.id = id; return this; }
        public QuestionOptionBuilder question(Question question) { this.question = question; return this; }
        public QuestionOptionBuilder content(String content) { this.content = content; return this; }
        public QuestionOptionBuilder orderNo(Integer orderNo) { this.orderNo = orderNo; return this; }

        public QuestionOption build() {
            if (question == null) {
                throw new IllegalStateException("QuestionOptionBuilder: question must be provided");
            }
            return QuestionOption.builder()
                    .id(id)
                    .question(question)
                    .content(content)
                    .orderNo(orderNo)
                    .build();
        }
    }

    /* ======================================
     * 2) 설문 + 문항/옵션 그래프 한 번에 생성
     * ====================================== */

    /**
     * 설문 1개와 그 하위 문항/옵션들을 한 번에 구성하는 고수준 빌더.
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
    public static class SurveyGraphBuilder {
        // 설문 필드
        private Long surveyId;
        private String title = "테스트 설문";
        private String description = "테스트 설명";
        private LocalDateTime startDt = LocalDateTime.now();
        private LocalDateTime endDt = LocalDateTime.now().plusDays(1);
        private Boolean resultGenerated = false;

        // 내부 조립 상태
        private Survey survey; // lazy ensure
        private final List<Question> questions = new ArrayList<>();
        private final List<QuestionOption> options = new ArrayList<>();

        public static SurveyGraphBuilder create() { return new SurveyGraphBuilder(); }

        private Survey ensureSurvey() {
            if (survey == null) {
                survey = new SurveyBuilder()
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
        public SurveyGraphBuilder surveyId(Long id) { this.surveyId = id; return this; }
        public SurveyGraphBuilder surveyTitle(String title) { this.title = title; return this; }
        public SurveyGraphBuilder surveyDescription(String description) { this.description = description; return this; }
        public SurveyGraphBuilder surveyStartDt(LocalDateTime startDt) { this.startDt = startDt; return this; }
        public SurveyGraphBuilder surveyEndDt(LocalDateTime endDt) { this.endDt = endDt; return this; }
        public SurveyGraphBuilder surveyResultGenerated(Boolean resultGenerated) { this.resultGenerated = resultGenerated; return this; }

        /* ----- 문항/옵션 추가 ----- */
        public SurveyGraphBuilder addQuestion(String content,
                                              SeletionType selectionType,
                                              int orderNo,
                                              List<String> optionContents) {
            Survey s = ensureSurvey(); // 먼저 설문을 만들어 둠
            Question q = new QuestionBuilder()
                    .survey(s) // FK 주입 (세터/의도메서드 없이)
                    .content(content)
                    .selectionType(selectionType)
                    .orderNo(orderNo)
                    .build();
            questions.add(q);

            int optOrder = 1;
            for (String oc : optionContents) {
                QuestionOption opt = new QuestionOptionBuilder()
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
}
