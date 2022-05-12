package com.jsplan.drp.domain.pl.ctgopt.repository;

import static com.jsplan.drp.domain.pl.ctgopt.entity.QPlanCtgOpt.planCtgOpt;
import static com.jsplan.drp.domain.pl.report.entity.QPlanReport.planReport;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptListDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.QPlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.entity.PlanCtgOpt;
import com.jsplan.drp.global.obj.repository.Querydsl5RepositorySupport;
import com.jsplan.drp.global.obj.vo.UseStatus;
import com.jsplan.drp.global.util.StringUtil;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Class : PlanCtgOptCustomRepositoryImpl
 * @Author : KDW
 * @Date : 2022-05-11
 * @Description : 분류 옵션 설정 Custom Repository Impl
 */
public class PlanCtgOptCustomRepositoryImpl extends Querydsl5RepositorySupport implements
    PlanCtgOptCustomRepository {

    /**
     * <p>분류 옵션 설정 Custom Repository Impl 생성자</p>
     */
    public PlanCtgOptCustomRepositoryImpl() {
        super(PlanCtgOpt.class);
    }

    /**
     * <p>분류 옵션 목록</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @param useYn     (사용 여부)
     * @return List (분류 옵션 목록)
     */
    @Override
    public List<PlanCtgOptListDTO> searchPlanCtgOptList(String rtneCtgCd, String userId,
        UseStatus useYn) {
        List<PlanCtgOpt> planCtgOptList = getPlanCtgOptListQuery(rtneCtgCd, userId, useYn).fetch();

        return planCtgOptList.stream()
            .map(v -> new PlanCtgOptListDTO(v.getRtneCtgId().getRtneCtgCd(), v.getRtneCtgNm(),
                v.getWtVal(), v.getRecgMinTime(), v.getRecgMaxTime(),
                getRtneDate(v.getRtneCtgId().getRtneCtgCd(), v.getRtneCtgId().getPlanUser()),
                v.getUseYn(), v.getRtneCtgId().getPlanUser(), v.getLastYn()))
            .collect(Collectors.toList());
    }

    /**
     * <p>분류 옵션 목록 쿼리</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @param useYn     (사용 여부)
     * @return JPAQuery (생성된 쿼리문)
     */
    private JPAQuery<PlanCtgOpt> getPlanCtgOptListQuery(String rtneCtgCd, String userId,
        UseStatus useYn) {
        return selectFrom(planCtgOpt)
            .where(upperRtneCtgCdEq(rtneCtgCd), planUserEq(userId), useYnEq(useYn))
            .orderBy(
                new CaseBuilder().when(planCtgOpt.useYn.eq(UseStatus.Y)).then(1).otherwise(2).asc(),
                planCtgOpt.rtneCtgId.rtneCtgCd.substring(
                    planCtgOpt.rtneCtgId.rtneCtgCd.length().subtract(2), 3).asc(),
                planCtgOpt.rtneCtgId.rtneCtgCd.substring(0, 1).asc());
    }

    /**
     * <p>분류 옵션 목록 조회 조건 : 상위 루틴 분류 코드</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression upperRtneCtgCdEq(String rtneCtgCd) {
        return !StringUtil.isEmpty(rtneCtgCd) ? planCtgOpt.upperPlanCtgOpt.rtneCtgId.rtneCtgCd.eq(
            rtneCtgCd) : planCtgOpt.upperPlanCtgOpt.rtneCtgId.rtneCtgCd.isNull();
    }

    /**
     * <p>분류 옵션 목록 조회 조건 : 담당자</p>
     *
     * @param userId (사용자 아이디)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression planUserEq(String userId) {
        return userId != null ? planCtgOpt.rtneCtgId.planUser.eq(userId) : null;
    }

    /**
     * <p>분류 옵션 목록 조회 조건 : 사용 여부</p>
     *
     * @param useYn (사용 여부)
     * @return BooleanExpression (Boolean 표현식)
     */
    private BooleanExpression useYnEq(UseStatus useYn) {
        return useYn != null ? planCtgOpt.useYn.eq(useYn) : null;
    }

    /**
     * <p>분류 옵션별 적용 기간 조회</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @return String (적용 기간)
     */
    private String getRtneDate(String rtneCtgCd, String userId) {
        return select(
            Expressions.stringTemplate("FORMAT({0}, {1})", planReport.rtneDate.min(),
                ConstantImpl.create("yyyy-MM-dd"))
            .concat(" ~ ")
            .concat(Expressions.stringTemplate("FORMAT({0}, {1})", planReport.rtneDate.max(),
                ConstantImpl.create("yyyy-MM-dd"))
            ))
            .from(planReport)
            .where(planReport.planCtgOpt.rtneCtgId.rtneCtgCd.eq(rtneCtgCd),
                planReport.planCtgOpt.rtneCtgId.planUser.eq(userId))
            .groupBy(planReport.planCtgOpt.rtneCtgId)
            .fetchOne();
    }

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param planUser  (담당자)
     * @return PlanCtgOptDetailDTO (분류 옵션 DTO)
     */
    @Override
    public PlanCtgOptDetailDTO findPlanCtgOptByRtneCtgId(String rtneCtgCd, String planUser) {
        return select(new QPlanCtgOptDetailDTO(
            planCtgOpt.rtneCtgId.rtneCtgCd,
            planCtgOpt.upperPlanCtgOpt.rtneCtgId.rtneCtgCd,
            planCtgOpt.rtneCtgNm,
            planCtgOpt.wtVal,
            planCtgOpt.recgMinTime,
            planCtgOpt.recgMaxTime,
            planCtgOpt.useYn,
            planCtgOpt.rtneCtgId.planUser.as("userId"),
            Expressions.simpleTemplate(String.class, "getUserNm({0})",
                planCtgOpt.rtneCtgId.planUser)))
            .from(planCtgOpt)
            .where(planCtgOpt.rtneCtgId.rtneCtgCd.eq(rtneCtgCd), planUserEq(planUser))
            .fetchOne();
    }

    /**
     * <p>일과 확인</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @return boolean (일과 존재 여부)
     */
    @Override
    public boolean existsPlanReportByRtneCtgId(String rtneCtgCd, String userId) {
        Long planReportCnt = select(planReport.count())
            .from(planReport)
            .where(planReport.planCtgOpt.rtneCtgId.rtneCtgCd.eq(rtneCtgCd),
                planReport.planCtgOpt.rtneCtgId.planUser.eq(userId))
            .fetchOne();
        return planReportCnt != null && planReportCnt > 0;
    }

    /**
     * <p>분류 옵션 엑셀 목록</p>
     *
     * @param rtneCtgCd (루틴 분류 코드)
     * @param userId    (사용자 아이디)
     * @param useYn     (사용 여부)
     * @return List (분류 옵션 목록)
     */
    @Override
    public List<PlanCtgOptListDTO> searchPlanCtgOptExcelList(String rtneCtgCd, String userId,
        UseStatus useYn) {
        List<PlanCtgOpt> planCtgOptList = getPlanCtgOptListQuery(rtneCtgCd, userId, useYn).fetch();

        List<PlanCtgOptListDTO> excelList = planCtgOptList.stream()
            .map(v -> new PlanCtgOptListDTO(v.getRtneCtgId().getRtneCtgCd(), v.getRtneCtgNm(),
                v.getWtVal(), v.getRecgMinTime(), v.getRecgMaxTime(),
                getRtneDate(rtneCtgCd, userId), v.getUseYn(), v.getRtneCtgId().getPlanUser(), null))
            .collect(Collectors.toList());

        return addRowNum(excelList, 1, excelList.size());
    }
}
