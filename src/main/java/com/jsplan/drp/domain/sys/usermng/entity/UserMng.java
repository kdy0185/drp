package com.jsplan.drp.domain.sys.usermng.entity;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.jsplan.drp.global.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column(name = "USER_NM", nullable = false)
    private String userNm; // 성명

    @Column(name = "USER_PW", nullable = false, length = 68)
    private String userPw; // 비밀번호

    @Column(name = "MOBILE_NUM", length = 50)
    private String mobileNum; // 휴대폰 번호

    @Column(name = "EMAIL", length = 100)
    private String email; // 이메일

    @Column(name = "USER_TYPE", nullable = false, length = 1)
    private String userType; // 사용자 유형

    @Column(name = "USE_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRP_CD")
    private UserGrpMng userGrpMng; // 그룹 엔티티

    @OneToMany(mappedBy = "userMng", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAuthMng> userAuthMng = new ArrayList<>(); // 사용자 권한 매핑 엔티티

    @Builder
    public UserMng(String userId, String userNm, String userPw, String mobileNum, String email,
        String userType, UseStatus useYn, UserGrpMng userGrpMng, String userAuthMng) {
        this.userId = userId;
        this.userNm = userNm;
        this.userPw = userPw;
        this.mobileNum = mobileNum;
        this.email = email;
        this.userType = userType;
        this.useYn = useYn;
        setUserGrpMng(userGrpMng);
        setUserAuthMng(userAuthMng);
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }

    // 그룹 설정
    private void setUserGrpMng(UserGrpMng userGrpMng) {
        removeUserGrpMng();
        this.userGrpMng = userGrpMng;
        userGrpMng.getUserMng().add(this);
    }

    // 기존 그룹 제거
    private void removeUserGrpMng() {
        if (this.userGrpMng != null) {
            this.userGrpMng.getUserMng().remove(this);
        }
    }

    // 권한 설정
    private void setUserAuthMng(String authCdList) {
        removeUserAuthMng();
        Arrays.asList(StringUtil.split(authCdList)).forEach(this::addUserAuthMng);
        addUserAuthMng("IS_AUTHENTICATED_FULLY"); // 기본 인증 권한 설정
    }

    // 기존 권한 제거
    private void removeUserAuthMng() {
        if (this.userAuthMng.size() > 0) {
            this.userAuthMng.clear();
        }
    }

    // 권한 매핑 정보 추가
    private void addUserAuthMng(String authCd) {
        UserAuthMng userAuthMng = UserAuthMng.createUserAuthMng(this,
            AuthMng.builder().authCd(authCd).build());
        this.userAuthMng.add(userAuthMng);
    }

    // 사용자 수정
    public void updateUserMng(UserMngRequest request) {
        this.userNm = request.getUserNm();
        if (!StringUtil.isBlank(request.getUserPw())) {
            this.userPw = request.getUserPw();
        }
        this.mobileNum = request.getMobileNum();
        this.email = request.getEmail();
        this.userType = request.getUserType();
        this.useYn = request.getUseYn();
        setUserAuthMng(request.getAuthCd());
    }

    // 권한 매핑 정보 수정
    public void updateUserAuthMng(String authCdList) {
        setUserAuthMng(authCdList);
    }
}
