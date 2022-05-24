package com.jsplan.drp.domain.pl.ctgopt.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Class : RtneCtgId
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 루틴 분류 코드 복합키 Class
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RtneCtgId implements Serializable {

    @Column(name = "RTNE_CTG_CD", length = 9)
    private String rtneCtgCd; // 루틴 분류 코드

    @Column(name = "PLAN_USER", length = 100)
    private String planUser; // 담당자

    // 복합키 생성
    public static RtneCtgId createRtneCtgId(String rtneCtgCd, String planUser) {
        RtneCtgId rtneCtgId = new RtneCtgId();
        rtneCtgId.setRtneCtgCd(rtneCtgCd);
        rtneCtgId.setPlanUser(planUser);
        return rtneCtgId;
    }
}
