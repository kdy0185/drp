package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.entity.DataStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : UserMngResponse
 * @Author : KDW
 * @Date : 2022-04-18
 * @Description : 사용자 관리 Response DTO
 */
@Getter
@RequiredArgsConstructor
public class UserMngResponse {

    private final String userId; // 사용자 아이디
    private final DataStatus dataStatus; // 응답 상태
}
