package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.vo.DataStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : UserGrpMngResponse
 * @Author : KDW
 * @Date : 2022-03-21
 * @Description : 그룹 관리 Response DTO
 */
@Getter
@RequiredArgsConstructor
public class UserGrpMngResponse {

    private final String grpCd; // 그룹 코드
    private final DataStatus dataStatus; // 응답 상태
}
