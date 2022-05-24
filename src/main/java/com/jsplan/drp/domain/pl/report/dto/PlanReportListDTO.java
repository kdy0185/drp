package com.jsplan.drp.domain.pl.report.dto;

import com.jsplan.drp.global.obj.dto.BaseListDTO;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

/**
 * @Class : PlanReportListDTO
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 데일리 리포트 목록 DTO
 */
@Getter
public class PlanReportListDTO extends BaseListDTO {

    private Long rtneId; // 일련번호
    private LocalDate rtneDate; // 루틴 일자
    private Integer rtneOrd; // 루틴 순서
    private String rtneDateTime; // 루틴 일시
    private String rtneCtgCd; // 루틴 분류 코드
    private final String rtneCtgNm; // 루틴 분류명
    private String rtneNm; // 루틴명
    private String achvRate; // 달성률
    private String concRate; // 몰입도
    private String planUser; // 담당자

    public PlanReportListDTO(String rtneCtgCd, String rtneCtgNm) {
        this.rtneCtgCd = rtneCtgCd;
        this.rtneCtgNm = rtneCtgNm;
    }

    @QueryProjection
    public PlanReportListDTO(Long rtneId, LocalDate rtneDate, Integer rtneOrd,
        LocalDateTime rtneStartTime, LocalDateTime rtneEndTime, String rtneCtgNm, String rtneNm,
        Integer achvRate, String concRate, String planUser) {
        this.rtneId = rtneId;
        this.rtneDate = rtneDate;
        this.rtneOrd = rtneOrd;
        this.rtneDateTime =
            rtneStartTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " ~ "
                + rtneEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.rtneCtgNm = rtneCtgNm;
        this.rtneNm = rtneNm;
        this.achvRate = achvRate + "%";
        this.concRate = concRate;
        this.planUser = planUser;
    }
}
