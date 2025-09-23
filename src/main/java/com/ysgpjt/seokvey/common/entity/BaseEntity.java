package com.ysgpjt.seokvey.common.entity;

import jakarta.persistence.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_dt", updatable = false)
    private LocalDateTime createdDt;

    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    // 생성 시점
    @PrePersist
    public void prePersist() {
        this.createdDt = LocalDateTime.now();
        this.createdBy = getCurrentUser();
    }

    // 수정 시점
    @PreUpdate
    public void preUpdate() {
        this.updatedDt = LocalDateTime.now();
        this.updatedBy = getCurrentUser();
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }
        return authentication.getName(); // 로그인한 사용자 이름 반환
    }
}
