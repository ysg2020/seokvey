package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "QuestionOptionCreateRequest : 옵션 생성 요청 DTO")
public class QuestionOptionCreateRequest {

    @Schema(description = "내용", example = "첫번째 옵션 내용")
    private String content;             // 내용

    @Schema(description = "옵션 순서", example = "1")
    private Integer orderNo;            // 옵션 순서
}
