package com.jsplan.drp.global.obj.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(name = "REG_USER", length = 100, updatable = false)
    private String regUser; // 등록자

    @LastModifiedBy
    @Column(name = "MOD_USER", length = 100)
    private String modUser; // 수정자
}
