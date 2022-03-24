package com.jsplan.drp.domain.sys.usermng.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @Class : UserGrpMngSearchDto
 * @Author : KDW
 * @Date : 2022-03-24
 * @Description : 그룹 관리 Search DTO
 */
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserGrpMngSearchDto {

    private final int pageNo; // 조회할 페이지 번호
    private final int pageSize; // 페이지당 데이터 출력 수
    private final String grpNm; // 그룹명
}
