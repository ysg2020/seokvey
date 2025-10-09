package com.ysgpjt.seokvey.survey.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyParticipationRequest : 설문 참여 요청 DTO")
public class SurveyParticipationRequest {

    @Schema(description = "설문 고유값", example = "1")
    private Long surveyId;                          // 설문 고유값

    @Schema(description = "설문 참여 시각", example = "1")
    private LocalDateTime surveyDt;                 // 설문 참여 시각

    @Schema(description = "설문 답변", example = "1")
    private List<SurveyAnswerRequest> answers;      // 설문 답변
}
