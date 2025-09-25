package com.ysgpjt.seokvey.survey.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import com.ysgpjt.seokvey.survey.dto.QuestionOptionUpdateRequest;
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
public class QuestionOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;          // 문항 고유값

    @Column
    private String content;             // 옵션 내용

    @Column
    private Integer orderNo;            // 옵션 순서

    // 옵션 수정
    public void modify(QuestionOptionUpdateRequest questionOptionUpdateRequest) {
        this.content = questionOptionUpdateRequest.getContent();
        this.orderNo = questionOptionUpdateRequest.getOrderNo();
    }

}
