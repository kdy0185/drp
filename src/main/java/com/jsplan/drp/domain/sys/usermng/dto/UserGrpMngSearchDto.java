package com.jsplan.drp.domain.sys.usermng.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : UserGrpMngSearchDto
 * @Author : KDW
 * @Date : 2022-03-24
 * @Description : 그룹 관리 Search DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class UserGrpMngSearchDto {

    private int pageNo; // 조회할 페이지 번호
    private int pageSize; // 페이지당 데이터 출력 수
    private String grpCd; // 그룹 코드
    private String grpNm; // 그룹명

    // Test 전용 생성자
    UserGrpMngSearchDto(int pageNo, int pageSize, String grpNm) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.grpNm = grpNm;
    }
}
