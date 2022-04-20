package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.entity.UseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : UserMngSearchDto
 * @Author : KDW
 * @Date : 2022-04-08
 * @Description : 사용자 관리 Search DTO
 */
@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserMngSearchDto {

    private int pageNo; // 조회할 페이지 번호
    private int pageSize; // 페이지당 데이터 출력 수
    private String grpCd; // 그룹 코드
    private String userId; // 사용자 아이디
    private String searchCd; // 검색 조건
    private String searchWord; // 검색어
    private UseStatus useYn; // 사용 여부

    public UserMngSearchDto(String searchCd) {
        this.searchCd = searchCd;
    }
}
