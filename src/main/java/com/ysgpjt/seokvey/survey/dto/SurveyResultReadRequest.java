package com.ysgpjt.seokvey.survey.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResultReadRequest {
    private List<Long> surveyIdList;        // 설문 고유값 리스트
    private int page;                       // 페이지
    private int size;                       // 크기
    private Boolean liveYn;                 // 실시간 조회 여부
}
