package com.jsplan.drp.domain.sys.codemng.dto;

/**
 * @Class : CodeMngSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-05-03
 * @Description : 코드 관리 Search DTO Builder
 */
public class CodeMngSearchDTOBuilder {

    // Test 전용 생성자
    public static CodeMngSearchDTO build(int pageNo, int pageSize, String grpCd, String comCd,
        String searchCd, String searchWord) {
        return new CodeMngSearchDTO(pageNo, pageSize, grpCd, comCd, searchCd, searchWord);
    }
}
