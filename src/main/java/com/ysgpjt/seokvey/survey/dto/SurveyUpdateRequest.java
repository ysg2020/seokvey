package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyUpdateRequest {

    private Long surveyId;                              // 설문 고유값
    private String title;                               // 제목
    private String description;                         // 설명
    private LocalDateTime startDt;                      // 설문 시작일
    private LocalDateTime endDt;                        // 설문 종료일
    private List<QuestionUpdateRequest> questions;      // 문항 리스트
}
