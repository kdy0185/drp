package com.jsplan.drp.domain.pl.report;

import com.jsplan.drp.global.obj.ComsVO;
import java.io.Serializable;

/**
 * @Class : PlanReportVO
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 데일리 리포트 VO
 */
public class PlanReportVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = -8608124348183797962L;

    /* 목록 */
    private String rtneCd; // 루틴 코드
    private String rtneDate; // 루틴 일자
    private String rtneOrd; // 루틴 순서
    private String rtneCtgCd; // 루틴 분류 코드
    private String rtneCtgNm; // 루틴 분류명
    private String rtneStartDate; // 루틴 시작 일시
    private String rtneEndDate; // 루틴 종료 일시
    private String rtneNm; // 루틴명
    private String achvRate; // 달성률
    private String concRate; // 몰입도
    private String mainYn; // 주요 일정 여부
    private String planUser; // 담당자
    private String regUser; // 등록자
    private String regDate; // 등록 일시
    private String modUser; // 수정자
    private String modDate; // 수정 일시

    public String getRtneCd() {
        return rtneCd;
    }

    public void setRtneCd(String rtneCd) {
        this.rtneCd = rtneCd;
    }

    public String getRtneDate() {
        return rtneDate;
    }

    public void setRtneDate(String rtneDate) {
        this.rtneDate = rtneDate;
    }

    public String getRtneOrd() {
        return rtneOrd;
    }

    public void setRtneOrd(String rtneOrd) {
        this.rtneOrd = rtneOrd;
    }

    public String getRtneCtgCd() {
        return rtneCtgCd;
    }

    public void setRtneCtgCd(String rtneCtgCd) {
        this.rtneCtgCd = rtneCtgCd;
    }

    public String getRtneCtgNm() {
        return rtneCtgNm;
    }

    public void setRtneCtgNm(String rtneCtgNm) {
        this.rtneCtgNm = rtneCtgNm;
    }

    public String getRtneStartDate() {
        return rtneStartDate;
    }

    public void setRtneStartDate(String rtneStartDate) {
        this.rtneStartDate = rtneStartDate;
    }

    public String getRtneEndDate() {
        return rtneEndDate;
    }

    public void setRtneEndDate(String rtneEndDate) {
        this.rtneEndDate = rtneEndDate;
    }

    public String getRtneNm() {
        return rtneNm;
    }

    public void setRtneNm(String rtneNm) {
        this.rtneNm = rtneNm;
    }

    public String getAchvRate() {
        return achvRate;
    }

    public void setAchvRate(String achvRate) {
        this.achvRate = achvRate;
    }

    public String getConcRate() {
        return concRate;
    }

    public void setConcRate(String concRate) {
        this.concRate = concRate;
    }

    public String getMainYn() {
        return mainYn;
    }

    public void setMainYn(String mainYn) {
        this.mainYn = mainYn;
    }

    public String getPlanUser() {
        return planUser;
    }

    public void setPlanUser(String planUser) {
        this.planUser = planUser;
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
