package com.ysgpjt.seokvey.consumer.entity;

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
public class Consumer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer age;            // 나이

    @Column
    private String gender;         // 성별

    @Column
    private String userId;         // 아이디

    @Column
    private String userPw;         // 비밀번호

    @Column
    private String email;         // 이메일


}
