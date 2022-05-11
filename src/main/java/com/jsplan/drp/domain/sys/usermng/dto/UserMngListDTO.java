package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.dto.BaseListDTO;
import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

/**
 * @Class : UserMngListDTO
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 사용자 목록 DTO
 */
@Getter
public class UserMngListDTO extends BaseListDTO {

    private final String grpCd; // 그룹 코드
    private final String grpNm; // 그룹명
    private final String userId; // 사용자 아이디
    private final String userNm; // 성명
    private final String mobileNum; // 휴대폰 번호
    private final String email; // 이메일
    private final String userType; // 사용자 유형
    private final String useYn; // 사용 여부
    private final String regDate; // 등록 일자
    private final String modDate; // 수정 일자

    @QueryProjection
    public UserMngListDTO(String grpCd, String grpNm, String userId, String userNm,
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
