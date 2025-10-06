package com.ysgpjt.seokvey.common;


import com.ysgpjt.seokvey.common.dto.PagedResponse;
import com.ysgpjt.seokvey.survey.dto.SurveyResultQuery;
import com.ysgpjt.seokvey.survey.dto.SurveyResultReadRequest;
import com.ysgpjt.seokvey.survey.entity.*;
import com.ysgpjt.seokvey.survey.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final SurveyQueryRepository surveyQueryRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final QuestionOptionResultRepository questionOptionResultRepository;


    @Scheduled(cron = "0 0 0 * * *")
    public void schedule() {
        log.info("Schedule start");
        int size = 20;
        // 결과 생성이 안된 종료된 설문 id 리스트 조회
        List<Long> endSurveyIdList = surveyRepository.findByEndDtBeforeAndResultGenerated(LocalDateTime.now(),false)
                .stream().map(Survey::getId).toList();
        List<SurveyResult> surveyResultList = new ArrayList<>();
        List<QuestionOptionResult> questionOptionResultList = new ArrayList<>();

        // 설문 결과 생성
        for (Long endSurveyId :endSurveyIdList) {
            Survey survey = surveyRepository.findById(endSurveyId).get();
            SurveyResult surveyResult = SurveyResult.builder()
                    .survey(survey)
                    .title(survey.getTitle())
                    .build();
            surveyResultList.add(surveyResult);
            // 결과 생성
            survey.generateResult();
            surveyRepository.save(survey);

        }
        surveyResultRepository.saveAll(surveyResultList);

        // 문항 옵션 결과 생성에 참조하기위한 map 생성
        Map<Long, SurveyResult> surveyResultMap = surveyResultList.stream()
                .collect(Collectors.toMap(sr -> sr.getSurvey().getId(), sr -> sr));


        int totalPages = (int) Math.ceil((double) endSurveyIdList.size() / size);
        for (int i = 0; i < totalPages; i++) {
            SurveyResultReadRequest resultRequest = SurveyResultReadRequest.builder()
                    .surveyIdList(endSurveyIdList)
                    .page(i)
                    .size(size)
                    .build();

            // 문항 옵션 결과 생성
            List<SurveyResultQuery> surveyResultQueryList = surveyQueryRepository.findLiveSurveyResult(resultRequest).getItems();
            for (SurveyResultQuery surveyResultQuery : surveyResultQueryList) {
                SurveyResult surveyResult = surveyResultMap.get(surveyResultQuery.getSurveyId());
                Question question = questionRepository.findById(surveyResultQuery.getQuestionId()).get();
                QuestionOption questionOption = questionOptionRepository.findById(surveyResultQuery.getQuestionOptionId()).get();

                QuestionOptionResult questionOptionResult = QuestionOptionResult.builder()
                        .surveyResult(surveyResult)
                        .question(question)
                        .questionOption(questionOption)
                        .questionContent(surveyResultQuery.getQuestionContent())
                        .questionOptionContent(surveyResultQuery.getQuestionOptionContent())
                        .selectedCount(surveyResultQuery.getSelectedCount())
                        .selectedRatio(surveyResultQuery.getSelectedRatio())
                        .build();
                questionOptionResultList.add(questionOptionResult);

            }
            questionOptionResultRepository.saveAll(questionOptionResultList);

        }

    }
}
