package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "QuestionOptionResponse : 문항 옵션 응답 DTO")
public class QuestionOptionResponse {

    @Schema(description = "옵션 고유값", example = "1")
    private Long questionOptionId;          // 옵션 고유값

    @Schema(description = "내용", example = "테스트 옵션 내용")
    private String content;                 // 내용

    @Schema(description = "옵션 순서", example = "1")
    private Integer orderNo;                // 옵션 순서
}
