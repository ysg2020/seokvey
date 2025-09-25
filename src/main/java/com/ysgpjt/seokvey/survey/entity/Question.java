package com.ysgpjt.seokvey.survey.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import com.ysgpjt.seokvey.survey.dto.QuestionUpdateRequest;
import com.ysgpjt.seokvey.type.SeletionType;
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
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Survey survey;                  // 설문 고유값

    @Column
    private String content;                 // 문항 내용

    @Column
    @Enumerated(EnumType.STRING)
    private SeletionType selectionType;     // 선택 종류

    @Column
    private Integer orderNo;                // 문항 순서

    // 문항 수정
    public void modify(QuestionUpdateRequest questionUpdateRequest) {
        this.content = questionUpdateRequest.getContent();
        this.selectionType = questionUpdateRequest.getSelectionType();
        this.orderNo = questionUpdateRequest.getOrderNo();
    }

}
