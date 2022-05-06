package com.jsplan.drp.domain.sys.usermng.dto;

/**
 * @Class : UserGrpMngSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 그룹 관리 Search DTO Builder
 */
public class UserGrpMngSearchDTOBuilder {

    // Test 전용 생성자
    public static UserGrpMngSearchDTO build(int pageNo, int pageSize, String grpCd, String grpNm) {
        return new UserGrpMngSearchDTO(pageNo, pageSize, grpCd, grpNm);
    }
}
