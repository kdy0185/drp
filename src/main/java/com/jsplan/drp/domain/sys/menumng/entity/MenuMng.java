package com.jsplan.drp.domain.sys.menumng.entity;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequest;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import com.jsplan.drp.global.obj.vo.UseStatus;
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
 * @Class : MenuMng
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 관리 Entity
 */
@Entity
@Table(name = "SYS_DRP_MENU")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"menuCd", "menuNm", "menuEngNm", "menuUrl", "menuDesc", "menuLv", "menuOrd",
    "useYn"})
public class MenuMng extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "MENU_CD", length = 5)
    private String menuCd; // 메뉴 코드

    @Column(name = "UPPER_MENU_CD", length = 5)
    private String upperMenuCd; // 상위 메뉴 코드

    @Column(name = "MENU_NM", nullable = false)
    private String menuNm; // 메뉴명

    @Column(name = "MENU_ENG_NM")
    private String menuEngNm; // 메뉴 영문명

    @Column(name = "MENU_URL")
    private String menuUrl; // 이동 주소

    @Column(name = "MENU_DESC", length = 1000)
    private String menuDesc; // 메뉴 설명

    @Column(name = "MENU_LV", nullable = false)
    private Integer menuLv; // 메뉴 수준

    @Column(name = "MENU_ORD", nullable = false)
    private Integer menuOrd; // 메뉴 순서

    @Column(name = "USE_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPPER_MENU_CD", insertable = false, updatable = false)
    private MenuMng upperMenuMng; // 상위 메뉴 엔티티

    @OneToMany(mappedBy = "upperMenuMng")
    private List<MenuMng> menuMngList = new ArrayList<>(); // 하위 메뉴 엔티티

    @OneToMany(mappedBy = "menuMng", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuAuthMng> menuAuthMng = new ArrayList<>(); // 메뉴 권한 매핑 엔티티

    @Builder
    public MenuMng(String menuCd, MenuMng upperMenuMng, String menuNm, String menuEngNm,
        String menuUrl, String menuDesc, Integer menuLv, Integer menuOrd, UseStatus useYn,
        String menuAuthMng) {
        this.menuCd = menuCd;
        this.upperMenuMng = upperMenuMng;
        this.menuNm = menuNm;
        this.menuEngNm = menuEngNm;
        this.menuUrl = menuUrl;
        this.menuDesc = menuDesc;
        this.menuLv = menuLv;
        this.menuOrd = menuOrd;
        this.useYn = useYn;
        setMenuAuthMng(menuAuthMng);
    }

    @Override
    public String getId() {
        return menuCd;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }

    // 권한 설정
    private void setMenuAuthMng(String authCdList) {
        removeMenuAuthMng();
        Arrays.asList(StringUtil.split(authCdList)).forEach(this::addMenuAuthMng);
        setBaseMenuAuthMng();
    }

    // 기존 권한 제거
    private void removeMenuAuthMng() {
        if (this.menuAuthMng.size() > 0) {
            this.menuAuthMng.clear();
        }
    }

    // 권한 매핑 정보 추가
    private void addMenuAuthMng(String authCd) {
        MenuAuthMng menuAuthMng = MenuAuthMng.createMenuAuthMng(this,
            AuthMng.builder().authCd(authCd).build());
        this.menuAuthMng.add(menuAuthMng);
    }

    // 기본 인증 권한 설정
    private void setBaseMenuAuthMng() {
        // 메인 메뉴 : 반드시 인증 권한 추가 필요
        if ("A0000".equals(menuCd) || "A0100".equals(menuCd)) {
            addMenuAuthMng("IS_AUTHENTICATED_FULLY");
        }
        // 로그인 메뉴 : 반드시 익명 권한 추가 필요
        if ("A0200".equals(menuCd)) {
            addMenuAuthMng("ANONYMOUS");
        }
    }

    // 최하위 자식 노드 여부 조회
    public String getLastYn() {
        return menuMngList.size() > 0 ? "N" : "Y";
    }

    // 메뉴 수정
    public void updateMenuMng(MenuMngRequest request) {
        this.menuNm = request.getMenuNm();
        this.menuEngNm = request.getMenuEngNm();
        this.menuUrl = request.getMenuUrl();
        this.menuDesc = request.getMenuDesc();
        this.menuOrd = request.getMenuOrd();
        this.useYn = request.getUseYn();
        setMenuAuthMng(request.getAuthCd());
    }

    // 권한 매핑 정보 수정
    public void updateMenuAuthMng(String authCdList) {
        setMenuAuthMng(authCdList);
    }
}
