package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionCreateRequest {

    private String content;             // 내용
    private Integer orderNo;            // 옵션 순서
}
