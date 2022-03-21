package com.jsplan.drp.domain.sys.usermng.entity;

import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @Class : UserMng
 * @Author : KDW
 * @Date : 2022-03-18
 * @Description : 사용자 관리 Entity
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SYS_DRP_USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"userId", "userNm", "mobileNum", "email", "userType", "useYn"})
public class UserMng extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "USER_ID", length = 100)
    private String userId; // 사용자 아이디

    @Column(name = "USER_NM")
    private String userNm; // 성명

    @Column(name = "USER_PW", length = 68)
    private String userPw; // 비밀번호

    @Column(name = "MOBILE_NUM", length = 50)
    private String mobileNum; // 휴대폰 번호

    @Column(name = "EMAIL", length = 100)
    private String email; // 이메일

    @Column(name = "USER_TYPE", length = 1)
    private String userType; // 사용자 유형

    @Column(name = "USE_YN", length = 1)
    private String useYn; // 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRP_CD")
    private UserGrpMng userGrpMng; // 그룹 엔티티

    // 연관관계 편의 메서드
    public void setUserGrpMng(UserGrpMng userGrpMng) {
        // 기존 그룹 엔티티 정보 제거
        if (this.userGrpMng != null) {
            this.userGrpMng.getUserMng().remove(this);
        }
        this.userGrpMng = userGrpMng;
        userGrpMng.getUserMng().add(this);
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }
}
