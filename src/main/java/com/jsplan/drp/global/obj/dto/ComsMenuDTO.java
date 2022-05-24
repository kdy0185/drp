package com.jsplan.drp.global.obj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : ComsMenuDTO
 * @Author : KDW
 * @Date : 2022-05-20
 * @Description : 공통 메뉴 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ComsMenuDTO {

    private String menuCd; // 메뉴 코드
    private String menuNm; // 메뉴명
    private String menuEngNm; // 메뉴명(영문)
    private String upperMenuCd; // 상위 메뉴 코드
    private String upperMenuNm; // 상위 메뉴명
    private String menuUrl; // 이동 주소
    private Integer menuLv; // 메뉴 수준
    private String lastYn; // 하위 메뉴 존재 여부
    private String authCd; // 권한 코드
    private String upperAuthCd; // 상위 권한 코드

    // 메뉴 목록
    public ComsMenuDTO(String menuCd, String menuNm, String upperMenuCd, String menuUrl,
        Integer menuLv, String lastYn, String authCd) {
        this.menuCd = menuCd;
        this.menuNm = menuNm;
        this.upperMenuCd = upperMenuCd;
        this.menuUrl = menuUrl;
        this.menuLv = menuLv;
        this.lastYn = lastYn;
        this.authCd = authCd;
    }
}
