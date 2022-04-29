package com.jsplan.drp.domain.sys.authmng.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Class : AuthMenuMngListDTO
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한별 메뉴 목록 DTO
 */
@Getter
@AllArgsConstructor
@ToString(of = {"menuCd", "menuNm", "authYn", "lastYn"})
public class AuthMenuMngListDTO {

    private String menuCd; // 사용자 아이디
    private String menuNm; // 성명
    private String authYn; // 권한 여부
    private String lastYn; // 최하위 자식 노드 여부
}
