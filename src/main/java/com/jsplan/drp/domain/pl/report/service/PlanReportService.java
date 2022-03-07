package com.jsplan.drp.domain.pl.report.service;

import com.jsplan.drp.domain.pl.report.entity.PlanReportVO;
import com.jsplan.drp.domain.pl.report.mapper.PlanReportMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Class : PlanReportService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 데일리 리포트 Service
 */
@Service("PlanReportService")
public class PlanReportService {

    @Resource
    private PlanReportMapper planReportMapper;

    /**
     * <p>분류 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanReportVO> selectPlanReportRtneCtgList(PlanReportVO planReportVO) throws Exception {
        return planReportMapper.selectPlanReportRtneCtgList(planReportVO);
    }

    /**
     * <p>일과 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanReportVO> selectPlanReportList(PlanReportVO planReportVO) throws Exception {
        return planReportMapper.selectPlanReportList(planReportVO);
    }

    /**
     * <p>일과 목록 수</p>
     *
     * @param planReportVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectPlanReportListCnt(PlanReportVO planReportVO) throws Exception {
        return planReportMapper.selectPlanReportListCnt(planReportVO);
    }

    /**
     * <p>일과 엑셀 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanReportVO> selectPlanReportExcelList(PlanReportVO planReportVO) throws Exception {
        return (List<PlanReportVO>) planReportMapper.selectPlanReportExcelList(planReportVO);
    }

}
