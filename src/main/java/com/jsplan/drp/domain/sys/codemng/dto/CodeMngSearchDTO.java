package com.jsplan.drp.domain.sys.codemng.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : CodeMngSearchDTO
 * @Author : KDW
 * @Date : 2022-05-02
 * @Description : 코드 관리 Search DTO
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CodeMngSearchDTO {

    private int pageNo; // 조회할 페이지 번호
    private int pageSize; // 페이지당 데이터 출력 수
    private String grpCd; // 그룹 코드
    private String comCd; // 공통 코드
    private String searchCd; // 검색 조건
    private String searchWord; // 검색어

    public CodeMngSearchDTO(String searchCd) {
        this.searchCd = searchCd;
    }
}
