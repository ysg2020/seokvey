package com.ysgpjt.seokvey.survey.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyReadRequest : 설문 조회 요청 DTO")
public class SurveyReadRequest {
    @Schema(description = "설문 고유값", example = "1")
    private Long surveyId;      // 설문 고유값

    @Schema(description = "페이지", example = "1")
    private int page;           // 페이지

    @Schema(description = "크기", example = "10")
    private int size;           // 크기
}
