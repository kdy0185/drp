package com.jsplan.drp.domain.sys.menumng.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Class : MenuAuthMngListDTO
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴별 권한 목록 DTO
 */
@Getter
@AllArgsConstructor
@ToString(of = {"authCd", "authNm", "authYn", "lastYn"})
public class MenuAuthMngListDTO {

    private String authCd; // 권한 코드
    private String authNm; // 권한명
    private String authYn; // 권한 여부
    private String lastYn; // 최하위 자식 노드 여부
}
