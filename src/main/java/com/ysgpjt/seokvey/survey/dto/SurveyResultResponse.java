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
public class SurveyResultResponse {

    private Long surveyId;                                      // 설문 고유값
    private String surveyTitle;                                 // 설문 제목
    private List<SurveyResultQuestionResponse> questions;       // 설문 참여 문항 리스트


}
