package com.jsplan.drp.domain.sys.authmng.dto;

import com.jsplan.drp.global.obj.vo.UseStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : AuthMngSearchDTO
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Search DTO
 */
@Getter
@Setter
public class AuthMngSearchDTO {

    private int pageNo; // 조회할 페이지 번호
    private int pageSize; // 페이지당 데이터 출력 수
    private String authCd; // 권한 코드
    private String searchCd; // 검색 조건
    private String searchWord; // 검색어
    private UseStatus useYn; // 사용 여부
    private String grpCd; // 그룹 코드
    private String menuCd; // 메뉴 코드

    public AuthMngSearchDTO(String searchCd) {
        this.searchCd = searchCd;
    }

    // Test 전용 생성자 : 권한 목록 조회
    AuthMngSearchDTO(String authCd, String searchCd, String searchWord, UseStatus useYn) {
        this.authCd = authCd;
        this.searchCd = searchCd;
        this.searchWord = searchWord;
        this.useYn = useYn;
    }

    // Test 전용 생성자 : 권한별 사용자 목록 조회
    AuthMngSearchDTO(int pageNo, int pageSize, String authCd, String grpCd, String searchCd,
        String searchWord) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.authCd = authCd;
        this.grpCd = grpCd;
        this.searchCd = searchCd;
        this.searchWord = searchWord;
    }

    // Test 전용 생성자 : 권한별 메뉴 목록 조회
    AuthMngSearchDTO(String authCd, String menuCd) {
        this.authCd = authCd;
        this.menuCd = menuCd;
    }
}
