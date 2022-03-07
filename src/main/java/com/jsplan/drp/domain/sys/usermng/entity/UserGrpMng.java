package com.jsplan.drp.domain.sys.usermng.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @Class : UserGrpMng
 * @Author : KDW
 * @Date : 2022-03-04
 * @Description : 그룹 관리 엔티티
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SYS_DRP_GRP")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"grpCd", "grpNm", "grpDesc"})
public class UserGrpMng implements Persistable<String> {

    @Id
    @Column(name = "GRP_CD", length = 20)
    private String grpCd; // 그룹 코드

    @Column(name = "GRP_NM", nullable = false, length = 50)
    private String grpNm; // 그룹명

    @Column(name = "GRP_DESC", length = 1000)
    private String grpDesc; // 그룹 설명

    @CreatedBy
    @Column(name = "REG_USER", length = 100, updatable = false)
    private String regUser; // 등록자

    @CreatedDate
    @Column(name = "REG_DATE", updatable = false)
    private LocalDateTime regDate; // 등록 일시

    @LastModifiedBy
    @Column(name = "MOD_USER", length = 100, updatable = false)
    private String modUser; // 수정자

    @LastModifiedDate
    @Column(name = "MOD_DATE")
    private LocalDateTime modDate; // 수정 일시

    @Override
    public String getId() {
        return grpCd;
    }

    @Override
    public boolean isNew() {
        return regDate == null;
    }
}
