package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.type.SeletionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQuery {

    private Long questionId;                // 문항 고유값
    private String questionContent;         // 문항 내용
    private SeletionType selectionType;     // 선택 종류
    private Integer questionOrderNo;        // 문항 순서
    private Long optionId;                  // 옵션 고유값
    private String optionContent;           // 옵션 내용
    private Integer optionOrderNo;          // 옵션 순서


}
