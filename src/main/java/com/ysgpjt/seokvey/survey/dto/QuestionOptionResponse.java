package com.ysgpjt.seokvey.survey.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionResponse {

    private Long questionOptionId;          // 옵션 고유값
    private String content;                 // 내용
    private Integer orderNo;                // 옵션 순서
}
