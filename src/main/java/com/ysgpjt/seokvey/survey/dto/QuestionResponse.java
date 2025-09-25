package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.type.SeletionType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long questionId;                                // 문항 고유값
    private String content;                                 // 내용
    private SeletionType selectionType;                     // 선택 종류
    private Integer orderNo;                                // 문항 순서
    private List<QuestionOptionResponse> options;       // 문항 옵션 리스트

    public void addOption(QuestionOptionResponse option) {
        this.options.add(option);
    }


}
