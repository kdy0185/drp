package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMng;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * @Class : UserGrpMngRequest
 * @Author : KDW
 * @Date : 2022-03-21
 * @Description : 그룹 관리 Request DTO
 */
public class UserGrpMngRequest {

    @NotBlank(message = "{js.valid.msg.required}")
    private final String grpCd; // 그룹 코드

    @NotBlank(message = "{js.valid.msg.required}")
    private final String grpNm; // 그룹명

    private final String grpDesc; // 그룹 설명

    @Builder
    public UserGrpMngRequest(String grpCd, String grpNm, String grpDesc) {
        this.grpCd = grpCd;
        this.grpNm = grpNm;
        this.grpDesc = grpDesc;
    }

    // Request DTO → Entity 변환
    public UserGrpMng toEntity() {
        return UserGrpMng.builder()
            .grpCd(grpCd)
            .grpNm(grpNm)
            .grpDesc(grpDesc)
            .build();
    }
}
