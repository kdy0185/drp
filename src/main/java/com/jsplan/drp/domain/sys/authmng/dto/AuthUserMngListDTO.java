package com.jsplan.drp.domain.sys.authmng.dto;

import com.jsplan.drp.global.obj.entity.BaseListDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

/**
 * @Class : AuthUserMngListDTO
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한별 사용자 목록 DTO
 */
@Getter
@ToString(of = {"userId", "userNm", "mobileNum", "authYn"})
public class AuthUserMngListDTO extends BaseListDTO {

    private final String userId; // 사용자 아이디
    private final String userNm; // 성명
    private final String mobileNum; // 휴대폰 번호
    private String authYn; // 권한 여부

    // 사용자별 권한 허용 여부 설정
    public void setAuthYn(String authYn) {
        this.authYn = authYn;
    }

    @QueryProjection
    public AuthUserMngListDTO(String userId, String userNm, String mobileNum) {
        this.userId = userId;
        this.userNm = userNm;
        this.mobileNum = mobileNum;
    }
}
