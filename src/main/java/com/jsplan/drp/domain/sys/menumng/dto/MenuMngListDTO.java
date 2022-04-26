package com.jsplan.drp.domain.sys.menumng.dto;

import com.jsplan.drp.global.obj.entity.BaseListDTO;
import com.jsplan.drp.global.obj.entity.UseStatus;
import lombok.Getter;

/**
 * @Class : MenuMngListDTO
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 목록 DTO
 */
@Getter
public class MenuMngListDTO extends BaseListDTO {

    private final String menuCd; // 메뉴 코드
    private final String menuNm; // 메뉴명
    private final String menuUrl; // 이동 주소
    private final String menuDesc; // 메뉴 설명
    private final Integer menuLv; // 메뉴 수준
    private final Integer menuOrd; // 메뉴 순서
    private final String useYn; // 사용 여부
    private final String lastYn; // 최하위 자식 노드 여부

    public MenuMngListDTO(String menuCd, String menuNm, String menuUrl, String menuDesc,
        Integer menuLv, Integer menuOrd, UseStatus useYn, String lastYn) {
        this.menuCd = menuCd;
        this.menuNm = menuNm;
        this.menuUrl = menuUrl;
        this.menuDesc = menuDesc;
        this.menuLv = menuLv;
        this.menuOrd = menuOrd;
        this.useYn = useYn.getTitle();
        this.lastYn = lastYn;
    }
}
