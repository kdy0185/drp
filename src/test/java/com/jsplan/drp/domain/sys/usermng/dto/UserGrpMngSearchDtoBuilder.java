package com.jsplan.drp.domain.sys.usermng.dto;

/**
 * @Class : UserGrpMngSearchDtoBuilder
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 그룹 관리 Search DTO Builder
 */
public class UserGrpMngSearchDtoBuilder {

    // Test 전용 생성자
    public static UserGrpMngSearchDto build(int pageNo, int pageSize, String grpCd, String grpNm) {
        return new UserGrpMngSearchDto(pageNo, pageSize, grpCd, grpNm);
    }
}
