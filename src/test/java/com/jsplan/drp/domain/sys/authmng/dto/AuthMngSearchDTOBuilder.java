package com.jsplan.drp.domain.sys.authmng.dto;

import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;

/**
 * @Class : AuthMngSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-04-28
 * @Description : 권한 관리 Search DTO Builder
 */
public class AuthMngSearchDTOBuilder {

    // Test 전용 생성자 : 권한 목록 조회
    public static AuthMngSearchDTO buildAuthMng(String authCd, String searchCd, String searchWord,
        UseStatus useYn) {
        return new AuthMngSearchDTO(authCd, searchCd, searchWord, useYn);
    }

    // Test 전용 생성자 : 권한별 사용자 목록 조회
    public static AuthMngSearchDTO buildAuthUserMng(int pageNo, int pageSize, String authCd,
        String grpCd, String searchCd, String searchWord) {
        return new AuthMngSearchDTO(pageNo, pageSize, authCd, grpCd, searchCd, searchWord);
    }

    // Test 전용 생성자 : 권한별 메뉴 목록 조회
    public static AuthMngSearchDTO buildAuthMenuMng(String authCd, String menuCd) {
        return new AuthMngSearchDTO(authCd, menuCd);
    }
}
