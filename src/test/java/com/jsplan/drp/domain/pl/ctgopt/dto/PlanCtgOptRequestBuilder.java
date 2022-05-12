package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.global.obj.vo.UseStatus;

/**
 * @Class : PlanCtgOptRequestBuilder
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Request DTO Builder
 */
public class PlanCtgOptRequestBuilder {

    // Test 전용 생성자
    public static PlanCtgOptRequest build(String rtneCtgCd, String upperRtneCtgCd, String rtneCtgNm,
        Float wtVal, Integer recgMinTime, Integer recgMaxTime, UseStatus useYn, String userId) {
        return new PlanCtgOptRequest(rtneCtgCd, upperRtneCtgCd, rtneCtgNm, wtVal, recgMinTime,
            recgMaxTime, useYn, userId);
    }
}
