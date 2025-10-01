package com.ysgpjt.seokvey.survey.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
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
public class SurveyParticipation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Survey survey;              // 설문 고유값

    @Column
    private String userId;              // 사용자 아이디

    @Column
    private String anonymousToken;      // 비회원 식별 토큰

    @Column
    private String ipAddress;           // ip 주소

    @Column
    private LocalDateTime surveyDt;     // 설문 참여 시각



}
