package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.entity.UseStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Class : UserMngDetailDTO
 * @Author : KDW
 * @Date : 2022-04-16
 * @Description : 사용자 상세 DTO
 */
@Getter
@NoArgsConstructor
public class UserMngDetailDTO {

    private String grpCd; // 그룹 코드
    private String userId; // 사용자 아이디
    private String userNm; // 성명
    private String userPw; // 비밀번호
    private String userPwDup; // 비밀번호 확인
    private String mobileNum; // 휴대폰 번호
    private String email; // 이메일
    private String userType; // 사용자 유형
    private String useYn; // 사용 여부
    private String regDate; // 등록 일시
    private String modDate; // 수정 일시
    private String state; // 등록/수정 (등록 : I, 수정 : U)

    // 상세 조회 시 구분값 설정
    public void setState(String state) {
        this.state = state;
    }

    // 권한 설정 팝업 화면 : 아이디 설정
    public UserMngDetailDTO(String userIdList) {
        userId = userIdList;
    }

    @QueryProjection
    public UserMngDetailDTO(String grpCd, String userId, String userNm, String mobileNum,
        String email, String userType, UseStatus useYn, LocalDateTime regDate,
        LocalDateTime modDate) {
        this.grpCd = grpCd;
        this.userId = userId;
        this.userNm = userNm;
        this.mobileNum = mobileNum;
        this.email = email;
        this.userType = userType;
        this.useYn = useYn.getCode();
        this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.modDate =
            modDate != null ? modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }
}
