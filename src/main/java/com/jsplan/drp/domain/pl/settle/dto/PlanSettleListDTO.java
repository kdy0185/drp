package com.jsplan.drp.domain.pl.settle.dto;

import com.jsplan.drp.global.obj.dto.BaseListDTO;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Class : PlanSettleListDTO
 * @Author : KDW
 * @Date : 2022-05-17
 * @Description : 일일 결산 목록 DTO
 */
@Getter
@NoArgsConstructor
public class PlanSettleListDTO extends BaseListDTO {

    private Long dailyId; // 일련번호
    private LocalDate rtneDate; // 루틴 일자
    private String achvRate; // 달성률
    private String concRate; // 몰입도
    private Double achvRateNum; // 달성률 수치
    private Double concRateNum; // 몰입도 수치
    private Float dailyScore; // 점수
    private String memo; // 메모
    private String planUser; // 담당자

    // 달성률, 몰입도 추가
    public void addRateInfo(PlanSettleListDTO rateInfo) {
        this.achvRate = String.format("%.1f", rateInfo.getAchvRateNum()) + "%";
        this.concRate = String.format("%.2f", rateInfo.getConcRateNum()) + " / 5";
    }

    @QueryProjection
    public PlanSettleListDTO(Long dailyId, LocalDate rtneDate, Float dailyScore, String memo,
        String planUser) {
        this.dailyId = dailyId;
        this.rtneDate = rtneDate;
        this.dailyScore = dailyScore;
        this.memo = memo;
        this.planUser = planUser;
    }
}
