package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResultOptionResponse {

    private Long questionOptionId;              // 문항 옵션 고유값
    private String questionOptionContent;       // 문항 옵션 내용
    private Long selectedCount;                 // 선택 횟수
    private Long selectedRatio;                 // 선택 비율

}
