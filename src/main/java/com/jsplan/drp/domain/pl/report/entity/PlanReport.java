package com.jsplan.drp.domain.pl.report.entity;

import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.domain.pl.report.vo.MainStatus;
import com.jsplan.drp.global.obj.entity.BaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * @Class : PlanReport
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 데일리 리포트 Entity
 */
@Entity
@Table(name = "PL_RTNE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RTNE_ID")
    private Long rtneId; // 일련번호

    @Column(name = "RTNE_DATE", nullable = false)
    private LocalDate rtneDate; // 루틴 일자

    @Column(name = "RTNE_ORD", nullable = false)
    private Integer rtneOrd; // 루틴 순서

    @Column(name = "RTNE_START_DATE", nullable = false)
    private LocalDateTime rtneStartDate; // 루틴 시작 일시

    @Column(name = "RTNE_END_DATE", nullable = false)
    private LocalDateTime rtneEndDate; // 루틴 종료 일시

    @Column(name = "RTNE_NM", nullable = false, length = 50)
    private String rtneNm; // 루틴명

    @Column(name = "ACHV_RATE")
    private Integer achvRate; // 달성률

    @Column(name = "CONC_RATE", length = 20)
    private String concRate; // 몰입도

    @Column(name = "MAIN_YN", nullable = false, length = 1)
    @ColumnDefault("'N'")
    @Enumerated(EnumType.STRING)
    private MainStatus mainYn; // 주요 일정 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "RTNE_CTG_CD", referencedColumnName = "RTNE_CTG_CD"),
        @JoinColumn(name = "PLAN_USER", referencedColumnName = "PLAN_USER")
    })
    private PlanCtgOpt planCtgOpt; // 분류 옵션 엔티티

    @Builder
    public PlanReport(LocalDate rtneDate, Integer rtneOrd, LocalDateTime rtneStartDate,
        LocalDateTime rtneEndDate, String rtneNm, Integer achvRate, String concRate,
        MainStatus mainYn, PlanCtgOpt planCtgOpt) {
        this.rtneDate = rtneDate;
        this.rtneOrd = rtneOrd;
        this.rtneStartDate = rtneStartDate;
        this.rtneEndDate = rtneEndDate;
        this.rtneNm = rtneNm;
        this.achvRate = achvRate;
        this.concRate = concRate;
        this.mainYn = mainYn;
        this.planCtgOpt = planCtgOpt;
    }
}
