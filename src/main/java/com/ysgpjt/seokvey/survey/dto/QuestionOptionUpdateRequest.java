package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "QuestionOptionUpdateRequest : 옵션 수정 요청 DTO")
public class QuestionOptionUpdateRequest {

    @Schema(description = "옵션 고유값", example = "1")
    private Long questionOptionId;      // 옵션 고유값

    @Schema(description = "내용", example = "첫번째 옵션 내용")
    private String content;             // 내용

    @Schema(description = "옵션 순서", example = "1")
    private Integer orderNo;            // 옵션 순서
}
