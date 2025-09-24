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
public class SurveyQuery {

    private Long surveyId;
    private String title;
    private String description;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private Long questionId;
    private String questionContent;
    private SeletionType selectionType;
    private Integer questionOrderNo;
    private Long optionId;
    private String optionContent;
    private Integer optionOrderNo;


}
