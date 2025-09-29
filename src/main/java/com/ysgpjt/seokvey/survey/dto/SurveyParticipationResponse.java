package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.survey.entity.SurveyParticipation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyParticipationResponse {

    private Long surveyParticipationId;                                 // 설문 참여 고유값
    private Long surveyId;                                              // 설문 고유값
    private String userId;                                              // 사용자 아이디
    private LocalDateTime surveyDt;                                     // 설문 참여 시각
    private List<SurveyParticipationQuestionResponse> questions;        // 설문 참여 문항 리스트

    public static SurveyParticipationResponse fromSurveyParticipation(SurveyParticipation surveyParticipation) {
        return SurveyParticipationResponse.builder()
                .surveyParticipationId(surveyParticipation.getId())
                .surveyId(surveyParticipation.getSurvey().getId())
                .userId(surveyParticipation.getUserId())
                .surveyDt(surveyParticipation.getSurveyDt())
                .build();

    }

}
