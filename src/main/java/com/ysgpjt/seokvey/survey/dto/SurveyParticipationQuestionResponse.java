package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyParticipationQuestionResponse {

    private Long questionId;                                        // 문항 고유값
    private String questionContent;                                 // 문항 내용
    private List<SurveyParticipationOptionResponse> options;        // 옵션 리스트

    public void addOption(SurveyParticipationOptionResponse option) {
        this.options.add(option);
    }
}
