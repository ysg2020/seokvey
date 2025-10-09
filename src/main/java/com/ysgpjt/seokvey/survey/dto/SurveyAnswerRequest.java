package com.ysgpjt.seokvey.survey.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyAnswerRequest : 설문 답변 조회 요청 DTO")
public class SurveyAnswerRequest {

    @Schema(description = "문항 고유값", example = "1")
    private Long questionId;                    // 문항 고유값

    @Schema(description = "옵션 고유값", example = "[1,2]")
    private List<Long> questionOptionIds;       // 문항 옵션 고유값

}
