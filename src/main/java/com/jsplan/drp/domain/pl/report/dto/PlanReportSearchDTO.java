package com.jsplan.drp.domain.pl.report.dto;

import com.jsplan.drp.global.obj.vo.AuthVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : PlanReportSearchDTO
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 데일리 리포트 Search DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PlanReportSearchDTO extends AuthVO {

    private int pageNo; // 조회할 페이지 번호
    private int pageSize; // 페이지당 데이터 출력 수
    private Long rtneId; // 일련번호
    private String rtneStartDate; // 루틴 시작 일자
    private String rtneEndDate; // 루틴 종료 일자
    private String rtneCtgCd; // 루틴 분류 코드
    private String rtneNm; // 루틴명

    // Test 전용 생성자
    PlanReportSearchDTO(int pageNo, int pageSize, String userId, String userNm,
        String rtneStartDate, String rtneEndDate, String rtneCtgCd, String rtneNm) {
        super(userId, userNm);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.rtneStartDate = rtneStartDate;
        this.rtneEndDate = rtneEndDate;
        this.rtneCtgCd = rtneCtgCd;
        this.rtneNm = rtneNm;
    }

    // 슈퍼관리자가 아닌 경우 로그인 사용자 정보 고정
    public void fixUserInfo() {
        if ("N".equals(getAuthAdmin())) {
            setUserInfo();
        }
    }
}
