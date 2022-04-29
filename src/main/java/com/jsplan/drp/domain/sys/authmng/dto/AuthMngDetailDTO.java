package com.jsplan.drp.domain.sys.authmng.dto;

import com.jsplan.drp.global.obj.entity.UseStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Class : AuthMngDetailDTO
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 상세 DTO
 */
@Getter
@NoArgsConstructor
public class AuthMngDetailDTO {

    private String authCd; // 권한 코드
    private String upperAuthCd; // 상위 권한 코드
    private String authNm; // 권한명
    private String authDesc; // 권한 설명
    private Integer authLv; // 권한 수준
    private Integer authOrd; // 권한 순서
    private String useYn; // 사용 여부
    private String state; // 등록/수정 (등록 : I, 수정 : U)

    // 상세 조회 시 구분값 설정
    public void setState(String state) {
        this.state = state;
    }

    // 메뉴 설정 팝업 화면 : 권한 설정
    public AuthMngDetailDTO(String authCdList) {
        authCd = authCdList;
    }

    @QueryProjection
    public AuthMngDetailDTO(String authCd, String upperAuthCd, String authNm, String authDesc,
        Integer authLv, Integer authOrd, UseStatus useYn) {
        this.authCd = authCd;
        this.upperAuthCd = upperAuthCd;
        this.authNm = authNm;
        this.authDesc = authDesc;
        this.authLv = authLv;
        this.authOrd = authOrd;
        this.useYn = useYn.getCode();
    }
}
