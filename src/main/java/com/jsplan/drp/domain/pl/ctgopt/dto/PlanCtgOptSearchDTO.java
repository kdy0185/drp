package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.global.obj.vo.AuthVO;
import com.jsplan.drp.global.obj.vo.UseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : PlanCtgOptSearchDTO
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Search DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PlanCtgOptSearchDTO extends AuthVO {

    private String rtneCtgCd; // 루틴 분류 코드
    private String planUser; // 담당자
    private UseStatus useYn; // 사용 여부

    // Test 전용 생성자
    public PlanCtgOptSearchDTO(String rtneCtgCd, String userId, String userNm, UseStatus useYn) {
        super(userId, userNm);
        this.rtneCtgCd = rtneCtgCd;
        this.useYn = useYn;
    }

    // 슈퍼관리자가 아닌 경우 로그인 사용자 정보 고정
    public void fixUserInfo() {
        if ("N".equals(getAuthAdmin())) {
            setUserInfo();
        }
    }
}
