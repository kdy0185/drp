package com.jsplan.drp.domain.sys.menumng.dto;

import com.jsplan.drp.global.obj.entity.DataStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Class : MenuMngResponse
 * @Author : KDW
 * @Date : 2022-04-26
 * @Description : 메뉴 관리 Response DTO
 */
@Getter
@RequiredArgsConstructor
public class MenuMngResponse {

    private final String menuCd; // 메뉴 코드
    private final DataStatus dataStatus; // 응답 상태
}
