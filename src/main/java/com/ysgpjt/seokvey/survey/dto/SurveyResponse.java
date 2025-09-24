package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {

    private Long surveyId;                            // 설문 고유값
}
