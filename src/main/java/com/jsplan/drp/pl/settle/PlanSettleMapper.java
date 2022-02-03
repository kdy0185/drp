package com.jsplan.drp.pl.settle;

import com.jsplan.drp.cmmn.obj.AbstractDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @Class : PlanSettleMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 일일 결산 Mapper
 */
@Repository("PlanSettleMapper")
public class PlanSettleMapper extends AbstractDAO {

    String namespace = "PlanSettle.";

    /**
     * <p>일일 결산 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanSettleVO> selectPlanSettleDayList(PlanSettleVO planSettleVO) throws Exception {
        return (List<PlanSettleVO>) selectList(namespace + "selectPlanSettleDayList", planSettleVO);
    }

    /**
     * <p>일일 결산 목록 수</p>
     *
     * @param planSettleVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectPlanSettleDayListCnt(PlanSettleVO planSettleVO) throws Exception {
        return (Integer) selectOne(namespace + "selectPlanSettleDayListCnt", planSettleVO);
    }

    /**
     * <p>분류별 할당 시간 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanSettleVO> selectPlanSettleDayTime(PlanSettleVO planSettleVO) throws Exception {
        return (List<PlanSettleVO>) selectList(namespace + "selectPlanSettleDayTime", planSettleVO);
    }

    /**
     * <p>일과별 달성률 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanSettleVO> selectPlanSettleDayAchvRate(PlanSettleVO planSettleVO) throws Exception {
        return (List<PlanSettleVO>) selectList(namespace + "selectPlanSettleDayAchvRate", planSettleVO);
    }

    /**
     * <p>일과별 몰입도 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanSettleVO> selectPlanSettleDayConcRate(PlanSettleVO planSettleVO) throws Exception {
        return (List<PlanSettleVO>) selectList(namespace + "selectPlanSettleDayConcRate", planSettleVO);
    }

    /**
     * <p>일일 결산 엑셀 목록</p>
     *
     * @param planSettleVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanSettleVO> selectPlanSettleDayExcelList(PlanSettleVO planSettleVO) throws Exception {
        return (List<PlanSettleVO>) selectList(namespace + "selectPlanSettleDayExcelList", planSettleVO);
    }

}
