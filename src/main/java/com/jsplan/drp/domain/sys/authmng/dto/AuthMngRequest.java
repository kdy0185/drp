package com.jsplan.drp.domain.sys.authmng.dto;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMng;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : AuthMngRequest
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Request DTO
 */
@Getter
@Setter
public class AuthMngRequest {

    @NotBlank(message = "{js.valid.msg.required}")
    private String authCd; // 권한 코드
    private String upperAuthCd; // 상위 권한 코드

    @NotBlank(message = "{js.valid.msg.required}")
    private String authNm; // 권한명
    private String authDesc; // 권한 설명

    @Positive(message = "{js.valid.msg.digitsReq}")
    @NotNull(message = "{js.valid.msg.required}")
    private Integer authLv; // 권한 수준

    @Positive(message = "{js.valid.msg.digitsReq}")
    @NotNull(message = "{js.valid.msg.required}")
    private Integer authOrd; // 권한 순서

    private String state; // 등록/수정 (등록 : I, 수정 : U)

    // Test 전용 생성자
    AuthMngRequest(String authCd, String upperAuthCd, String authNm, String authDesc,
        Integer authLv, Integer authOrd) {
        this.authCd = authCd;
        this.upperAuthCd = upperAuthCd;
        this.authNm = authNm;
        this.authDesc = authDesc;
        this.authLv = authLv;
        this.authOrd = authOrd;
    }

    // Request DTO → Entity 변환
    public AuthMng toEntity() {
        return AuthMng.builder()
            .authCd(authCd)
            .upperAuthMng(AuthMng.builder().authCd(upperAuthCd).build())
            .authNm(authNm)
            .authDesc(authDesc)
            .authLv(authLv)
            .authOrd(authOrd)
            .build();
    }
}
