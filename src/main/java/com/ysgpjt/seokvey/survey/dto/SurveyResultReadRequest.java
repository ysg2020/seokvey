package com.ysgpjt.seokvey.survey.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyResultReadRequest : 설문 결과 조회 요청 DTO")
public class SurveyResultReadRequest {

    @Schema(description = "설문 고유값", example = "1")
    private Long surveyId;                  // 설문 고유값

    // 스케줄러 생성용
    private List<Long> surveyIdList;        // 설문 고유값 리스트

}
