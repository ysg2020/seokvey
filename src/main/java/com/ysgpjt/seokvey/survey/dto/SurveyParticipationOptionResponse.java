package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyParticipationOptionResponse {

    private Long questionOptionId;              // 문항 옵션 고유값
    private String questionOptionContent;       // 문항 옵션 내용

}
