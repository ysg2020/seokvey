package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.survey.entity.SurveyParticipation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyParticipationResponse : 설문 참여 조회 응답 DTO")
public class SurveyParticipationResponse {

    @Schema(description = "설문 참여 고유값", example = "1")
    private Long surveyParticipationId;                                 // 설문 참여 고유값

    @Schema(description = "설문 고유값", example = "1")
    private Long surveyId;                                              // 설문 고유값

    @Schema(description = "사용자 아이디", example = "user123")
    private String userId;                                              // 사용자 아이디

    @Schema(description = "설문 참여 시각", example = "2025-10-09 12:00:00")
    private LocalDateTime surveyDt;                                     // 설문 참여 시각

    @Schema(description = "설문 참여 문항 리스트")
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
