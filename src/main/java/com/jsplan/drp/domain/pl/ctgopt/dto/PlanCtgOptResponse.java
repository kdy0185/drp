package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.global.obj.vo.DataStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : PlanCtgOptResponse
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Response DTO
 */
@Getter
@RequiredArgsConstructor
public class PlanCtgOptResponse {

    private final String rtneCtgCd; // 루틴 분류 코드
    private final DataStatus dataStatus; // 응답 상태
}
