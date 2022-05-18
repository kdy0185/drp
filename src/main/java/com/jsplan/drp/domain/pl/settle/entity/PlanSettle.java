package com.jsplan.drp.domain.pl.settle.entity;

import com.jsplan.drp.domain.sys.usermng.entity.UserMng;
import com.jsplan.drp.global.obj.entity.BaseTimeEntity;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

/**
 * @Class : PlanSettle
 * @Author : KDW
 * @Date : 2022-05-17
 * @Description : 일일 결산 Entity
 */
@Entity
@Table(name = "PL_RTNE_DAILY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanSettle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAILY_ID")
    private Long dailyId; // 일련번호

    @Column(name = "RTNE_DATE", nullable = false)
    private LocalDate rtneDate; // 루틴 일자

    @Column(name = "DAILY_SCORE", nullable = false)
    @ColumnDefault("0.0")
    private Float dailyScore; // 점수

    @Column(name = "MEMO", length = 4000)
    private String memo; // 메모

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_USER")
    private UserMng userMng; // 사용자 엔티티

    @Builder
    public PlanSettle(Long dailyId, LocalDate rtneDate, Float dailyScore, String memo,
        UserMng userMng) {
        this.dailyId = dailyId;
        this.rtneDate = rtneDate;
        this.dailyScore = dailyScore;
        this.memo = memo;
        this.userMng = userMng;
    }
}
