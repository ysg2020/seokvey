package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.survey.entity.Survey;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyListResponse {

    private List<SurveyResponse> surveys;               // 설문 리스트
    private Long surveyTotalCount;                      // 설문 총 갯수

}
