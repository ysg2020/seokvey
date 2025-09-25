package com.ysgpjt.seokvey.survey.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyReadRequest {
    private Long surveyId;      // 설문 고유값
    private int page;           // 페이지
    private int size;           // 크기
}
