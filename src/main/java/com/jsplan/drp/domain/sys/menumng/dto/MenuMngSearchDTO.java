package com.jsplan.drp.domain.sys.menumng.dto;

import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : MenuMngSearchDTO
 * @Author : KDW
 * @Date : 2022-04-26
 * @Description : 메뉴 관리 Search DTO
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class MenuMngSearchDTO {

    private String menuCd; // 메뉴 코드
    private String searchCd; // 검색 조건
    private String searchWord; // 검색어
    private UseStatus useYn; // 사용 여부

    public MenuMngSearchDTO(String searchCd) {
        this.searchCd = searchCd;
    }
}
