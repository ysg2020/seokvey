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
@Schema(title = "SurveyResultResponse : 설문 결과 응답 DTO")
public class SurveyResultResponse {

    @Schema(description = "설문 고유값", example = "1")
    private Long surveyId;                                      // 설문 고유값

    @Schema(description = "설문 제목", example = "테스트 설문 제목")
    private String surveyTitle;                                 // 설문 제목

    @Schema(description = "설문 참여 문항 리스트")
    private List<SurveyResultQuestionResponse> questions;       // 설문 참여 문항 리스트


}
