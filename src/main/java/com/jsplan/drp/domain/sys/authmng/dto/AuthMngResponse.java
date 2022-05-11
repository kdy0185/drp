package com.jsplan.drp.domain.sys.authmng.dto;

import com.jsplan.drp.global.obj.vo.DataStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : AuthMngResponse
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Response DTO
 */
@Getter
@RequiredArgsConstructor
public class AuthMngResponse {

    private final String authCd; // 권한 코드
    private final DataStatus dataStatus; // 응답 상태
}
