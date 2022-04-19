package com.jsplan.drp.domain.sys.usermng.entity;

import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import com.jsplan.drp.global.obj.entity.UseStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

/**
 * @Class : UserMng
 * @Author : KDW
 * @Date : 2022-03-18
 * @Description : 사용자 관리 Entity
 */
@Entity
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
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

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

    @Builder
    public UserMng(String userId, String userNm, String userPw, String mobileNum,
        String email, String userType, UseStatus useYn, UserGrpMng userGrpMng) {
        this.userId = userId;
        this.userNm = userNm;
        this.userPw = userPw;
        this.mobileNum = mobileNum;
        this.email = email;
        this.userType = userType;
        this.useYn = useYn;
        setUserGrpMng(userGrpMng);
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }

    // 엔티티 수정
    public void update(UserMngRequest request) {
        this.userNm = request.getUserNm();
        if (request.getUserPw() != null) {
            this.userPw = request.getUserPw();
        }
        this.mobileNum = request.getMobileNum();
        this.email = request.getEmail();
        this.userType = request.getUserType();
        this.useYn = request.getUseYn();

        // FK 변경
//        setUserGrpMng(UserGrpMng.builder().grpCd(request.getGrpCd()).build());
    }
}
