package com.jsplan.drp.pl.settle;

import com.jsplan.drp.cmmn.obj.ComsVO;
import java.io.Serializable;

/**
 * @Class : PlanSettleVO
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 일일 결산 VO
 */
public class PlanSettleVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = 164615697149093814L;

    /* 목록 */
    private String rtneDate; // 루틴 일자
    private String rtneStartDate; // 루틴 시작 일시
    private String rtneEndDate; // 루틴 종료 일시
    private String achvRate; // 달성률
    private String concRate; // 몰입도
    private String dailyScore; // 점수
    private String planUser; // 담당자
    private String memo; // 메모

    /* 상세 */
    private String settleType; // 결산 유형
    private String rtneCtgNm; // 루틴 분류명
    private String rtneAssignCnt; // 할당 시간(분)
    private String rtneAssignTime; // 할당 시간
    private String rtneAssignPer; // 할당 비율
    private String rtneCnt; // 일과 수

    public String getRtneDate() {
        return rtneDate;
    }

    public void setRtneDate(String rtneDate) {
        this.rtneDate = rtneDate;
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

    public String getDailyScore() {
        return dailyScore;
    }

    public void setDailyScore(String dailyScore) {
        this.dailyScore = dailyScore;
    }

    public String getPlanUser() {
        return planUser;
    }

    public void setPlanUser(String planUser) {
        this.planUser = planUser;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public String getRtneCtgNm() {
        return rtneCtgNm;
    }

    public void setRtneCtgNm(String rtneCtgNm) {
        this.rtneCtgNm = rtneCtgNm;
    }

    public String getRtneAssignCnt() {
        return rtneAssignCnt;
    }

    public void setRtneAssignCnt(String rtneAssignCnt) {
        this.rtneAssignCnt = rtneAssignCnt;
    }

    public String getRtneAssignTime() {
        return rtneAssignTime;
    }

    public void setRtneAssignTime(String rtneAssignTime) {
        this.rtneAssignTime = rtneAssignTime;
    }

    public String getRtneAssignPer() {
        return rtneAssignPer;
    }

    public void setRtneAssignPer(String rtneAssignPer) {
        this.rtneAssignPer = rtneAssignPer;
    }

    public String getRtneCnt() {
        return rtneCnt;
    }

    public void setRtneCnt(String rtneCnt) {
        this.rtneCnt = rtneCnt;
    }
}
