package com.jsplan.drp.domain.pl.report.repository;

import static com.jsplan.drp.domain.pl.ctgopt.entity.QPlanCtgOpt.planCtgOpt;
import static com.jsplan.drp.domain.pl.report.entity.QPlanReport.planReport;

import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.domain.pl.report.dto.PlanReportListDTO;
import com.jsplan.drp.domain.pl.report.dto.QPlanReportListDTO;
import com.jsplan.drp.domain.pl.report.entity.PlanReport;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : PlanReportCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-05-13
 * @Description : 데일리 리포트 Custom Repository Impl
 */
public class PlanReportCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    PlanReportCustomRepository {

    /**
     * <p>데일리 리포트 Custom Repository Impl 생성자</p>
     */
    public PlanReportCustomRepositoryImpl() {
        super(PlanReport.class);
    }

    /**
     * <p>분류 목록</p>
     *
     * @param userId (사용자 아이디)
     * @return List (분류 목록)
     */
    @Override
    public List<PlanReportListDTO> searchPlanReportRtneCtgList(String userId) {
        List<PlanCtgOpt> rtneCtgList = selectFrom(planCtgOpt)
            .where(planCtgOpt.rtneCtgId.planUser.eq(userId))
            .fetch();

        return rtneCtgList.stream()
            .map(v -> new PlanReportListDTO(v.getRtneCtgId().getRtneCtgCd(), v.getRtneCtgNm()))
            .collect(Collectors.toList());
    }

    /**
     * <p>일과 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param rtneCtgCd     (루틴 분류 코드)
     * @param rtneNm        (루틴명)
     * @param pageable      (페이징 정보)
     * @return Page (일과 목록)
     */
    @Override
    public Page<PlanReportListDTO> searchPlanReportList(String userId, String rtneStartDate,
        String rtneEndDate, String rtneCtgCd, String rtneNm, Pageable pageable) {
        return applyPagination(pageable,
            contentQuery -> getPlanReportListQuery(userId, rtneStartDate, rtneEndDate, rtneCtgCd,
                rtneNm, contentQuery),
            countQuery -> getPlanReportCountQuery(userId, rtneStartDate, rtneEndDate, rtneCtgCd,
                rtneNm, countQuery)
        );
    }

    /**
     * <p>일과 목록 쿼리</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param rtneCtgCd     (루틴 분류 코드)
     * @param rtneNm        (루틴명)
     * @param contentQuery  (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<PlanReportListDTO> getPlanReportListQuery(String userId, String rtneStartDate,
        String rtneEndDate, String rtneCtgCd, String rtneNm, JPAQueryFactory contentQuery) {
        return contentQuery.select(new QPlanReportListDTO(
                planReport.rtneId,
                planReport.rtneDate,
                planReport.rtneOrd,
                planReport.rtneStartDate,
                planReport.rtneEndDate,
                planCtgOpt.rtneCtgNm,
                planReport.rtneNm,
                planReport.achvRate,
                Expressions.simpleTemplate(String.class, "getCodeNm({0}, {1})", "CONC_RATE",
                    planReport.concRate),
                planCtgOpt.rtneCtgId.planUser))
            .from(planReport)
            .join(planReport.planCtgOpt, planCtgOpt)
            .where(planUserEq(userId), rtneStartDateGoe(rtneStartDate), rtneEndDateLoe(rtneEndDate),
                rtneCtgCdEq(rtneCtgCd), rtneNmLike(rtneNm))
            .orderBy(planReport.rtneId.asc());
    }

    /**
     * <p>일과 목록 조회 조건 : 담당자</p>
     *
     * @param userId (사용자 아이디)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression planUserEq(String userId) {
        return !StringUtil.isEmpty(userId) ? planCtgOpt.rtneCtgId.planUser.eq(userId) : null;
    }

    /**
     * <p>일과 목록 조회 조건 : 시작 일자</p>
     *
     * @param rtneStartDate (루틴 시작 일자)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression rtneStartDateGoe(String rtneStartDate) {
        return !StringUtil.isEmpty(rtneStartDate) ? planReport.rtneDate.goe(
            LocalDate.parse(rtneStartDate)) : null;
    }

    /**
     * <p>일과 목록 조회 조건 : 종료 일자</p>
     *
     * @param rtneEndDate (루틴 종료 일자)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression rtneEndDateLoe(String rtneEndDate) {
        return !StringUtil.isEmpty(rtneEndDate) ? planReport.rtneDate.loe(
            LocalDate.parse(rtneEndDate)) : null;
    }

    /**
     * <p>일과 목록 조회 조건 : 분류</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression rtneCtgCdEq(String rtneCtgCd) {
        return !StringUtil.isEmpty(rtneCtgCd) ? planCtgOpt.rtneCtgId.rtneCtgCd.eq(rtneCtgCd) : null;
    }

    /**
     * <p>일과 목록 조회 조건 : 일과</p>
     *
     * @param rtneNm (루틴명)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression rtneNmLike(String rtneNm) {
        if (!StringUtil.isEmpty(rtneNm)) {
            return planReport.rtneNm.contains(rtneNm);
        }
        return null;
    }

    /**
     * <p>일과 목록 카운트 쿼리</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param rtneCtgCd     (루틴 분류 코드)
     * @param rtneNm        (루틴명)
     * @param countQuery    (쿼리 Factory)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<Long> getPlanReportCountQuery(String userId, String rtneStartDate,
        String rtneEndDate, String rtneCtgCd, String rtneNm, JPAQueryFactory countQuery) {
        return countQuery.select(planReport.count())
            .from(planReport)
            .where(planUserEq(userId), rtneStartDateGoe(rtneStartDate), rtneEndDateLoe(rtneEndDate),
                rtneCtgCdEq(rtneCtgCd), rtneNmLike(rtneNm));
    }

    /**
     * <p>일과 엑셀 목록</p>
     *
     * @param userId        (사용자 아이디)
     * @param rtneStartDate (루틴 시작 일자)
     * @param rtneEndDate   (루틴 종료 일자)
     * @param rtneCtgCd     (루틴 분류 코드)
     * @param rtneNm        (루틴명)
     * @return List (일과 목록)
     */
    @Override
    public List<PlanReportListDTO> searchPlanReportExcelList(String userId, String rtneStartDate,
        String rtneEndDate, String rtneCtgCd, String rtneNm) {
        List<PlanReportListDTO> excelList = getPlanReportListQuery(userId, rtneStartDate,
            rtneEndDate, rtneCtgCd, rtneNm, getQueryFactory()).fetch();
        return addRowNum(excelList, 1, excelList.size());
    }
}
