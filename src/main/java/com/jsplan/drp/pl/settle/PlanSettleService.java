package com.jsplan.drp.pl.settle;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Class : PlanSettleService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 일일 결산 Service
 */
@Service("PlanSettleService")
public class PlanSettleService {

    @Resource
    private PlanSettleMapper planSettleMapper;

    /**
     * <p>일일 결산 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanSettleVO> selectPlanSettleDayList(PlanSettleVO planSettleVO) throws Exception {
        return planSettleMapper.selectPlanSettleDayList(planSettleVO);
    }

    /**
     * <p>일일 결산 목록 수</p>
     *
     * @param planSettleVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectPlanSettleDayListCnt(PlanSettleVO planSettleVO) throws Exception {
        return planSettleMapper.selectPlanSettleDayListCnt(planSettleVO);
    }

    /**
     * <p>분류별 할당 시간 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanSettleVO> selectPlanSettleDayTime(PlanSettleVO planSettleVO) throws Exception {
        return planSettleMapper.selectPlanSettleDayTime(planSettleVO);
    }

    /**
     * <p>일과별 달성률 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanSettleVO> selectPlanSettleDayAchvRate(PlanSettleVO planSettleVO) throws Exception {
        return planSettleMapper.selectPlanSettleDayAchvRate(planSettleVO);
    }

    /**
     * <p>일과별 몰입도 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanSettleVO> selectPlanSettleDayConcRate(PlanSettleVO planSettleVO) throws Exception {
        return planSettleMapper.selectPlanSettleDayConcRate(planSettleVO);
    }

    /**
     * <p>일일 결산 엑셀 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanSettleVO> selectPlanSettleDayExcelList(PlanSettleVO planSettleVO) throws Exception {
        return (List<PlanSettleVO>) planSettleMapper.selectPlanSettleDayExcelList(planSettleVO);
    }

}
