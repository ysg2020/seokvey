package com.ysgpjt.seokvey.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultQuery {

    private Long surveyId;                      // 설문 고유값
    private String surveyTitle;                 // 설문 제목
    private Long questionId;                    // 문항 고유값
    private String questionContent;             // 문항 내용
    private Long questionOptionId;              // 옵션 고유값
    private String questionOptionContent;       // 옵션 내용
    private Long selectedCount;                 // 선택 횟수
    private Long selectedRatio;                 // 선택 비율


}
