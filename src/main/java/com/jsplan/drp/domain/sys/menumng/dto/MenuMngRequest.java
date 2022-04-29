package com.jsplan.drp.domain.sys.menumng.dto;

import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.global.obj.entity.UseStatus;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : MenuMngRequest
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 관리 Request DTO
 */
@Getter
@Setter
public class MenuMngRequest {

    @NotBlank(message = "{js.valid.msg.required}")
    private String menuCd; // 메뉴 코드

    private String upperMenuCd; // 상위 메뉴 코드

    @NotBlank(message = "{js.valid.msg.required}")
    private String menuNm; // 메뉴명

    private String menuEngNm; // 메뉴 영문명

    private String menuUrl; // 이동 주소

    private String menuDesc; // 메뉴 설명

    @Positive(message = "{js.valid.msg.digitsReq}")
    @NotNull(message = "{js.valid.msg.required}")
    private Integer menuLv; // 메뉴 수준

    @Positive(message = "{js.valid.msg.digitsReq}")
    @NotNull(message = "{js.valid.msg.required}")
    private Integer menuOrd; // 메뉴 순서

    @NotNull(message = "{js.valid.msg.required}")
    private UseStatus useYn; // 사용 여부

    private String authCd; // 권한 코드

    private String state; // 등록/수정 (등록 : I, 수정 : U)

    // Test 전용 생성자
    MenuMngRequest(String menuCd, String upperMenuCd, String menuNm, String menuEngNm,
        String menuUrl, String menuDesc, Integer menuLv, Integer menuOrd, UseStatus useYn,
        String authCd) {
        this.menuCd = menuCd;
        this.upperMenuCd = upperMenuCd;
        this.menuNm = menuNm;
        this.menuEngNm = menuEngNm;
        this.menuUrl = menuUrl;
        this.menuDesc = menuDesc;
        this.menuLv = menuLv;
        this.menuOrd = menuOrd;
        this.useYn = useYn;
        this.authCd = authCd;
    }

    // Request DTO → Entity 변환
    public MenuMng toEntity() {
        return MenuMng.builder()
            .menuCd(menuCd)
            .upperMenuMng(MenuMng.builder().menuCd(upperMenuCd).build())
            .menuNm(menuNm)
            .menuEngNm(menuEngNm)
            .menuUrl(menuUrl)
            .menuDesc(menuDesc)
            .menuLv(menuLv)
            .menuOrd(menuOrd)
            .useYn(useYn)
            .menuAuthMng(authCd)
            .build();
    }
}
