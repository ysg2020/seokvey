package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyParticipationResponse : 설문 참여 옵션 조회 응답 DTO")
public class SurveyParticipationOptionResponse {

    @Schema(description = "옵션 고유값", example = "1")
    private Long questionOptionId;              // 문항 옵션 고유값

    @Schema(description = "옵션 내용", example = "첫번째 옵션 내용")
    private String questionOptionContent;       // 문항 옵션 내용

}
