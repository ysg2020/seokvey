package com.ysgpjt.seokvey.log.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import com.ysgpjt.seokvey.consumer.entity.Consumer;
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
public class SurveyHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Consumer consumer;                  // 사용자 고유값

    @Column
    private Long questionOptionId;              // 문항 옵션 고유값

    @Column
    private String anonymousToken;              // 익명 사용자 식별 토큰

    @Column
    private LocalDateTime surveyHistoryDt;      // 설문 기록 시각

}
