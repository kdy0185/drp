package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.vo.UseStatus;

/**
 * @Class : UserMngSearchDTOBuilder
 * @Author : KDW
 * @Date : 2022-04-18
 * @Description : 사용자 관리 Search DTO Builder
 */
public class UserMngSearchDTOBuilder {

    // Test 전용 생성자
    public static UserMngSearchDTO build(int pageNo, int pageSize, String grpCd, String userId,
        String searchCd, String searchWord, UseStatus useYn) {
        return new UserMngSearchDTO(pageNo, pageSize, grpCd, userId, searchCd, searchWord, useYn);
    }
}
