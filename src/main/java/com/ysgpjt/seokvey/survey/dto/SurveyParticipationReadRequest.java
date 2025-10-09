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
@Schema(title = "SurveyParticipationReadRequest : 설문 참여 요청 DTO")
public class SurveyParticipationReadRequest {

    @Schema(description = "사용자 아이디", example = "user123")
    private String userId;      // 사용자 아이디

    @Schema(description = "페이지", example = "1")
    private int page;           // 페이지

    @Schema(description = "크기", example = "10")
    private int size;           // 크기
}
