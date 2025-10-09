package com.ysgpjt.seokvey.survey.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "SurveyUpdateRequest : 설문 수정 요청 DTO")
public class SurveyUpdateRequest {

    @Schema(description = "설문 고유값", example = "1")
    private Long surveyId;                              // 설문 고유값

    @Schema(description = "제목", example = "테스트 설문 제목")
    private String title;                               // 제목

    @Schema(description = "설명", example = "테스트 설문 설명")
    private String description;                         // 설명

    @Schema(description = "설문 시작일", example = "2025-10-09 12:00:00")
    private LocalDateTime startDt;                      // 설문 시작일

    @Schema(description = "설문 종료일", example = "2025-10-10 12:00:00")
    private LocalDateTime endDt;                        // 설문 종료일

    @Schema(description = "문항 리스트")
    private List<QuestionUpdateRequest> questions;      // 문항 리스트
}
