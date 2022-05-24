package com.jsplan.drp.domain.pl.ctgopt.dto;

import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.domain.pl.ctgopt.entity.RtneCtgId;
import com.jsplan.drp.global.obj.vo.DetailStatus;
import com.jsplan.drp.global.obj.vo.UseStatus;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @Class : PlanCtgOptRequest
 * @Author : KDW
 * @Date : 2022-05-12
 * @Description : 분류 옵션 설정 Request DTO
 */
@Getter
@Setter
public class PlanCtgOptRequest {

    @NotBlank(message = "{js.valid.msg.required}")
    private String rtneCtgCd; // 루틴 분류 코드

    private String upperRtneCtgCd; // 상위 루틴 분류 코드

    @NotBlank(message = "{js.valid.msg.required}")
    private String rtneCtgNm; // 루틴 분류명

    @Digits(integer = 2, fraction = 1)
    @NotNull(message = "{js.valid.msg.required}")
    private Float wtVal; // 가중치

    @Positive(message = "{js.valid.msg.digitsReq}")
    @NotNull(message = "{js.valid.msg.required}")
    private Integer recgMinTime; // 최소 권장 시간

    @Positive(message = "{js.valid.msg.digitsReq}")
    @NotNull(message = "{js.valid.msg.required}")
    private Integer recgMaxTime; // 최대 권장 시간

    @NotNull(message = "{js.valid.msg.required}")
    private UseStatus useYn; // 사용 여부

    @NotBlank(message = "{js.valid.msg.required}")
    private String userId; // 사용자 아이디

    private DetailStatus detailStatus; // 등록/수정 구분

    // Test 전용 생성자
    PlanCtgOptRequest(String rtneCtgCd, String upperRtneCtgCd, String rtneCtgNm, Float wtVal,
        Integer recgMinTime, Integer recgMaxTime, UseStatus useYn, String userId) {
        this.rtneCtgCd = rtneCtgCd;
        this.upperRtneCtgCd = upperRtneCtgCd;
        this.rtneCtgNm = rtneCtgNm;
        this.wtVal = wtVal;
        this.recgMinTime = recgMinTime;
        this.recgMaxTime = recgMaxTime;
        this.useYn = useYn;
        this.userId = userId;
    }

    // 복합키 추출
    public RtneCtgId getRtneCtgId() {
        return RtneCtgId.createRtneCtgId(rtneCtgCd, userId);
    }

    // Request DTO → Entity 변환
    public PlanCtgOpt toEntity() {
        return PlanCtgOpt.builder()
            .rtneCtgId(getRtneCtgId())
            .upperRtneCtgCd(upperRtneCtgCd)
            .rtneCtgNm(rtneCtgNm)
            .wtVal(wtVal)
            .recgMinTime(recgMinTime)
            .recgMaxTime(recgMaxTime)
            .useYn(useYn)
            .build();
    }
}
