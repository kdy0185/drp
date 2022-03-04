package com.jsplan.drp.pl.settle;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : PlanSettleMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 일일 결산 Mapper
 */
@Repository
@Mapper
public interface PlanSettleMapper {

    /**
     * <p>일일 결산 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanSettleVO> selectPlanSettleDayList(PlanSettleVO planSettleVO) throws Exception;

    /**
     * <p>일일 결산 목록 수</p>
     *
     * @param planSettleVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectPlanSettleDayListCnt(PlanSettleVO planSettleVO) throws Exception;

    /**
     * <p>분류별 할당 시간 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanSettleVO> selectPlanSettleDayTime(PlanSettleVO planSettleVO) throws Exception;

    /**
     * <p>일과별 달성률 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanSettleVO> selectPlanSettleDayAchvRate(PlanSettleVO planSettleVO) throws Exception;

    /**
     * <p>일과별 몰입도 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanSettleVO> selectPlanSettleDayConcRate(PlanSettleVO planSettleVO) throws Exception;

    /**
     * <p>일일 결산 엑셀 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    List<PlanSettleVO> selectPlanSettleDayExcelList(PlanSettleVO planSettleVO) throws Exception;
}
