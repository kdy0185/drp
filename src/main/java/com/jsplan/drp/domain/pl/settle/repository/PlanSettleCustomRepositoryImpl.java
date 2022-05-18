package com.jsplan.drp.domain.pl.settle.repository;

import static com.jsplan.drp.domain.pl.ctgopt.entity.QPlanCtgOpt.planCtgOpt;
import static com.jsplan.drp.domain.pl.report.entity.QPlanReport.planReport;
import static com.jsplan.drp.domain.pl.settle.entity.QPlanSettle.planSettle;

import com.jsplan.drp.domain.pl.settle.dto.PlanSettleDetailDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleListDTO;
import com.jsplan.drp.domain.pl.settle.dto.QPlanSettleListDTO;
import com.jsplan.drp.domain.pl.settle.entity.PlanSettle;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : PlanSettleCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-05-17
 * @Description : 일일 결산 Custom Repository Impl
 */
public class PlanSettleCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    PlanSettleCustomRepository {

    /**
     * <p>일일 결산 Custom Repository Impl 생성자</p>
     */
    public PlanSettleCustomRepositoryImpl() {
        super(PlanSettle.class);
    }

    /**
     * <p>일일 결산 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param pageable      (페이징 정보)
     * @return Page (일일 결산 목록)
     */
    @Override
    public Page<PlanSettleListDTO> searchPlanSettleDayList(String userId, String rtneStartDate,
        String rtneEndDate, Pageable pageable) {
        Page<PlanSettleListDTO> planSettleDayList = applyPagination(pageable,
            contentQuery -> getPlanSettleListQuery(userId, rtneStartDate, rtneEndDate,
                contentQuery),
            countQuery -> getPlanSettleCountQuery(userId, rtneStartDate, rtneEndDate, countQuery)
        );

        // 달성률, 몰입도 설정
        planSettleDayList.forEach(
            v -> v.addRateInfo(getRateInfo(v.getRtneDate(), v.getPlanUser())));

        return planSettleDayList;
    }

    /**
     * <p>일일 결산 목록 쿼리</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param contentQuery  (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<PlanSettleListDTO> getPlanSettleListQuery(String userId, String rtneStartDate,
        String rtneEndDate, JPAQueryFactory contentQuery) {
        return contentQuery.select(new QPlanSettleListDTO(
                planSettle.dailyId,
                planSettle.rtneDate,
                planSettle.dailyScore,
                planSettle.memo,
                planSettle.userMng.userId.as("planUser")))
            .from(planSettle)
            .where(planUserEq(userId), rtneStartDateGoe(rtneStartDate), rtneEndDateLoe(rtneEndDate))
            .orderBy(planSettle.rtneDate.asc());
    }

    /**
     * <p>일일 결산 목록 카운트 쿼리</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param countQuery    (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<Long> getPlanSettleCountQuery(String userId, String rtneStartDate,
        String rtneEndDate, JPAQueryFactory countQuery) {
        return countQuery.select(planSettle.count())
            .from(planSettle)
            .where(planUserEq(userId), rtneStartDateGoe(rtneStartDate),
                rtneEndDateLoe(rtneEndDate));
    }

    /**
     * <p>일일 결산 목록 조회 조건 : 담당자</p>
     *
     * @param userId (사용자 아이디)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression planUserEq(String userId) {
        return !StringUtil.isEmpty(userId) ? planSettle.userMng.userId.eq(userId) : null;
    }

    /**
     * <p>일일 결산 목록 조회 조건 : 시작 일자</p>
     *
     * @param rtneStartDate (루틴 시작 일자)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression rtneStartDateGoe(String rtneStartDate) {
        return !StringUtil.isEmpty(rtneStartDate) ? planSettle.rtneDate.goe(
            LocalDate.parse(rtneStartDate)) : null;
    }

    /**
     * <p>일일 결산 목록 조회 조건 : 종료 일자</p>
     *
     * @param rtneEndDate (루틴 종료 일자)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression rtneEndDateLoe(String rtneEndDate) {
        return !StringUtil.isEmpty(rtneEndDate) ? planSettle.rtneDate.loe(
            LocalDate.parse(rtneEndDate)) : null;
    }

    /**
     * <p>달성률, 몰입도 조회</p>
     *
     * @param rtneDate (루틴 일자)
     * @param planUser (담당자)
     * @return PlanSettleListDTO (달성률, 몰입도 정보)
     */
    private PlanSettleListDTO getRateInfo(LocalDate rtneDate, String planUser) {
        return select(Projections.fields(PlanSettleListDTO.class,
            planReport.achvRate.floatValue().avg().as("achvRateNum"),
            planReport.concRate.castToNum(Integer.class).floatValue().avg().as("concRateNum")))
            .from(planReport)
            .where(planReport.rtneDate.eq(rtneDate),
                planReport.planCtgOpt.rtneCtgId.planUser.eq(planUser))
            .groupBy(planReport.rtneDate, planReport.planCtgOpt.rtneCtgId.planUser)
            .fetchOne();
    }

