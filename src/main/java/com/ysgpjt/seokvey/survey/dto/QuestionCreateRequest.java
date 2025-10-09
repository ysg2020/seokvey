package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.type.SeletionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "QuestionCreateRequest : 문항 생성 요청 DTO")
public class QuestionCreateRequest {

    @Schema(description = "내용", example = "첫번째 테스트 문항 내용")
    private String content;                                 // 내용

    @Schema(description = "선택 종류", example = "SINGLE")
    private SeletionType selectionType;                     // 선택 종류

    @Schema(description = "문항 순서", example = "1")
    private Integer orderNo;                                // 문항 순서

    @Schema(description = "문항 옵션 리스트")
    private List<QuestionOptionCreateRequest> options;      // 문항 옵션 리스트
}
