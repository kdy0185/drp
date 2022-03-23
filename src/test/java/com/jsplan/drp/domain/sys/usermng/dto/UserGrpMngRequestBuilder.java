package com.jsplan.drp.domain.sys.usermng.dto;

/**
 * @Class : UserGrpMngRequestBuilder
 * @Author : KDW
 * @Date : 2022-03-23
 * @Description : 그룹 관리 Request Builder
 */
public class UserGrpMngRequestBuilder {

    // Test 전용 생성자
    public static UserGrpMngRequest build(String grpCd, String grpNm, String grpDesc) {
        return new UserGrpMngRequest(grpCd, grpNm, grpDesc);
    }
}
