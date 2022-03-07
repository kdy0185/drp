package com.jsplan.drp.domain.sys.menumng.entity;

import java.io.Serializable;
import java.util.List;

import com.jsplan.drp.global.obj.entity.ComsVO;

/**
 * @Class : MenuMngVO
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 메뉴 관리 VO
 */
public class MenuMngVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = -8817935986757848866L;

    /* 목록 */
    private String menuCd; // 메뉴 코드
    private String upperMenuCd; // 상위 메뉴 코드
    private String menuNm; // 메뉴명
    private String menuEngNm; // 메뉴명(영문)
    private String menuUrl; // 이동 주소
    private String menuDesc; // 메뉴 설명
    private String menuLv; // 메뉴 수준
    private String menuOrd; // 메뉴 순서
    private String useYn; // 사용 여부
    private List<String> menuCdList; // 메뉴 코드 목록

    /* 권한 */
    private String authCd; // 권한 코드
    private String authNm; // 권한명
    private String authYn; // 권한 여부
    private String lastYn; // 최하위 자식 노드 여부

    public String getMenuCd() {
        return menuCd;
    }

    public void setMenuCd(String menuCd) {
        this.menuCd = menuCd;
    }

    public String getUpperMenuCd() {
        return upperMenuCd;
    }

    public void setUpperMenuCd(String upperMenuCd) {
        this.upperMenuCd = upperMenuCd;
    }

    public String getMenuNm() { return menuNm; }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }

    public String getMenuEngNm() {
        return menuEngNm;
    }

    public void setMenuEngNm(String menuEngNm) {
        this.menuEngNm = menuEngNm;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getMenuLv() {
        return menuLv;
    }

    public void setMenuLv(String menuLv) { this.menuLv = menuLv; }

    public String getMenuOrd() {
        return menuOrd;
    }

    public void setMenuOrd(String menuOrd) { this.menuOrd = menuOrd; }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public List<String> getMenuCdList() {
        return menuCdList;
    }

    public void setMenuCdList(List<String> menuCdList) {
        this.menuCdList = menuCdList;
    }

    public String getAuthCd() {
        return authCd;
    }

    public void setAuthCd(String authCd) { this.authCd = authCd; }

    public String getAuthNm() {
        return authNm;
    }

    public void setAuthNm(String authNm) {
        this.authNm = authNm;
    }

    public String getAuthYn() {
        return authYn;
    }

    public void setAuthYn(String authYn) {
        this.authYn = authYn;
    }

    public String getLastYn() {
        return lastYn;
    }

    public void setLastYn(String lastYn) {
        this.lastYn = lastYn;
    }

}
