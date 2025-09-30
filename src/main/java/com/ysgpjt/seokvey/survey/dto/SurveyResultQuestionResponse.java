package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResultQuestionResponse {

    private Long questionId;                                // 문항 고유값
    private String questionContent;                         // 문항 내용
    private List<SurveyResultOptionResponse> options;       // 옵션 리스트

    public void addOption(SurveyResultOptionResponse option) {
        this.options.add(option);
    }
}
