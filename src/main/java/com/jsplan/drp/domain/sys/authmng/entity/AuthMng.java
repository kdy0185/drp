package com.jsplan.drp.domain.sys.authmng.entity;

import com.jsplan.drp.domain.sys.usermng.entity.UserAuthMng;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.util.ArrayList;
import java.util.List;
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

/**
 * @Class : AuthMng
 * @Author : KDW
 * @Date : 2022-04-20
 * @Description : 권한 관리 Entity
 */
@Entity
@Table(name = "SYS_DRP_AUTH")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private int authLv; // 권한 수준

    @Column(name = "AUTH_ORD", nullable = false)
    private int authOrd; // 권한 순서

    @Column(name = "USE_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPPER_AUTH_CD")
    private AuthMng upperAuthMng; // 상위 권한 엔티티

    @OneToMany(mappedBy = "upperAuthMng")
    private List<AuthMng> authMngList = new ArrayList<>(); // 하위 권한 엔티티

    @OneToMany(mappedBy = "authMng")
    private List<UserAuthMng> userAuthMng = new ArrayList<>(); // 사용자 권한 매핑 엔티티

    @Builder
    public AuthMng(String authCd, String authNm, String authDesc, int authLv, int authOrd,
        UseStatus useYn) {
        this.authCd = authCd;
        this.authNm = authNm;
        this.authDesc = authDesc;
        this.authLv = authLv;
        this.authOrd = authOrd;
        this.useYn = useYn;
    }

    // 최하위 자식 노드 여부 조회
    public String getLastYn() {
        return authMngList.size() > 0 ? "N" : "Y";
    }
}
