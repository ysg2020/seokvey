package com.ysgpjt.seokvey.survey.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyParticipationRequest {
    private Long surveyId;                          // 설문 고유값
    private LocalDateTime surveyDt;                 // 설문 참여 시각
    private List<SurveyAnswerRequest> answers;      // 설문 답변
}
