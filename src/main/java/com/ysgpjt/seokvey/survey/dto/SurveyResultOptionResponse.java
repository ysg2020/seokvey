package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyResultQuestionResponse : 설문 결과 옵션 응답 DTO")
public class SurveyResultOptionResponse {

    @Schema(description = "문항 옵션 고유값", example = "1")
    private Long questionOptionId;              // 문항 옵션 고유값

    @Schema(description = "문항 옵션 내용", example = "첫번째 문항 내용")
    private String questionOptionContent;       // 문항 옵션 내용

    @Schema(description = "선택 횟수", example = "5")
    private Long selectedCount;                 // 선택 횟수

    @Schema(description = "선택 비율", example = "50")
    private Long selectedRatio;                 // 선택 비율

}