    /**
     * <p>분류별 할당 시간 목록</p>
     *
     * @param rtneDate (루틴 일자)
     * @param planUser (담당자)
     * @return List (분류별 할당 시간 목록)
     */
    @Override
    public List<PlanSettleDetailDTO> searchPlanSettleDayTime(String rtneDate, String planUser) {
        // 할당 시간(분) 설정
        NumberTemplate<Integer> rtneAssignCnt = Expressions.numberTemplate(Integer.class,
            "datediff(MI, {0}, {1})", planReport.rtneStartDate, planReport.rtneEndDate);

        // 목록 조회
        List<PlanSettleDetailDTO> planSettleDayTimeList = getPlanSettleDayTimeQuery(rtneDate,
            planUser, rtneAssignCnt, getQueryFactory()).fetch();

        // 전체 할당 시간
        Integer rtneAssignSum = planSettleDayTimeList.stream()
            .mapToInt(PlanSettleDetailDTO::getRtneAssignCnt).sum();

        // 분류명, 할당 비율 설정
        planSettleDayTimeList.forEach(v -> {
            v.addRtneCtgNm(getRtneCtgNm(v.getRtneCtgCd(), planUser));
            v.addRtneAssignPer(v.getRtneAssignCnt(), rtneAssignSum);
        });

        return addRowNum(planSettleDayTimeList, 1, planSettleDayTimeList.size());
    }

    /**
     * <p>분류별 할당 시간 목록 쿼리</p>
     *
     * @param rtneDate      (루틴 일자)
     * @param planUser      (담당자)
     * @param rtneAssignCnt (할당 시간(분))
     * @param contentQuery  (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<PlanSettleDetailDTO> getPlanSettleDayTimeQuery(String rtneDate,
        String planUser, NumberTemplate<Integer> rtneAssignCnt, JPAQueryFactory contentQuery) {
        return contentQuery.select(Projections.constructor(PlanSettleDetailDTO.class,
                planReport.planCtgOpt.rtneCtgId.rtneCtgCd,
                rtneAssignCnt.sum(),
                Expressions.simpleTemplate(String.class, "getFormatTime({0})", rtneAssignCnt.sum()),
                rtneAssignCnt.count()))
            .from(planReport)
            .where(planReport.rtneDate.eq(LocalDate.parse(rtneDate)),
                planReport.planCtgOpt.rtneCtgId.planUser.eq(planUser))
            .groupBy(planReport.planCtgOpt.rtneCtgId.rtneCtgCd)
            .orderBy(planReport.planCtgOpt.rtneCtgId.rtneCtgCd.asc());
    }

    /**
     * <p>분류명 조회</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param planUser  (담당자)
     * @return String (루틴 분류명)
     */
    private String getRtneCtgNm(String rtneCtgCd, String planUser) {
        return select(planCtgOpt.rtneCtgNm)
            .from(planCtgOpt)
            .where(planCtgOpt.rtneCtgId.rtneCtgCd.eq(rtneCtgCd),
                planCtgOpt.rtneCtgId.planUser.eq(planUser))
            .fetchOne();
    }

    /**
     * <p>일과별 달성률 목록</p>
     *
     * @param rtneDate (루틴 일자)
     * @param planUser (담당자)
     * @return List (일과별 달성률 목록)
     */
    @Override
    public List<PlanSettleDetailDTO> searchPlanSettleDayAchvRate(String rtneDate, String planUser) {
        List<PlanSettleDetailDTO> planSettleDayAchvRateList = select(
            Projections.constructor(PlanSettleDetailDTO.class,
                planReport.achvRate,
                planReport.achvRate.count().as("rtneCnt")))
            .from(planReport)
            .where(planReport.rtneDate.eq(LocalDate.parse(rtneDate)),
                planReport.planCtgOpt.rtneCtgId.planUser.eq(planUser))
            .groupBy(planReport.achvRate)
            .orderBy(planReport.achvRate.desc())
            .fetch();

        return addRowNum(planSettleDayAchvRateList, 1, planSettleDayAchvRateList.size());
    }

    /**
     * <p>일과별 몰입도 목록</p>
     *
     * @param rtneDate (루틴 일자)
     * @param planUser (담당자)
     * @return List (일과별 몰입도 목록)
     */
    @Override
    public List<PlanSettleDetailDTO> searchPlanSettleDayConcRate(String rtneDate, String planUser) {
        List<PlanSettleDetailDTO> planSettleDayConcRateList = select(
            Projections.constructor(PlanSettleDetailDTO.class,
                Expressions.simpleTemplate(String.class, "getCodeNm({0}, {1})", "CONC_RATE",
                    planReport.concRate),
                planReport.concRate.count().as("rtneCnt")))
            .from(planReport)
            .where(planReport.rtneDate.eq(LocalDate.parse(rtneDate)),
                planReport.planCtgOpt.rtneCtgId.planUser.eq(planUser))
            .groupBy(planReport.concRate)
            .orderBy(planReport.concRate.desc())
            .fetch();

        return addRowNum(planSettleDayConcRateList, 1, planSettleDayConcRateList.size());
    }

    /**
     * <p>일일 결산 엑셀 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @return List (일일 결산 목록)
     */
    @Override
    public List<PlanSettleListDTO> searchPlanSettleDayExcelList(String userId, String rtneStartDate,
        String rtneEndDate) {
        List<PlanSettleListDTO> excelList = getPlanSettleListQuery(userId, rtneStartDate,
            rtneEndDate, getQueryFactory()).fetch();

        // 달성률, 몰입도 설정
        excelList.forEach(
            v -> v.addRateInfo(getRateInfo(v.getRtneDate(), v.getPlanUser())));

        return addRowNum(excelList, 1, excelList.size());
    }
}
