package com.jsplan.drp.pl.report;

import com.jsplan.drp.cmmn.obj.AbstractDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @Class : PlanReportMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 데일리 리포트 Mapper
 */
@Repository("PlanReportMapper")
public class PlanReportMapper extends AbstractDAO {

    String namespace = "PlanReport.";

    /**
     * <p>분류 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanReportVO> selectPlanReportRtneCtgList(PlanReportVO planReportVO) throws Exception {
        return (List<PlanReportVO>) selectList(namespace + "selectPlanReportRtneCtgList", planReportVO);
    }

    /**
     * <p>일과 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanReportVO> selectPlanReportList(PlanReportVO planReportVO) throws Exception {
        return (List<PlanReportVO>) selectList(namespace + "selectPlanReportList", planReportVO);
    }

    /**
     * <p>일과 목록 수</p>
     *
     * @param planReportVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectPlanReportListCnt(PlanReportVO planReportVO) throws Exception {
        return (Integer) selectOne(namespace + "selectPlanReportListCnt", planReportVO);
    }

    /**
     * <p>일과 엑셀 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanReportVO> selectPlanReportExcelList(PlanReportVO planReportVO) throws Exception {
        return (List<PlanReportVO>) selectList(namespace + "selectPlanReportExcelList", planReportVO);
    }

}
