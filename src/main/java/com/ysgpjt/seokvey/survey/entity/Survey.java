package com.ysgpjt.seokvey.survey.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
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

}
