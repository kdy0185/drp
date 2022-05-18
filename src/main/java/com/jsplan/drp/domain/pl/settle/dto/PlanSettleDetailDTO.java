package com.jsplan.drp.domain.pl.settle.dto;

import com.jsplan.drp.global.obj.dto.BaseListDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Class : PlanSettleDetailDTO
 * @Author : KDW
 * @Date : 2022-05-17
 * @Description : 일일 결산 상세 DTO
 */
@Getter
@NoArgsConstructor
public class PlanSettleDetailDTO extends BaseListDTO {

    private String rtneCtgCd; // 루틴 분류 코드
    private String rtneCtgNm; // 루틴 분류명
    private Integer rtneAssignCnt; // 할당 시간(분)
    private String rtneAssignTime; // 할당 시간
    private String rtneAssignPer; // 할당 비율
    private String achvRate; // 달성률
    private String concRate; // 몰입도
    private Long rtneCnt; // 일과 수

    private String rtneDate; // 루틴 일자
    private String planUser; // 담당자
    private String settleType; // 상세 항목 구분

    // 상세 조회 시 루틴 일자 설정
    public void setRtneDate(String rtneDate) {
        this.rtneDate = rtneDate;
    }

    // 생성자 : 분류별 할당 시간
    public PlanSettleDetailDTO(String rtneCtgCd, Integer rtneAssignCnt, String rtneAssignTime,
        Long rtneCnt) {
        this.rtneCtgCd = rtneCtgCd;
        this.rtneAssignCnt = rtneAssignCnt;
        this.rtneAssignTime = rtneAssignTime;
        this.rtneCnt = rtneCnt;
    }

    // 생성자 : 일과별 달성률
    public PlanSettleDetailDTO(Integer achvRate, Long rtneCnt) {
        this.achvRate = achvRate + "%";
        this.rtneCnt = rtneCnt;
    }

    // 생성자 : 일과별 몰입도
    public PlanSettleDetailDTO(String concRate, Long rtneCnt) {
        this.concRate = concRate;
        this.rtneCnt = rtneCnt;
    }

    // 루틴 분류명 추가
    public void addRtneCtgNm(String rtneCtgNm) {
        this.rtneCtgNm = rtneCtgNm;
    }

    // 할당 비율 추가
    public void addRtneAssignPer(Integer rtneAssignCnt, Integer rtneAssignSum) {
        this.rtneAssignPer =
            String.format("%.2f", rtneAssignCnt.floatValue() / rtneAssignSum * 100) + "%";
    }
}
