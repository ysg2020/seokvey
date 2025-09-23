package com.ysgpjt.seokvey.log.entity;

import com.ysgpjt.seokvey.common.entity.BaseEntity;
import com.ysgpjt.seokvey.consumer.entity.Consumer;
import com.ysgpjt.seokvey.type.ActionType;
import com.ysgpjt.seokvey.type.TargetType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ConsumerActionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Consumer consumer;          // 사용자 고유값

    @Column
    private ActionType actionType;      // 행동 종류

    @Column
    private TargetType targetType;      // 행동 대상 종류

    @Column
    private String targetId;            // 행동 대상 고유값

    @Column
    private String extraInfo;           // 추가 정보

    @Column
    private String ip_address;          // 사용자 ip 주소

    @Column
    private String device_info;         // 기기정보

}
