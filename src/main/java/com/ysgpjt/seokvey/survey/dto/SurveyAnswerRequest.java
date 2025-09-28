package com.ysgpjt.seokvey.survey.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyAnswerRequest {
    private Long questionId;                    // 문항 고유값
    private List<Long> questionOptionIds;       // 문항 옵션 고유값

}
