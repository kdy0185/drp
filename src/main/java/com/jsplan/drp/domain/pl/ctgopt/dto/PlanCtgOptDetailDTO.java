package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.global.obj.entity.UserVO;
import com.jsplan.drp.global.obj.vo.DetailStatus;
import com.jsplan.drp.global.obj.vo.UseStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Class : PlanCtgOptDetailDTO
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 분류 옵션 상세 DTO
 */
@Getter
@NoArgsConstructor
public class PlanCtgOptDetailDTO {

    private String rtneCtgCd; // 루틴 분류 코드
    private String upperRtneCtgCd; // 상위 루틴 분류 코드
    private String rtneCtgNm; // 루틴 분류명
    private Float wtVal; // 가중치
    private Integer recgMinTime; // 최소 권장 시간
    private Integer recgMaxTime; // 최대 권장 시간
    private String useYn; // 사용 여부
    private String userId; // 사용자 아이디
    private String userNm; // 성명
    private DetailStatus detailStatus; // 등록/수정 구분

    // 상세 조회 시 구분값 설정
    public void setDetailStatus(DetailStatus detailStatus) {
        this.detailStatus = detailStatus;
    }

    @QueryProjection
    public PlanCtgOptDetailDTO(String rtneCtgCd, String upperRtneCtgCd, String rtneCtgNm,
        Float wtVal, Integer recgMinTime, Integer recgMaxTime, UseStatus useYn, String userId,
        String userNm) {
        this.rtneCtgCd = rtneCtgCd;
        this.upperRtneCtgCd = upperRtneCtgCd;
        this.rtneCtgNm = rtneCtgNm;
        this.wtVal = wtVal;
        this.recgMinTime = recgMinTime;
        this.recgMaxTime = recgMaxTime;
        this.useYn = useYn.getCode();
        this.userId = userId;
        this.userNm = userNm;
    }

    // 등록 시 로그인 사용자 정보 자동 세팅
    public void setUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.userId = ((UserVO) auth.getPrincipal()).getUserId();
        this.userNm = ((UserVO) auth.getPrincipal()).getUserNm();
    }
}
