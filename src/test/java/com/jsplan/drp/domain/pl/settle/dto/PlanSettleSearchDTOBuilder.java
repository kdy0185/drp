package com.jsplan.drp.domain.pl.settle.dto;

/**
 * @Class : PlanSettleSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-05-18
 * @Description : 일일 결산 Search DTO Builder
 */
public class PlanSettleSearchDTOBuilder {

    // Test 전용 생성자 : 일일 결산 목록
    public static PlanSettleSearchDTO listBuild(int pageNo, int pageSize, String userId,
        String userNm, String rtneStartDate, String rtneEndDate) {
        return new PlanSettleSearchDTO(pageNo, pageSize, userId, userNm, rtneStartDate,
            rtneEndDate);
    }

    // Test 전용 생성자 : 일일 결산 상세
    public static PlanSettleSearchDTO detailBuild(String rtneDate, String planUser) {
        return new PlanSettleSearchDTO(rtneDate, planUser);
    }
}
