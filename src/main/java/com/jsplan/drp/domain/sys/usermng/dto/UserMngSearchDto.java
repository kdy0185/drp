package com.jsplan.drp.domain.sys.usermng.dto;

import com.jsplan.drp.global.obj.entity.UseStatus;
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
public class UserMngSearchDto {

    private int pageNo; // 조회할 페이지 번호
    private int pageSize; // 페이지당 데이터 출력 수
    private String grpCd; // 그룹 코드
    private String searchCd; // 검색 조건
    private String searchWord; // 검색어
    private UseStatus useYn; // 사용 여부

    public UserMngSearchDto(String searchCd) {
        this.searchCd = searchCd;
    }

    // Test 전용 생성자
    UserMngSearchDto(int pageNo, int pageSize, String grpCd, String searchCd,
        String searchWord,
        UseStatus useYn) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.grpCd = grpCd;
        this.searchCd = searchCd;
        this.searchWord = searchWord;
        this.useYn = useYn;
    }
}
