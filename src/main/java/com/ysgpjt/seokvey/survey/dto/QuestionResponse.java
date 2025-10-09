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
@Schema(title = "QuestionResponse : 문항 응답 DTO")
public class QuestionResponse {

    @Schema(description = "문항 고유값", example = "1")
    private Long questionId;                                // 문항 고유값

    @Schema(description = "내용", example = "테스트 문항 내용")
    private String content;                                 // 내용

    @Schema(description = "선택 종류", example = "SINGLE")
    private SeletionType selectionType;                     // 선택 종류

    @Schema(description = "문항 순서", example = "1")
    private Integer orderNo;                                // 문항 순서

    @Schema(description = "문항 옵션 리스트", example = "1")
    private List<QuestionOptionResponse> options;           // 문항 옵션 리스트

    @Schema(description = "문항 총 갯수", example = "30")
    private int questionTotalCount;                         // 문항 총 갯수

    @Schema(description = "문항 총 페이지", example = "5")
    private int questionTotalPages;                         // 문항 총 페이지

    public void addOption(QuestionOptionResponse option) {
        this.options.add(option);
    }


}
