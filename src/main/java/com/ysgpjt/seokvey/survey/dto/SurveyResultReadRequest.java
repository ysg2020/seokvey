package com.ysgpjt.seokvey.survey.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResultReadRequest {
    private Long surveyId;                  // 설문 고유값

    // 스케줄러 생성용
    private List<Long> surveyIdList;        // 설문 고유값 리스트

}
