package com.jsplan.drp.domain.pl.ctgopt.entity;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequest;
import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import com.jsplan.drp.global.obj.vo.UseStatus;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.domain.Persistable;

/**
 * @Class : PlanCtgOpt
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 분류 옵션 설정 Entity
 */
@Entity
@Table(name = "PL_RTNE_CTG")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanCtgOpt extends BaseTimeEntity implements Persistable<RtneCtgId> {

    @EmbeddedId
    private RtneCtgId rtneCtgId; // 루틴 분류 코드 + 담당자

    @Column(name = "UPPER_RTNE_CTG_CD", length = 9)
    private String upperRtneCtgCd; // 상위 루틴 분류 코드

    @Column(name = "RTNE_CTG_NM", nullable = false, length = 50)
    private String rtneCtgNm; // 루틴 분류명

    @Column(name = "WT_VAL", nullable = false)
    @ColumnDefault("0.0")
    private Float wtVal; // 가중치

    @Column(name = "RECG_MIN_TIME", nullable = false)
    @ColumnDefault("0")
    private Integer recgMinTime; // 최소 권장 시간

    @Column(name = "RECG_MAX_TIME", nullable = false)
    @ColumnDefault("0")
    private Integer recgMaxTime; // 최대 권장 시간

    @Column(name = "USE_YN", nullable = false, length = 1)
    @ColumnDefault("'N'")
    @Enumerated(EnumType.STRING)
    private UseStatus useYn; // 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_USER", insertable = false, updatable = false)
    private UserMng userMng; // 사용자 엔티티

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "UPPER_RTNE_CTG_CD", referencedColumnName = "RTNE_CTG_CD", insertable = false, updatable = false),
        @JoinColumn(name = "PLAN_USER", referencedColumnName = "PLAN_USER", insertable = false, updatable = false)
    })
    private PlanCtgOpt upperPlanCtgOpt; // 상위 루틴 분류 엔티티

    @OneToMany(mappedBy = "upperPlanCtgOpt")
    private List<PlanCtgOpt> planCtgOptList = new ArrayList<>(); // 하위 루틴 분류 엔티티

    @Builder
    public PlanCtgOpt(RtneCtgId rtneCtgId, String upperRtneCtgCd, String rtneCtgNm,
        Float wtVal, Integer recgMinTime, Integer recgMaxTime, UseStatus useYn) {
        this.rtneCtgId = rtneCtgId;
        this.upperRtneCtgCd = upperRtneCtgCd;
        this.rtneCtgNm = rtneCtgNm;
        this.wtVal = wtVal;
        this.recgMinTime = recgMinTime;
        this.recgMaxTime = recgMaxTime;
        this.useYn = useYn;
    }

    // 최하위 자식 노드 여부 조회
    public String getLastYn() {
        return planCtgOptList.size() > 0 ? "N" : "Y";
    }

    // 분류 옵션 수정
    public void updatePlanCtgOpt(PlanCtgOptRequest request) {
        this.rtneCtgNm = request.getRtneCtgNm();
        this.wtVal = request.getWtVal();
        this.recgMinTime = request.getRecgMinTime();
        this.recgMaxTime = request.getRecgMaxTime();
        this.useYn = request.getUseYn();
    }

    @Override
    public RtneCtgId getId() {
        return rtneCtgId;
    }

    @Override
    public boolean isNew() {
        return getRegDate() == null;
    }
}
