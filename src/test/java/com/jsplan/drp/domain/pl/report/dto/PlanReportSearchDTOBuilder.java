package com.jsplan.drp.domain.pl.report.dto;

/**
 * @Class : PlanReportSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 데일리 리포트 Search DTO Builder
 */
public class PlanReportSearchDTOBuilder {

    // Test 전용 생성자
    public static PlanReportSearchDTO build(int pageNo, int pageSize, String userId, String userNm,
        String rtneStartDate, String rtneEndDate, String rtneCtgCd, String rtneNm) {
        return new PlanReportSearchDTO(pageNo, pageSize, userId, userNm, rtneStartDate, rtneEndDate,
            rtneCtgCd, rtneNm);
    }
}
