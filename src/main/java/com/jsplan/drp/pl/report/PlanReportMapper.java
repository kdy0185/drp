package com.jsplan.drp.pl.report;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : PlanReportMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 데일리 리포트 Mapper
 */
@Repository
@Mapper
public interface PlanReportMapper {

    /**
     * <p>분류 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanReportVO> selectPlanReportRtneCtgList(PlanReportVO planReportVO) throws Exception;

    /**
     * <p>일과 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanReportVO> selectPlanReportList(PlanReportVO planReportVO) throws Exception;

    /**
     * <p>일과 목록 수</p>
     *
     * @param planReportVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectPlanReportListCnt(PlanReportVO planReportVO) throws Exception;

    /**
     * <p>일과 엑셀 목록</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanReportVO> selectPlanReportExcelList(PlanReportVO planReportVO) throws Exception;
}
