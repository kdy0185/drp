package com.jsplan.drp.domain.sys.usermng.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Class : UserGrpMngDetailDTO
 * @Author : KDW
 * @Date : 2022-03-21
 * @Description : 그룹 상세 DTO
 */
@Getter
@NoArgsConstructor
public class UserGrpMngDetailDTO {

    private String grpCd; // 그룹 코드
    private String grpNm; // 그룹명
    private String grpDesc; // 그룹 설명
    private String regUser; // 등록자
    private String regDate; // 등록 일시
    private String modUser; // 수정자
    private String modDate; // 수정 일시
    private String state; // 등록/수정 (등록 : I, 수정 : U)

    public void setState(String state) {
        this.state = state;
    }

    @QueryProjection
    public UserGrpMngDetailDTO(String grpCd, String grpNm, String grpDesc, String regUser,
        LocalDateTime regDate, String modUser, LocalDateTime modDate) {
        this.grpCd = grpCd;
        this.grpNm = grpNm;
        this.grpDesc = grpDesc;
        this.regUser = regUser;
        this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.modUser = modUser;
        this.modDate =
            modDate != null ? modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }
}
