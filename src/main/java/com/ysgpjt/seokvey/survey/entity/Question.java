package com.ysgpjt.seokvey.survey.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Survey survey;            // 설문 고유값

    @Column
    private String content;             // 문항 내용

    @Column
    private String selectionType;       // 선택 종류

    @Column
    private Integer orderNo;            // 문항 순서

}
