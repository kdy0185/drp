package com.jsplan.drp.domain.pl.settle.dto;

import com.jsplan.drp.global.obj.vo.AuthVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : PlanSettleSearchDTO
 * @Author : KDW
 * @Date : 2022-05-18
 * @Description : 일일 결산 Search DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PlanSettleSearchDTO extends AuthVO {

    private int pageNo; // 조회할 페이지 번호
    private int pageSize; // 페이지당 데이터 출력 수
    private String rtneStartDate; // 루틴 시작 일자
    private String rtneEndDate; // 루틴 종료 일자

    private String rtneDate; // 루틴 일자
    private String planUser; // 담당자

    // Test 전용 생성자 : 일일 결산 목록
    PlanSettleSearchDTO(int pageNo, int pageSize, String userId, String userNm,
        String rtneStartDate, String rtneEndDate) {
        super(userId, userNm);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.rtneStartDate = rtneStartDate;
        this.rtneEndDate = rtneEndDate;
    }

    // Test 전용 생성자 : 일일 결산 상세
    PlanSettleSearchDTO(String rtneDate, String planUser) {
        this.rtneDate = rtneDate;
        this.planUser = planUser;
    }

    // 슈퍼관리자가 아닌 경우 로그인 사용자 정보 고정
    public void fixUserInfo() {
        if ("N".equals(getAuthAdmin())) {
            setUserInfo();
        }
    }
}

