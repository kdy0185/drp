package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.vo.DetailStatus;
import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : UserMngRequest
 * @Author : KDW
 * @Date : 2022-04-16
 * @Description : 사용자 관리 Request DTO
 */
@Getter
@Setter
public class UserMngRequest {

    @NotBlank(message = "{js.valid.msg.required}")
    private String grpCd; // 그룹 코드

    @NotBlank(message = "{js.valid.msg.required}")
    private String userId; // 사용자 아이디

    @NotBlank(message = "{js.valid.msg.required}")
    private String userNm; // 성명

    @Pattern(regexp = "^((?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z]).*)?$", message = "{js.valid.msg.pwCheck}")
    private String userPw; // 비밀번호

    @Pattern(regexp = "^(01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4}))?$", message = "{js.valid.msg.mobileNum}")
    private String mobileNum; // 휴대폰 번호

    @Email(message = "{js.valid.msg.email}")
    private String email; // 이메일

    @NotBlank(message = "{js.valid.msg.required}")
    private String userType; // 사용자 유형

    @NotNull(message = "{js.valid.msg.required}")
    private UseStatus useYn; // 사용 여부

    private String authCd; // 권한 코드

    private DetailStatus detailStatus; // 등록/수정 구분

    // Test 전용 생성자
    UserMngRequest(String grpCd, String userId, String userNm, String userPw,
        String mobileNum, String email, String userType, UseStatus useYn, String authCd) {
        this.grpCd = grpCd;
        this.userId = userId;
        this.userNm = userNm;
        this.userPw = userPw;
        this.mobileNum = mobileNum;
        this.email = email;
        this.userType = userType;
        this.useYn = useYn;
        this.authCd = authCd;
    }

    // Request DTO → Entity 변환
    public UserMng toEntity() {
        return UserMng.builder()
            .userId(userId)
            .userNm(userNm)
            .userPw(userPw)
            .mobileNum(mobileNum)
            .email(email)
            .userType(userType)
            .useYn(useYn)
            .userGrpMng(UserGrpMng.builder().grpCd(grpCd).build())
            .userAuthMng(authCd)
            .build();
    }
}
