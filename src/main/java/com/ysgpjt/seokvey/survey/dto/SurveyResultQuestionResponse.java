package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyResultQuestionResponse : 설문 결과 문항 응답 DTO")
public class SurveyResultQuestionResponse {

    @Schema(description = "문항 고유값", example = "1")
    private Long questionId;                                // 문항 고유값

    @Schema(description = "문항 내용", example = "첫번째 문항 내용")
    private String questionContent;                         // 문항 내용

    @Schema(description = "옵션 리스트")
    private List<SurveyResultOptionResponse> options;       // 옵션 리스트

    public void addOption(SurveyResultOptionResponse option) {
        this.options.add(option);
    }
}
