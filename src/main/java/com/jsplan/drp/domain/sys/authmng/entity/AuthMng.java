package com.jsplan.drp.domain.sys.authmng.entity;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequest;
import com.jsplan.drp.domain.sys.menumng.entity.MenuAuthMng;
import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserAuthMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.jsplan.drp.global.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

/**
 * @Class : AuthMng
 * @Author : KDW
 * @Date : 2022-04-20
 * @Description : 권한 관리 Entity
 */
@Entity
@Table(name = "SYS_DRP_AUTH")
@DynamicInsert
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"authCd"}, callSuper = false)
@ToString(of = {"authCd", "authNm", "authDesc", "authLv", "authOrd", "useYn"})
public class AuthMng {

    @Id
    @Column(name = "AUTH_CD", length = 24)
    private String authCd; // 권한 코드

    @Column(name = "AUTH_NM", nullable = false, length = 50)
    private String authNm; // 권한명

    @Column(name = "AUTH_DESC", length = 1000)
    private String authDesc; // 권한 설명

    @Column(name = "AUTH_LV", nullable = false)
    private Integer authLv; // 권한 수준

    @Column(name = "AUTH_ORD", nullable = false)
    private Integer authOrd; // 권한 순서

    @Column(name = "USE_YN", nullable = false, length = 1)
    @ColumnDefault("'Y'")
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPPER_AUTH_CD")
    private AuthMng upperAuthMng; // 상위 권한 엔티티

    @OneToMany(mappedBy = "upperAuthMng")
    private List<AuthMng> authMngList = new ArrayList<>(); // 하위 권한 엔티티

    @OneToMany(mappedBy = "authMng", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAuthMng> authUserMng = new ArrayList<>(); // 권한 사용자 매핑 엔티티

    @OneToMany(mappedBy = "authMng", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuAuthMng> authMenuMng = new ArrayList<>(); // 권한 메뉴 매핑 엔티티

    @Builder
    public AuthMng(String authCd, AuthMng upperAuthMng, String authNm, String authDesc, int authLv,
        int authOrd) {
        this.authCd = authCd;
        this.upperAuthMng = upperAuthMng;
        this.authNm = authNm;
        this.authDesc = authDesc;
        this.authLv = authLv;
        this.authOrd = authOrd;
    }

    // 최하위 자식 노드 여부 조회
    public String getLastYn() {
        return authMngList.size() > 0 ? "N" : "Y";
    }

    // 권한 수정
    public void updateAuthMng(AuthMngRequest request) {
        this.authNm = request.getAuthNm();
        this.authDesc = request.getAuthDesc();
        this.authLv = request.getAuthLv();
        this.authOrd = request.getAuthOrd();
        setUpperAuthMng(AuthMng.builder().authCd(request.getUpperAuthCd()).build());
    }

    // 상위 권한 설정
    private void setUpperAuthMng(AuthMng upperAuthMng) {
        removeUpperAuthMng();
        this.upperAuthMng = upperAuthMng;
        upperAuthMng.getAuthMngList().add(this);
    }

    // 기존 상위 권한 제거
    private void removeUpperAuthMng() {
        if (this.upperAuthMng != null) {
            this.upperAuthMng.getAuthMngList().remove(this);
        }
    }

    // 사용자 매핑 정보 수정
    public void updateAuthUserMng(String userIdList, String authYn) {
        for (String userId : StringUtil.split(userIdList)) {
            UserAuthMng userAuthMng = UserAuthMng.createUserAuthMng(
                UserMng.builder().userId(userId).build(), this);

            if ("Y".equals(authYn)) {
                mergeAuthUserMng(userAuthMng);
            }
            if ("N".equals(authYn)) {
                removeAuthUserMng(userAuthMng);
            }
        }
    }

    // 권한 허용
    private void mergeAuthUserMng(UserAuthMng userAuthMng) {
        if (!isContainUserId(userAuthMng)) {
            this.authUserMng.add(userAuthMng);
        }
    }

    // 권한 해제
    private void removeAuthUserMng(UserAuthMng userAuthMng) {
        if (isContainUserId(userAuthMng)) {
            // remove 전 엔티티 간 동등성 확인 (equals, hashCode)
            // 검증 엔티티 : UserAuthMng, UserMng, AuthMng
            this.authUserMng.remove(userAuthMng);
        }
    }

    // 기존 권한별 아이디 적용 여부
    private boolean isContainUserId(UserAuthMng userAuthMng) {
        String allUserIdList = this.authUserMng.stream().map(v -> v.getUserMng().getUserId())
            .collect(Collectors.joining(","));
        String userId = userAuthMng.getUserMng().getUserId();
        return StringUtil.isContain(allUserIdList, userId, ",");
    }

    // 메뉴 매핑 정보 수정
    public void updateAuthMenuMng(String menuCdList) {
        removeAuthMenuMng();
        Arrays.asList(StringUtil.split(menuCdList)).forEach(this::addAuthMenuMng);
    }

    // 기존 메뉴 제거
    private void removeAuthMenuMng() {
        if (this.authMenuMng.size() > 0) {
            this.authMenuMng.clear();
        }
    }

    // 메뉴 매핑 정보 추가
    private void addAuthMenuMng(String menuCd) {
        MenuAuthMng menuAuthMng = MenuAuthMng.createMenuAuthMng(
            MenuMng.builder().menuCd(menuCd).build(), this);
        this.authMenuMng.add(menuAuthMng);
    }
}
