package com.jsplan.drp.domain.sys.codemng.dto;

import com.jsplan.drp.global.obj.entity.BaseListDTO;
import com.jsplan.drp.global.obj.entity.UseStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

/**
 * @Class : CodeGrpMngListDTO
 * @Author : KDW
 * @Date : 2022-04-30
 * @Description : 그룹 코드 목록 DTO
 */
@Getter
public class CodeGrpMngListDTO extends BaseListDTO {

    private final String grpCd; // 그룹 코드
    private final String grpNm; // 그룹 코드명
    private final String useYn; // 사용 여부
    private final String detl; // 비고
    private final String regUser; // 등록자
    private final String regDate; // 등록 일자
    private final String modUser; // 수정자
    private final String modDate; // 수정 일자

    @QueryProjection
    public CodeGrpMngListDTO(String grpCd, String grpNm, UseStatus useYn, String detl,
        String regUser, LocalDateTime regDate, String modUser, LocalDateTime modDate) {
        this.grpCd = grpCd;
        this.grpNm = grpNm;
        this.useYn = useYn.getCode();
        this.detl = detl;
        this.regUser = regUser;
        this.regDate = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.modUser = modUser;
        this.modDate =
            modDate != null ? modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }
}
