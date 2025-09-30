package com.ysgpjt.seokvey.survey.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private SurveyResult surveyResult;                      // 설문 결과 고유값

    @JoinColumn
    @ManyToOne
    private Question question;                              // 문항 고유값

    @JoinColumn
    @ManyToOne
    private QuestionOption questionOption;                  // 문항 옵션 고유값

    @Column
    private String questionContent;                         // 문항 내용

    @Column
    private String questionOptionContent;                   // 문항 옵션 내용

    @Column
    private Long selectedCount;                             // 선택 횟수

    @Column
    private Long selectedRatio;                             // 선택 비율

}
