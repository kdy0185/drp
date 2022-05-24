package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.global.obj.vo.UseStatus;

/**
 * @Class : PlanCtgOptSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Search DTO Builder
 */
public class PlanCtgOptSearchDTOBuilder {

    // Test 전용 생성자
    public static PlanCtgOptSearchDTO build(String rtneCtgCd, String userId, String userNm,
        UseStatus useYn) {
        return new PlanCtgOptSearchDTO(rtneCtgCd, userId, userNm, useYn);
    }
}
