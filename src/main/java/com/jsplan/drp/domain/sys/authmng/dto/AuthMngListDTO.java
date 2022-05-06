package com.jsplan.drp.domain.sys.authmng.dto;

import com.jsplan.drp.global.obj.entity.BaseListDTO;
import com.jsplan.drp.global.obj.entity.UseStatus;
import lombok.Getter;

/**
 * @Class : AuthMngListDTO
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 목록 DTO
 */
@Getter
public class AuthMngListDTO extends BaseListDTO {

    private final String authCd; // 권한 코드
    private final String authNm; // 권한명
    private String authDesc; // 권한 설명
    private Integer authLv; // 권한 수준
    private Integer authOrd; // 권한 순서
    private String useYn; // 사용 여부
    private String lastYn; // 최하위 자식 노드 여부

    // 상위 권한 목록 조회용 생성자
    public AuthMngListDTO(String authCd, String authNm) {
        this.authCd = authCd;
        this.authNm = authNm;
    }

    // 권한 목록 조회용 생성자
    public AuthMngListDTO(String authCd, String authNm, String authDesc, Integer authLv,
        Integer authOrd, UseStatus useYn, String lastYn) {
        this.authCd = authCd;
        this.authNm = authNm;
        this.authDesc = authDesc;
        this.authLv = authLv;
        this.authOrd = authOrd;
        this.useYn = useYn.getTitle();
        this.lastYn = lastYn;
    }
}
