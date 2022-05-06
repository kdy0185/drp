package com.jsplan.drp.domain.sys.menumng.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : MenuAuthMng
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 권한 매핑 Entity
 */
@Entity
@Table(name = "SYS_DRP_MENU_AUTH")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuAuthMng {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // 일련번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_CD")
    private MenuMng menuMng; // 메뉴 엔티티

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTH_CD")
    private AuthMng authMng; // 권한 엔티티

    // 메뉴 권한 매핑 정보 생성
    public static MenuAuthMng createMenuAuthMng(MenuMng menuMng, AuthMng authMng) {
        MenuAuthMng menuAuthMng = new MenuAuthMng();
        menuAuthMng.setMenuMng(menuMng);
        menuAuthMng.setAuthMng(authMng);
        return menuAuthMng;
    }
}
