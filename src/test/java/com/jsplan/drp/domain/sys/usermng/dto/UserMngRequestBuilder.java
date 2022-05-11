package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.vo.UseStatus;

/**
 * @Class : UserMngRequestBuilder
 * @Author : KDW
 * @Date : 2022-04-18
 * @Description : 사용자 관리 Request DTO Builder
 */
public class UserMngRequestBuilder {

    // Test 전용 생성자
    public static UserMngRequest build(String grpCd, String userId, String userNm, String userPw,
        String mobileNum, String email, String userType, UseStatus useYn, String authCd) {
        return new UserMngRequest(grpCd, userId, userNm, userPw, mobileNum, email, userType, useYn,
            authCd);
    }
}
