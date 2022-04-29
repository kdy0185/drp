package com.jsplan.drp.domain.sys.usermng.entity;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : UserAuthMng
 * @Author : KDW
 * @Date : 2022-04-20
 * @Description : 사용자 권한 매핑 Entity
 */
@Entity
@Table(name = "SYS_DRP_USER_AUTH")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"userMng", "authMng"})
public class UserAuthMng {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // 일련번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserMng userMng; // 사용자 엔티티

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTH_CD")
    private AuthMng authMng; // 권한 엔티티

    // 사용자 권한 매핑 정보 생성
    public static UserAuthMng createUserAuthMng(UserMng userMng, AuthMng authMng) {
        UserAuthMng userAuthMng = new UserAuthMng();
        userAuthMng.setUserMng(userMng);
        userAuthMng.setAuthMng(authMng);
        return userAuthMng;
    }
}
