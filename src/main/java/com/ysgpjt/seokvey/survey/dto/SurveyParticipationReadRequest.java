package com.ysgpjt.seokvey.survey.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyParticipationReadRequest {
    private String userId;      // 사용자 아이디
    private int page;           // 페이지
    private int size;           // 크기
}
