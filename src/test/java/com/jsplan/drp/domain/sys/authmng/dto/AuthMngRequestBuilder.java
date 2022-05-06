package com.jsplan.drp.domain.sys.authmng.dto;

/**
 * @Class : AuthMngRequestBuilder
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Request DTO Builder
 */
public class AuthMngRequestBuilder {

    // Test 전용 생성자
    public static AuthMngRequest build(String authCd, String upperAuthCd, String authNm,
        String authDesc, Integer authLv, Integer authOrd) {
        return new AuthMngRequest(authCd, upperAuthCd, authNm, authDesc, authLv, authOrd);
    }
}
