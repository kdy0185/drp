package com.jsplan.drp.domain.sys.usermng;

import java.io.Serializable;
import java.util.List;

import com.jsplan.drp.global.obj.ComsVO;

/**
 * @Class : UserMngVO
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 VO
 */
public class UserMngVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = -7654456040891308065L;

    /* 그룹 */
    private String grpCd; // 그룹 코드
    private String grpNm; // 그룹명
    private String grpDesc; // 그룹 설명

    /* 사용자 */
    private String userId; // 사용자 아이디
    private String userNm; // 성명
    private String userPw; // 비밀번호
    private String userPwDup; // 비밀번호 확인
    private String mobileNum; // 휴대폰 번호
    private String email; // 이메일
    private String userType; // 사용자 유형
    private String useYn; // 사용 여부
    private List<String> userIdList; // 사용자 아이디 목록

    /* 권한 */
    private String authCd; // 권한 코드
    private String upperAuthCd; // 상위 권한 코드
    private String authNm; // 권한명
    private String authLv; // 권한 레벨
    private String authYn; // 권한 보유 여부
    private String lastYn; // 최하위 자식 노드 여부

    private String regUser; // 등록자
    private String regDate; // 등록 일시
    private String modUser; // 수정자
    private String modDate; // 수정 일시

    public String getGrpCd() {
        return grpCd;
    }

    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    public String getGrpNm() {
        return grpNm;
    }

    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    public String getGrpDesc() {
        return grpDesc;
    }

    public void setGrpDesc(String grpDesc) {
        this.grpDesc = grpDesc;
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

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getUserPwDup() {
        return userPwDup;
    }

    public void setUserPwDup(String userPwDup) {
        this.userPwDup = userPwDup;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public String getAuthCd() {
        return authCd;
    }

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

    public String getAuthLv() {
        return authLv;
    }

    public void setAuthLv(String authLv) {
        this.authLv = authLv;
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

    public String getRegUser() {
        return regUser;
    }

    public void setRegUser(String regUser) {
        this.regUser = regUser;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getModUser() {
        return modUser;
    }

    public void setModUser(String modUser) {
        this.modUser = modUser;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }
}
