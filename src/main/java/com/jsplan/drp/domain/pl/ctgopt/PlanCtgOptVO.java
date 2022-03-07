package com.jsplan.drp.domain.pl.ctgopt;

import com.jsplan.drp.global.obj.entity.ComsVO;
import java.io.Serializable;

/**
 * @Class : PlanCtgOptVO
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 분류 옵션 설정 VO
 */
public class PlanCtgOptVO extends ComsVO implements Serializable {

    private static final long serialVersionUID = 381458884010087415L;

    /* 목록 */
    private String rtneCtgCd; // 루틴 분류 코드
    private String upperRtneCtgCd; // 상위 루틴 분류 코드
    private String rtneCtgNm; // 루틴 분류명
    private String wtVal; // 가중치
    private String recgMinTime; // 최소 권장 시간
    private String recgMaxTime; // 최대 권장 시간
    private String rtneStartDate; // 적용 시작 일자
    private String rtneEndDate; // 적용 종료 일자
    private String useYn; // 사용 여부
    private String planUser; // 담당자
    private String lastYn; // 최하위 자식 노드 여부

    public String getRtneCtgCd() {
        return rtneCtgCd;
    }

    public void setRtneCtgCd(String rtneCtgCd) {
        this.rtneCtgCd = rtneCtgCd;
    }

    public String getUpperRtneCtgCd() {
        return upperRtneCtgCd;
    }

    public void setUpperRtneCtgCd(String upperRtneCtgCd) {
        this.upperRtneCtgCd = upperRtneCtgCd;
    }

    public String getRtneCtgNm() {
        return rtneCtgNm;
    }

    public void setRtneCtgNm(String rtneCtgNm) {
        this.rtneCtgNm = rtneCtgNm;
    }

    public String getWtVal() {
        return wtVal;
    }

    public void setWtVal(String wtVal) {
        this.wtVal = wtVal;
    }

    public String getRecgMinTime() {
        return recgMinTime;
    }

    public void setRecgMinTime(String recgMinTime) {
        this.recgMinTime = recgMinTime;
    }

    public String getRecgMaxTime() {
        return recgMaxTime;
    }

    public void setRecgMaxTime(String recgMaxTime) {
        this.recgMaxTime = recgMaxTime;
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

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getPlanUser() {
        return planUser;
    }

    public void setPlanUser(String planUser) {
        this.planUser = planUser;
    }

    public String getLastYn() {
        return lastYn;
    }

    public void setLastYn(String lastYn) {
        this.lastYn = lastYn;
    }
}
