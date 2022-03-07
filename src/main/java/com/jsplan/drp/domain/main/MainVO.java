package com.jsplan.drp.domain.main;

import com.jsplan.drp.global.obj.entity.ComsVO;
import java.io.Serializable;

/**
 * @Class : MainVO
 * @Author : KDW
 * @Date : 2022-01-19
 * @Description : 메인 화면 VO
 */

public class MainVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = 7342082196562951373L;

    /* 정보 수정 */
    private String grpCd; // 그룹 코드
    private String grpNm; // 그룹명
    private String userId; // 사용자 아이디
    private String userNm; // 성명
    private String userPw; // 비밀번호
    private String userPwDup; // 비밀번호 확인
    private String mobileNum; // 휴대폰 번호
    private String email; // 이메일
    private String userType; // 사용자 유형
    private String useYn; // 사용 여부
    private String regDate; // 등록 일시
    private String modDate; // 수정 일시

    @Override
    public String getGrpCd() {
        return grpCd;
    }

    @Override
    public void setGrpCd(String grpCd) {
        this.grpCd = grpCd;
    }

    @Override
    public String getGrpNm() {
        return grpNm;
    }

    @Override
    public void setGrpNm(String grpNm) {
        this.grpNm = grpNm;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserNm() {
        return userNm;
    }

    @Override
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

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }
}
