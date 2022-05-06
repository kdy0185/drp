package com.jsplan.drp.domain.sys.codemng.dto;

import com.jsplan.drp.global.obj.entity.UseStatus;

/**
 * @Class : CodeGrpMngRequestBuilder
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 그룹 코드 Request DTO Builder
 */
public class CodeGrpMngRequestBuilder {

    // Test 전용 생성자 : 개별 그룹 코드
    public static CodeGrpMngRequest build(String grpCd, String grpNm, UseStatus useYn,
        String detl) {
        return new CodeGrpMngRequest(grpCd, grpNm, useYn, detl);
    }

    // Test 전용 생성자 : 전체 그룹 코드
    public static CodeGrpMngRequest jsonBuild(String jsonData) {
        return new CodeGrpMngRequest(jsonData);
    }
}
