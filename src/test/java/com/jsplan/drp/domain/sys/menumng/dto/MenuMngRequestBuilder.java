package com.jsplan.drp.domain.sys.menumng.dto;

import com.jsplan.drp.global.obj.vo.UseStatus;

/**
 * @Class : MenuMngRequestBuilder
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 관리 Request DTO Builder
 */
public class MenuMngRequestBuilder {

    // Test 전용 생성자
    public static MenuMngRequest build(String menuCd, String upperMenuCd, String menuNm,
        String menuEngNm, String menuUrl, String menuDesc, Integer menuLv, Integer menuOrd,
        UseStatus useYn, String authCd) {
        return new MenuMngRequest(menuCd, upperMenuCd, menuNm, menuEngNm, menuUrl, menuDesc, menuLv,
            menuOrd, useYn, authCd);
    }
}
