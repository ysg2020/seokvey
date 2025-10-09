package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyParticipationQuestionResponse : 설문 참여 문항 조회 응답 DTO")
public class SurveyParticipationQuestionResponse {

    @Schema(description = "문항 고유값", example = "1")
    private Long questionId;                                        // 문항 고유값

    @Schema(description = "문항 내용", example = "첫번째 문항 내용")
    private String questionContent;                                 // 문항 내용

    @Schema(description = "옵션 리스트")
    private List<SurveyParticipationOptionResponse> options;        // 옵션 리스트

    public void addOption(SurveyParticipationOptionResponse option) {
        this.options.add(option);
    }
}
