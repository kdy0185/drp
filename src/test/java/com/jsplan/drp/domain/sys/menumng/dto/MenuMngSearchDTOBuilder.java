package com.jsplan.drp.domain.sys.menumng.dto;

import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;

/**
 * @Class : MenuMngSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-04-26
 * @Description : 메뉴 관리 Search DTO Builder
 */
public class MenuMngSearchDTOBuilder {

    // Test 전용 생성자
    public static MenuMngSearchDTO build(String menuCd, String searchCd, String searchWord,
        UseStatus useYn) {
        return new MenuMngSearchDTO(menuCd, searchCd, searchWord, useYn);
    }
}
