package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.entity.BaseListDto;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Class : UserMngListDto
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 사용자 목록 DTO
 */
@Getter
@NoArgsConstructor
public class UserMngListDto extends BaseListDto {

    private String grpCd; // 그룹 코드
    private String grpNm; // 그룹명
    private String userId; // 사용자 아이디
    private String userNm; // 성명
    private String mobileNum; // 휴대폰 번호
    private String email; // 이메일
    private String userType; // 사용자 유형
    private String useYn; // 사용 여부
    private String regDate; // 등록 일시
    private String modDate; // 수정 일시

    @QueryProjection
    public UserMngListDto(String grpCd, String grpNm, String userId, String userNm,
        String mobileNum, String email, String userType, UseStatus useYn,
        LocalDateTime regDate, LocalDateTime modDate) {
        this.grpCd = grpCd;
        this.grpNm = grpNm;
        this.userId = userId;
        this.userNm = userNm;
        this.mobileNum = mobileNum;
        this.email = email;
        this.userType = userType;
        this.useYn = useYn.getTitle();
        this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.modDate =
            modDate != null ? modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }
}
