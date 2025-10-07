package com.ysgpjt.seokvey.survey.service.unit;

import com.ysgpjt.seokvey.common.exception.SeokveyException;
import com.ysgpjt.seokvey.support.TestBuilders;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.Question;
import com.ysgpjt.seokvey.survey.entity.QuestionOption;
import com.ysgpjt.seokvey.survey.entity.Survey;
import com.ysgpjt.seokvey.survey.entity.SurveyParticipation;
import com.ysgpjt.seokvey.survey.repository.*;
import com.ysgpjt.seokvey.survey.service.SurveyService;
import com.ysgpjt.seokvey.type.ErrorType;
import com.ysgpjt.seokvey.type.SeletionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionOptionRepository questionOptionRepository;

    @Mock
    private SurveyParticipationRepository surveyParticipationRepository;

    @Mock
    private SurveyAnswerRepository surveyAnswerRepository;

    @InjectMocks
    private SurveyService surveyService;


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
        // 설문 테스트 데이터
        // 문항 옵션이 없는 경우
        SurveyCreateRequest surveyCreateRequest = TestBuilders.SurveyCreateRequestGraphBuilder.create()
                .build();

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.createSurvey(surveyCreateRequest));

        // then
        assertEquals(ErrorType.NOT_ENOUGH_QUESTION, seokveyException.getErrorType());

    }
    @DisplayName("문항 옵션이 없는 설문 생성 불가")
    @Test
    void createSurveyNoQuestionOption() {
        // given
        // 설문 테스트 데이터
        // 문항 옵션이 없는 경우
        SurveyCreateRequest surveyCreateRequest = TestBuilders.SurveyCreateRequestGraphBuilder.create()
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1","옵션 2"))
                .addQuestion("두번째 문항", SeletionType.SINGLE, 2, new ArrayList<>())
                .build();

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.createSurvey(surveyCreateRequest));

        // then
        assertEquals(ErrorType.NOT_ENOUGH_OPTION, seokveyException.getErrorType());

    }

    @Test
    @DisplayName("문항 옵션이 1개인 설문 생성 불가")
    void createSurveyOneQuestionOption() {
        // given
        // 설문 테스트 데이터
        // 문항 옵션 1개인 문항
        SurveyCreateRequest surveyCreateRequest = TestBuilders.SurveyCreateRequestGraphBuilder.create()
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1","옵션 2"))
                .addQuestion("두번째 문항", SeletionType.SINGLE, 2, List.of("옵션 1"))
                .build();

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.createSurvey(surveyCreateRequest));

        // then
        assertEquals(ErrorType.NOT_ENOUGH_OPTION, seokveyException.getErrorType());

    }

    @Test
    @DisplayName("정상 설문 생성")
    void createSurvey() {
        // given
        SurveyCreateRequest surveyCreateRequest = TestBuilders.SurveyCreateRequestGraphBuilder.create()
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1","옵션 2"))
                .addQuestion("두번째 문항", SeletionType.SINGLE, 2, List.of("옵션 1","옵션 2"))
                .build();

        // when
        SurveyResponse surveyResponse = assertDoesNotThrow(() -> surveyService.createSurvey(surveyCreateRequest));

        // then
        // save에 전달된 값 검증
        ArgumentCaptor<Survey> captor = ArgumentCaptor.forClass(Survey.class);
        verify(surveyRepository, times(1)).save(captor.capture());
        Survey saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(surveyResponse.getSurveyId());

    }

    @Test
    @DisplayName("사용자 설문 참여 정상")
    void surveyParticipation(){
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();

        // 설문 참여 데이터
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), Collections.singletonList(surveyBundle.options().get(0).getId()))
                .addAnswers(surveyBundle.questions().get(1).getId(), List.of(surveyBundle.options().get(2).getId(), surveyBundle.options().get(3).getId()))
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(questionRepository.findBySurvey(any())).willReturn(surveyBundle.questions());
        given(questionOptionRepository.findAllById(any())).willReturn(surveyBundle.options());

        // when
        SurveyResponse surveyResponse = assertDoesNotThrow(() -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        // save에 전달된 값 검증
        ArgumentCaptor<SurveyParticipation> captor = ArgumentCaptor.forClass(SurveyParticipation.class);
        verify(surveyParticipationRepository, times(1)).save(captor.capture());
        SurveyParticipation saved = captor.getValue();
        assertThat(saved.getSurvey().getId()).isEqualTo(surveyResponse.getSurveyId());

    }

    @Test
    @DisplayName("응답하지 않는 문항이 있으면 설문 참여 불가")
    void surveyParticipationNotEnoughQuestion() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();

        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), Collections.singletonList(surveyBundle.options().get(0).getId()))
                // 두번쨰 문항 응답을 하지 않은 경우
                //.addAnswers~
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(questionRepository.findBySurvey(any())).willReturn(surveyBundle.questions());

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.NO_RESPONSE_QUESTION, seokveyException.getErrorType());


    }

    @Test
    @DisplayName("유효하지 않은 문항이 있으면 설문 참여 불가")
    void surveyParticipationInvalidQuestion() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();
        // questionId의 최댓값
        Long maxQuestionId = surveyBundle.questions().stream().mapToLong(q -> q.getId()).max().orElse(0L);

        // 존재하지 않는 questionId
        Long invalidQuestionId = maxQuestionId + 100L;

        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), Collections.singletonList(surveyBundle.options().get(0).getId()))
                // 유효하지 않은 문항
                .addAnswers(invalidQuestionId, List.of(1L, 2L)) // 옵션은 어떤 값이든 무방 (문항 단계에서 걸러짐)
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(questionRepository.findBySurvey(any())).willReturn(surveyBundle.questions());

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.INVALID_QUESTION, seokveyException.getErrorType());
    }

    @Test
    @DisplayName("최소 1개의 옵션을 선택하지 않으면 설문 참여 불가")
    void surveyParticipationNotChooseOption() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();

        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), Collections.singletonList(surveyBundle.options().get(0).getId()))
                // 옵션 선택 X
                .addAnswers(surveyBundle.questions().get(1).getId(), new ArrayList<>())
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(questionRepository.findBySurvey(any())).willReturn(surveyBundle.questions());

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.NOT_CHOOSE_OPTION, seokveyException.getErrorType());
    }

    @Test
    @DisplayName("단일 선택 문항은 옵션 2개이상 선택 불가")
    void surveyParticipationOneChooseOption() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();

        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                // 옵션 2개 선택
                .addAnswers(surveyBundle.questions().get(0).getId(), List.of(surveyBundle.options().get(0).getId(),surveyBundle.options().get(1).getId()))
                .addAnswers(surveyBundle.questions().get(1).getId(), Collections.singletonList(surveyBundle.options().get(2).getId()))
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(questionRepository.findBySurvey(any())).willReturn(surveyBundle.questions());
        given(questionOptionRepository.findAllById(any())).willReturn(surveyBundle.options());

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.ONE_CHOOSE_OPTION, seokveyException.getErrorType());
    }

    @Test
    @DisplayName("유효하지 않은 옵션이 있는 경우 설문 참여 불가")
    void surveyParticipationInvalidOption() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();


        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), List.of(surveyBundle.options().get(0).getId()))
                // 유효하지 않는 옵션 선택 (첫번째 문항의 첫번째 옵션 아이디)
                .addAnswers(surveyBundle.questions().get(1).getId(), List.of(surveyBundle.options().get(0).getId()))
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(questionRepository.findBySurvey(any())).willReturn(surveyBundle.questions());
        given(questionOptionRepository.findAllById(any())).willReturn(surveyBundle.options());



        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.INVALID_OPTION, seokveyException.getErrorType());
    }

    @Test
    @DisplayName("이미 설문 참여한 비회원 토큰")
    void surveyParticipationAlreadyAnonymousToken() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();


        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), List.of(surveyBundle.options().get(0).getId()))
                .addAnswers(surveyBundle.questions().get(1).getId(), List.of(surveyBundle.options().get(3).getId()))
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(surveyParticipationRepository.findBySurveyAndAnonymousToken(any(),any())).willReturn(Optional.ofNullable(SurveyParticipation.builder().build()));

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.ALREADY_SURVEY_ANONYMOUS_TOKEN, seokveyException.getErrorType());
    }

    @Test
    @DisplayName("이미 설문 참여한 ip 주소")
    void surveyParticipationAlreadyIpAddress() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();


        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), List.of(surveyBundle.options().get(0).getId()))
                .addAnswers(surveyBundle.questions().get(1).getId(), List.of(surveyBundle.options().get(3).getId()))
                .build();

        String anonToken = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(surveyParticipationRepository.findBySurveyAndAnonymousToken(any(),any())).willReturn(Optional.empty());
        given(surveyParticipationRepository.findBySurveyAndIpAddress(any(),any())).willReturn(Optional.ofNullable(SurveyParticipation.builder().build()));

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.ALREADY_SURVEY_IPADDRESS, seokveyException.getErrorType());
    }

    @Test
    @DisplayName("이미 설문 참여한 사용자")
    void surveyParticipationAlreadyConsumer() {
        // given
        // 설문 테스트 데이터
        // 정상 설문
        // 첫번째 문항 : 옵션 1, 옵션 2
        // 두번쨰 문항 : 옵션 A, 옵션 B, 옵션 C
        TestBuilders.SurveyBundle surveyBundle = TestBuilders.SurveyBuilder.create()
                .surveyId(1L)
                .surveyTitle("정상 설문")
                .surveyDescription("설명")
                .addQuestion("첫번째 문항", SeletionType.SINGLE, 1, List.of("옵션 1", "옵션 2"))
                .addQuestion("두번째 문항", SeletionType.MULTIPLE, 2, List.of("옵션 A", "옵션 B", "옵션 C"))
                .build();


        // 설문 참여 데이터 생성
        SurveyParticipationRequest request = TestBuilders.SurveyParticipationBuilder.create()
                .surveyId(surveyBundle.survey().getId())
                .addAnswers(surveyBundle.questions().get(0).getId(), List.of(surveyBundle.options().get(0).getId()))
                .addAnswers(surveyBundle.questions().get(1).getId(), List.of(surveyBundle.options().get(3).getId()))
                .build();

        String anonToken = null;
        String ipAddress = "127.0.0.1";

        // db 조회 모킹
        given(surveyRepository.findById(any())).willReturn(Optional.of(surveyBundle.survey()));
        given(surveyParticipationRepository.findBySurveyAndUserId(any(),any())).willReturn(Optional.ofNullable(SurveyParticipation.builder().build()));

        // when
        SeokveyException seokveyException = assertThrows(SeokveyException.class, () -> surveyService.participateSurvey(request, anonToken, ipAddress));

        // then
        assertEquals(ErrorType.ALREADY_SURVEY_CONSUMER, seokveyException.getErrorType());
    }


}