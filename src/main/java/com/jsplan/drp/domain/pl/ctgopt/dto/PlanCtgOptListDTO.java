package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.global.obj.dto.BaseListDTO;
import com.jsplan.drp.global.obj.vo.UseStatus;
import lombok.Getter;

/**
 * @Class : PlanCtgOptListDTO
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 분류 옵션 목록 DTO
 */
@Getter
public class PlanCtgOptListDTO extends BaseListDTO {

    private final String rtneCtgCd; // 루틴 분류 코드
    private final String rtneCtgNm; // 루틴 분류명
    private final Float wtVal; // 가중치
    private final String recgTime; // 권장 시간
    private final String rtneDate; // 적용 기간
    private final String useYn; // 사용 여부
    private final String planUser; // 담당자
    private final String lastYn; // 최하위 자식 노드 여부

    public PlanCtgOptListDTO(String rtneCtgCd, String rtneCtgNm, Float wtVal, Integer recgMinTime,
        Integer recgMaxTime, String rtneDate, UseStatus useYn, String planUser, String lastYn) {
        this.rtneCtgCd = rtneCtgCd;
        this.rtneCtgNm = rtneCtgNm;
        this.wtVal = wtVal;
        this.recgTime = recgMinTime + " ~ " + recgMaxTime;
        this.rtneDate = rtneDate;
        this.useYn = useYn.getTitle();
        this.planUser = planUser;
        this.lastYn = lastYn;
    }
}
