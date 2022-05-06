package com.jsplan.drp.global.obj.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @Class : BaseTimeEntity
 * @Author : KDW
 * @Date : 2022-04-06
 * @Description : JPA Auditing Time Entity
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "REG_DATE", updatable = false)
    private LocalDateTime regDate; // 등록 일시

    @LastModifiedDate
    @Column(name = "MOD_DATE")
    private LocalDateTime modDate; // 수정 일시
}
