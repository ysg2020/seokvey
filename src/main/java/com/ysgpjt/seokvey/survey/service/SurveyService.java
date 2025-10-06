package com.ysgpjt.seokvey.survey.service;

import com.ysgpjt.seokvey.common.HierarchyMapper;
import com.ysgpjt.seokvey.common.SecurityUtil;
import com.ysgpjt.seokvey.common.dto.PagedResponse;
import com.ysgpjt.seokvey.survey.dto.*;
import com.ysgpjt.seokvey.survey.entity.*;
import com.ysgpjt.seokvey.survey.repository.*;
import com.ysgpjt.seokvey.type.SeletionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final SurveyQueryRepository surveyQueryRepository;
    private final SurveyParticipationRepository surveyParticipationRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;


    @Transactional
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
                log.warn("문항 옵션은 2개 이상이어야합니다");
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

    public PagedResponse<SurveyResponse> getAllSurvey(SurveyReadRequest surveyReadRequest) {
        PagedResponse<SurveyResponse> allSurvey = surveyQueryRepository.findAllSurvey(surveyReadRequest);
        return allSurvey;

    }

    public SurveyResponse getSurvey(SurveyReadRequest surveyReadRequest) {
        Survey survey = surveyQueryRepository.findSurvey(surveyReadRequest);
        PagedResponse<QuestionQuery> getQuestionQueryList = surveyQueryRepository.findQuestion(surveyReadRequest);

        // 문항과 문항 옵션 계층 구조 변환
        // Flat List -> 계층 구조로 변환
        List<QuestionResponse> questions = HierarchyMapper.toHierarchy(
                // Param 1 : 평탄화된 데이터 리스트
                getQuestionQueryList.getItems(),
                // Param 2 : 부모 ID 추출 함수
                QuestionQuery::getQuestionId,
                // Param 3 : 부모 객체 생성 함수
                p -> QuestionResponse.builder()
                        .questionId(p.getQuestionId())
                        .content(p.getQuestionContent())
                        .selectionType(p.getSelectionType())
                        .orderNo(p.getQuestionOrderNo())
                        .options(new ArrayList<>())
                        .questionTotalCount(getQuestionQueryList.getTotalCount())
                        .questionTotalPages(getQuestionQueryList.getTotalPages())
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

        // 설문 계층 구조 변환
        SurveyResponse result = SurveyResponse.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .startDt(survey.getStartDt())
                .endDt(survey.getEndDt())
                .questions(questions)
                .build();
        return result;

    }

    public List<QuestionResponse> getQuestion(SurveyReadRequest surveyReadRequest) {
        PagedResponse<QuestionQuery> getQuestionQueryList = surveyQueryRepository.findQuestion(surveyReadRequest);
        // Flat List -> 계층 구조로 변환
        List<QuestionResponse> result = HierarchyMapper.toHierarchy(
                // Param 1 : 평탄화된 데이터 리스트
                getQuestionQueryList.getItems(),
                // Param 2 : 부모 ID 추출 함수
                QuestionQuery::getQuestionId,
                // Param 3 : 부모 객체 생성 함수
                p -> QuestionResponse.builder()
                        .questionId(p.getQuestionId())
                        .content(p.getQuestionContent())
                        .selectionType(p.getSelectionType())
                        .orderNo(p.getQuestionOrderNo())
                        .options(new ArrayList<>())
                        .questionTotalCount(getQuestionQueryList.getTotalCount())
                        .questionTotalPages(getQuestionQueryList.getTotalPages())
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

    @Transactional
    public SurveyResponse updateSurvey(SurveyUpdateRequest surveyUpdateRequest) {
        // 1. Survey 수정
        // 수정할 설문 조회
        Survey survey = surveyRepository.findById(surveyUpdateRequest.getSurveyId()).orElse(null);

        Optional<SurveyParticipation> surveyParticipation = surveyParticipationRepository.findBySurvey(survey);
        if(surveyParticipation.isPresent()) {
            log.warn("설문에 참여한 사람이 있습니다. surveyParticipationId : {} ,userId : {} ,anonymousToken : {}"
                    , surveyParticipation.get().getId()
                    ,surveyParticipation.get().getUserId()
                    ,surveyParticipation.get().getAnonymousToken());
            return null;
        }

        survey.modify(surveyUpdateRequest);

        // 2. Question 수정 (없으면 추가 있으면 수정)
        // 문항만큼 반복
        for (QuestionUpdateRequest questionUpdateRequest : surveyUpdateRequest.getQuestions()) {
            Question question;

            if (questionUpdateRequest.getQuestionId() == null) {
                // 문항 새로 추가
                question = Question.builder()
                        .survey(survey)
                        .content(questionUpdateRequest.getContent())
                        .selectionType(questionUpdateRequest.getSelectionType())
                        .orderNo(questionUpdateRequest.getOrderNo())
                        .build();
                questionRepository.save(question);
            } else {
                // 수정할 문항 조회
                question = questionRepository.findById(questionUpdateRequest.getQuestionId()).orElse(null);
                // 문항 수정
                question.modify(questionUpdateRequest);
            }

            // Option 삭제 : DB에 있으나 요청에 없는경우 삭제
            Set<Long> requestOptionIds = questionUpdateRequest.getOptions().stream()
                    .map(QuestionOptionUpdateRequest::getQuestionOptionId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // DB에 존재하는 Option 목록
            List<QuestionOption> existingOptions = questionOptionRepository.findByQuestion(question);

            // 요청에 없는 Option 삭제
            for (QuestionOption existing : existingOptions) {
                if (!requestOptionIds.contains(existing.getId())) {
                    questionOptionRepository.delete(existing);
                }
            }

            // 3. Option 수정 (없으면 추가 있으면 수정)
            // 문항 옵션수만큼 반복
            for (QuestionOptionUpdateRequest questionOptionUpdateRequest : questionUpdateRequest.getOptions()) {
                if (questionOptionUpdateRequest.getQuestionOptionId() == null) {
                    // 옵션 새로 추가
                    QuestionOption newQuestionOption = QuestionOption.builder()
                            .question(question)
                            .content(questionOptionUpdateRequest.getContent())
                            .orderNo(questionOptionUpdateRequest.getOrderNo())
                            .build();
                    questionOptionRepository.save(newQuestionOption);
                } else {
                    // 수정할 옵션 조회
                    QuestionOption questionOption = questionOptionRepository.findById(questionOptionUpdateRequest.getQuestionOptionId()).orElse(null);
                    // 옵션 수정
                    questionOption.modify(questionOptionUpdateRequest);
                }
            }
        }
        // Question 삭제 : DB에 있으나 요청에 없는경우 삭제
        // 요청으로 들어온 Question ID 목록
        Set<Long> requestQuestionIds = surveyUpdateRequest.getQuestions().stream()
                .map(QuestionUpdateRequest::getQuestionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // DB에 존재하는 Question 목록
        List<Question> existingQuestions = questionRepository.findBySurvey(survey);

        // 요청에 없는 Question 삭제
        for (Question existing : existingQuestions) {
            if (!requestQuestionIds.contains(existing.getId())) {
                questionRepository.delete(existing);
            }
        }


        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .build();
    }

    public SurveyResponse deleteSurvey(SurveyReadRequest surveyReadRequest) {
        // 삭제할 설문 조회
        Survey survey = surveyRepository.findById(surveyReadRequest.getSurveyId()).orElse(null);

        // 삭제할 문항 조회
        List<Question> questionList = questionRepository.findBySurvey(survey);

        // 문항만큼 반복
        for (Question question : questionList) {
            // 삭제할 옵션 조회
            List<QuestionOption> questionOptionList = questionOptionRepository.findByQuestion(question);
            questionOptionRepository.deleteAll(questionOptionList);
        }

        questionRepository.deleteAll(questionList);
        surveyRepository.delete(survey);

        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .build();
    }

    @Transactional
    public SurveyResponse participateSurvey(SurveyParticipationRequest surveyParticipationRequest, String anonymousToken, String ipAddress) {
        // 참여할 설문 조회
        Survey survey = surveyRepository.findById(surveyParticipationRequest.getSurveyId()).orElse(null);

        // 설문 참여(survey_participation) 생성
        SurveyParticipation surveyParticipation = getSurveyParticipation(surveyParticipationRequest, anonymousToken, ipAddress, survey);

        // 문항 조회
        List<Question> questions = questionRepository.findBySurvey(survey);
        Map<Long, Question> dbQuestionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));
        List<Long> dbQuestionIds = dbQuestionMap.keySet().stream().toList();

        // 요청 값 (문항 id - 옵션 idList)  Map 생성
        Map<Long, List<Long>> answerMap = surveyParticipationRequest.getAnswers().stream().collect(Collectors.toMap(
                SurveyAnswerRequest::getQuestionId,
                SurveyAnswerRequest::getQuestionOptionIds
        ));

        // 문항 검증
        validateQuestions(dbQuestionIds, answerMap);

        // 옵션 조회 - 앞서 만든 요청값 map을 통해 db에서 옵션 조회
        // 1. 요청에 들어온 모든 옵션 ID를 flatMap으로 모아줌
        List<Long> allOptionIds = answerMap.values().stream()
                .flatMap(List::stream)   // List<List<Long>> -> Stream<Long>
                .toList();

        // 2. DB에서 해당 옵션들을 한 번에 조회
        List<QuestionOption> optionEntities = questionOptionRepository.findAllById(allOptionIds);

        // 3. Map 형태로 변환 (id → entity 매핑)
        Map<Long, QuestionOption> optionMap = optionEntities.stream()
                .collect(Collectors.toMap(QuestionOption::getId, o -> o));

        // SurveyAnswer 생성
        List<SurveyAnswer> surveyAnswers = new ArrayList<>();
        for (SurveyAnswerRequest answer : surveyParticipationRequest.getAnswers()) {
            Question question = dbQuestionMap.get(answer.getQuestionId());
            validateAnswer(question, answer);

            for (Long optionId : answer.getQuestionOptionIds()) {
                QuestionOption option = optionMap.get(optionId);
                validateOption(question, option);

                surveyAnswers.add(SurveyAnswer.builder()
                        .surveyParticipation(surveyParticipation)
                        .question(question)
                        .questionOption(option)
                        .questionContent(question.getContent())
                        .questionOptionContent(option.getContent())
                        .build());
            }
        }

        surveyAnswerRepository.saveAll(surveyAnswers);

        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .build();

    }

    private void validateQuestions(List<Long> dbQuestionIds, Map<Long, List<Long>> answerMap) {
        Set<Long> dbQuestionIdSet = new HashSet<>(dbQuestionIds);
        Set<Long> requestQuestionIdSet = answerMap.keySet();

        // 1. 문항 개수 다르면 → 누락 or 중복
        if (dbQuestionIdSet.size() != requestQuestionIdSet.size()) {
            log.warn("응답하지 않은 문항이 있거나 중복된 문항이 있습니다. dbQuestionIdSize : {} , requestQuestionIdSize : {}", dbQuestionIdSet.size(), requestQuestionIdSet.size());
        }

        // 2. 요청 문항이 DB 문항에 모두 포함되는지 확인
        if (!dbQuestionIdSet.containsAll(requestQuestionIdSet)) {
            log.warn("유효하지 않은 문항이 포함되어 있습니다.");
        }

        // 3. 각 문항에 옵션이 최소 1개 이상 있는지 확인
        for (Long questionId : requestQuestionIdSet) {
            List<Long> optionIds = answerMap.get(questionId);
            if (optionIds == null || optionIds.isEmpty()) {
                log.warn("문항에 대한 옵션이 선택되지 않았습니다. questionId : {}", questionId);
            }
        }
    }

    private void validateAnswer(Question question, SurveyAnswerRequest answer) {
        if (question.getSelectionType().equals(SeletionType.SINGLE) && answer.getQuestionOptionIds().size() > 1) {
            log.warn("단일 선택 문항은 옵션 1개만 선택해야 합니다. questionId : {}", question.getId());
        }
    }

    private void validateOption(Question question, QuestionOption option) {
        if (!option.getQuestion().getId().equals(question.getId())) {
            log.warn("옵션이 해당 문항에 속하지 않습니다. questionId : {} ,optionId : {}", question.getId(), option.getId());
        }
    }

    private SurveyParticipation getSurveyParticipation(SurveyParticipationRequest surveyParticipationRequest, String anonymousToken, String ipAddress, Survey survey) {
        // 설문 참여 생성
        SurveyParticipation.SurveyParticipationBuilder surveyParticipationBuilder = SurveyParticipation.builder()
                .survey(survey)
                .surveyDt(surveyParticipationRequest.getSurveyDt());

        // 하나의 설문 중복 참여 불가
        // 익명 사용자인 경우
        if (anonymousToken != null) {
            Optional<SurveyParticipation> anonymousTokenSurvey = surveyParticipationRepository.findBySurveyAndAnonymousToken(survey, anonymousToken);

            // 익명 사용자 토큰에 해당하는 설문이 있는 경우
            if (anonymousTokenSurvey.isPresent()) {
                log.warn("이미 설문한 익명 사용자 토큰 입니다. anonymousToken : {}", anonymousTokenSurvey.get().getAnonymousToken());
                return null;
            } else {
                Optional<SurveyParticipation> anonymousIpAddressSurvey = surveyParticipationRepository.findBySurveyAndIpAddress(survey, ipAddress);

                // ip주소에 해당하는 설문이 있는 경우
                if (anonymousIpAddressSurvey.isPresent()) {
                    log.warn("이미 설문한 ip주소 입니다.  ipAddress : {}", anonymousIpAddressSurvey.get().getIpAddress());
                    return null;
                }

            }
            surveyParticipationBuilder.anonymousToken(anonymousToken)
                    .ipAddress(ipAddress);

        // 로그인한 사용자 인경우
        } else {
            Optional<SurveyParticipation> consumerSurvey = surveyParticipationRepository.findBySurveyAndUserId(survey, SecurityUtil.getCurrentUserId());

            // 사용자 아이디에 해당하는 설문이 있는 경우
            if (consumerSurvey.isPresent()) {
                log.warn("이미 설문한 사용자 입니다.  consumerId : {}", consumerSurvey.get().getUserId());
                return null;
            }
            surveyParticipationBuilder.userId(SecurityUtil.getCurrentUserId())
                    .ipAddress(ipAddress);

        }
        SurveyParticipation surveyParticipation = surveyParticipationBuilder.build();
        surveyParticipationRepository.save(surveyParticipation);
        return surveyParticipation;
    }

    public PagedResponse<SurveyParticipationResponse> getSurveyParticipation(SurveyParticipationReadRequest surveyParticipationReadRequest) {
        PagedResponse<SurveyParticipationQuery> surveyParticipation = surveyQueryRepository.findSurveyParticipation(surveyParticipationReadRequest);
        List<SurveyParticipationResponse> items = HierarchyMapper.toHierarchy(
                surveyParticipation.getItems()
                , SurveyParticipationQuery::getSurveyParticipationId
                , p -> SurveyParticipationResponse.builder()
                        .surveyParticipationId(p.getSurveyParticipationId())
                        .surveyId(p.getSurveyId())
                        .userId(p.getUserId())
                        .surveyDt(p.getSurveyDt())
                        .questions(new ArrayList<>())
                        .build()
                ,// Param 4 : 부모에 자식 붙이는 함수
                (survey, row) -> {
                    // Survey 안에 Question 계층 생성
                    List<SurveyParticipationQuestionResponse> questions = survey.getQuestions();

                    // 이미 존재하는 Question 확인
                    SurveyParticipationQuestionResponse question = questions.stream()
                            .filter(q -> q.getQuestionId().equals(row.getQuestionId()))
                            .findFirst()
                            .orElseGet(() -> {
                                SurveyParticipationQuestionResponse q = SurveyParticipationQuestionResponse.builder()
                                        .questionId(row.getQuestionId())
                                        .questionContent(row.getQuestionContent())
                                        .options(new ArrayList<>())
                                        .build();
                                questions.add(q);
                                return q;
                            });
                    // Question 안에 Option 추가
                    if (row.getQuestionOptionId() != null) {
                        question.addOption(SurveyParticipationOptionResponse.builder()
                                .questionOptionId(row.getQuestionOptionId())
                                .questionOptionContent(row.getQuestionOptionContent())
                                .build());
                    }
                }
        );

        return new PagedResponse<>(items,surveyParticipation.getPage(),surveyParticipation.getSize(),surveyParticipation.getTotalCount(),surveyParticipation.getTotalPages());

    }

    public PagedResponse<SurveyResultResponse> getSurveyResult(SurveyResultReadRequest surveyResultReadRequest) {
        PagedResponse<SurveyResultQuery> surveyResult;

        // 실시간 조회 인경우
        if (surveyResultReadRequest.getLiveYn()) {
            surveyResult = surveyQueryRepository.findLiveSurveyResult(surveyResultReadRequest);
        } else {
            surveyResult = surveyQueryRepository.findSurveyResult(surveyResultReadRequest);
        }

        // 계층 구조로 변환
        List<SurveyResultResponse> items = HierarchyMapper.toHierarchy(
                surveyResult.getItems()
                , SurveyResultQuery::getSurveyId
                , p -> SurveyResultResponse.builder()
                        .surveyId(p.getSurveyId())
                        .surveyTitle(p.getSurveyTitle())
                        .questions(new ArrayList<>())
                        .build()
                , (survey, row) -> {
                    // Survey 안에 Question 계층 생성
                    List<SurveyResultQuestionResponse> questions = survey.getQuestions();

                    // 이미 존재하는 Question 확인
                    SurveyResultQuestionResponse question = questions.stream()
                            .filter(q -> q.getQuestionId().equals(row.getQuestionId()))
                            .findFirst()
                            .orElseGet(() -> {
                                SurveyResultQuestionResponse q = SurveyResultQuestionResponse.builder()
                                        .questionId(row.getQuestionId())
                                        .questionContent(row.getQuestionContent())
                                        .options(new ArrayList<>())
                                        .build();
                                questions.add(q);
                                return q;
                            });
                    // Question 안에 Option 추가
                    if (row.getQuestionOptionId() != null) {
                        question.addOption(SurveyResultOptionResponse.builder()
                                .questionOptionId(row.getQuestionOptionId())
                                .questionOptionContent(row.getQuestionOptionContent())
                                .selectedCount(row.getSelectedCount())
                                .selectedRatio(row.getSelectedRatio())
                                .build());
                    }

                }
        );
        //계층구조로 변환 후 다시 PageResponse에 담아주기
        return new PagedResponse<>(items,surveyResult.getPage(),surveyResult.getSize(),surveyResult.getTotalCount(),surveyResult.getTotalPages());
    }
}
