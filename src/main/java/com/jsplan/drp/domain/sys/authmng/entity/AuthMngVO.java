package com.jsplan.drp.domain.sys.authmng.entity;

import com.jsplan.drp.global.obj.entity.ComsVO;
import java.io.Serializable;
import java.util.List;

/**
 * @Class : AuthMngVO
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 권한 관리 VO
 */
public class AuthMngVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = 8630605812002570816L;

    /* 권한 */
    private String authCd; // 권한 코드
    private String upperAuthCd; // 상위 권한 코드
    private String authNm; // 권한명
    private String authDesc; // 권한 설명
    private String authLv; // 권한 수준
    private String authOrd; // 권한 순서
    private String useYn; // 사용 여부
    private String authYn; // 권한 여부
    private List<String> authCdList; // 권한 코드 목록

    /* 사용자 */
    private String grpCd; // 그룹 코드
    private String userId; // 사용자 아이디
    private String userNm; // 성명
    private String mobileNum; // 휴대폰 번호

    /* 메뉴 */
    private String menuCd; // 메뉴 코드
    private String menuNm; // 메뉴명
    private String lastYn; // 최하위 자식 노드 여부

    public String getAuthCd() { return authCd; }

    public void setAuthCd(String authCd) {
        this.authCd = authCd;
    }

    public String getUpperAuthCd() {
        return upperAuthCd;
    }

    public void setUpperAuthCd(String upperAuthCd) {
        this.upperAuthCd = upperAuthCd;
    }

    public String getAuthNm() {
        return authNm;
    }

    public void setAuthNm(String authNm) {
        this.authNm = authNm;
    }

    public String getAuthDesc() {
        return authDesc;
    }

    public void setAuthDesc(String authDesc) {
        this.authDesc = authDesc;
    }

    public String getAuthLv() {
        return authLv;
    }

    public void setAuthLv(String authLv) {
        this.authLv = authLv;
    }

    public String getAuthOrd() { return authOrd; }

    public void setAuthOrd(String authOrd) {
        this.authOrd = authOrd;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getAuthYn() {
        return authYn;
    }

    public void setAuthYn(String authYn) {
        this.authYn = authYn;
    }

    public List<String> getAuthCdList() { return authCdList; }

    public void setAuthCdList(List<String> authCdList) {
        this.authCdList = authCdList;
    }

    public String getGrpId() {
        return grpCd;
    }

    public void setGrpId(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getMenuCd() {
        return menuCd;
    }

    public void setMenuCd(String menuCd) {
        this.menuCd = menuCd;
    }

    public String getMenuNm() {
        return menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }

    public String getLastYn() {
        return lastYn;
    }

    public void setLastYn(String lastYn) {
        this.lastYn = lastYn;
    }

}
