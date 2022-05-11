package com.jsplan.drp.domain.sys.codemng.dto;

import com.jsplan.drp.global.obj.vo.UseStatus;

/**
 * @Class : CodeMngRequestBuilder
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 공통 코드 Request DTO Builder
 */
public class CodeMngRequestBuilder {

    // Test 전용 생성자 : 개별 공통 코드
    public static CodeMngRequest build(String grpCd, String comCd, String comNm, UseStatus useYn,
        String detl, Integer ord) {
        return new CodeMngRequest(grpCd, comCd, comNm, useYn, detl, ord);
    }

    // Test 전용 생성자 : 전체 공통 코드
    public static CodeMngRequest jsonBuild(String jsonData) {
        return new CodeMngRequest(jsonData);
    }
}
