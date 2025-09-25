package com.ysgpjt.seokvey.survey.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import com.ysgpjt.seokvey.survey.dto.SurveyUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;               // 제목

    @Column
    private String description;         // 설명

    @Column
    private LocalDateTime startDt;     // 설문 시작일

    @Column
    private LocalDateTime endDt;       // 설문 종료일

    // 문항 수정
    public void modify(SurveyUpdateRequest surveyUpdateRequest) {
        this.title = surveyUpdateRequest.getTitle();
        this.description = surveyUpdateRequest.getDescription();
        this.startDt = surveyUpdateRequest.getStartDt();
        this.endDt = surveyUpdateRequest.getEndDt();
    }

}
