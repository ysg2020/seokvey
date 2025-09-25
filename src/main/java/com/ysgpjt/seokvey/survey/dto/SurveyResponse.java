package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.survey.entity.Survey;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {

    private Long surveyId;                              // 설문 고유값
    private String title;                               // 제목
    private String description;                         // 설명
    private LocalDateTime startDt;                      // 설문 시작일
    private LocalDateTime endDt;                        // 설문 종료일
    private List<QuestionResponse> questions;           // 문항 리스트

    public static SurveyResponse fromSurvey(Survey survey) {
        return SurveyResponse.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .startDt(survey.getStartDt())
                .endDt(survey.getEndDt())
                .build();

    }

}
