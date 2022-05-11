package com.jsplan.drp.domain.sys.menumng.dto;

import com.jsplan.drp.global.obj.vo.DetailStatus;
import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Class : MenuMngDetailDTO
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 상세 DTO
 */
@Getter
@NoArgsConstructor
public class MenuMngDetailDTO {

    private String menuCd; // 메뉴 코드
    private String upperMenuCd; // 상위 메뉴 코드
    private String menuNm; // 메뉴명
    private String menuEngNm; // 메뉴 영문명
    private String menuUrl; // 이동 주소
    private String menuDesc; // 메뉴 설명
    private Integer menuLv; // 메뉴 수준
    private Integer menuOrd; // 메뉴 순서
    private String useYn; // 사용 여부
    private String authCd; // 권한 코드
    private DetailStatus detailStatus; // 등록/수정 구분

    // 상세 조회 시 구분값 설정
    public void setDetailStatus(DetailStatus detailStatus) {
        this.detailStatus = detailStatus;
    }

    // 권한 설정 팝업 화면 : 메뉴 코드 설정
    public MenuMngDetailDTO(String menuCdList) {
        menuCd = menuCdList;
    }

    @QueryProjection
    public MenuMngDetailDTO(String menuCd, String upperMenuCd, String menuNm, String menuEngNm,
        String menuUrl, String menuDesc, Integer menuLv, Integer menuOrd, UseStatus useYn) {
        this.menuCd = menuCd;
        this.upperMenuCd = upperMenuCd;
        this.menuNm = menuNm;
        this.menuEngNm = menuEngNm;
        this.menuUrl = menuUrl;
        this.menuDesc = menuDesc;
        this.menuLv = menuLv;
        this.menuOrd = menuOrd;
        this.useYn = useYn.getCode();
    }
}
