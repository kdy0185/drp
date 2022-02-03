package com.jsplan.drp.pl.ctgopt;

import com.jsplan.drp.cmmn.obj.AbstractDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @Class : PlanCtgOptMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 분류 옵션 설정 Mapper
 */
@Repository("PlanCtgOptMapper")
public class PlanCtgOptMapper extends AbstractDAO {

    String namespace = "PlanCtgOpt.";

    /**
     * <p>분류 옵션 목록</p>
     *
     * @param planCtgOptVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanCtgOptVO> selectPlanCtgOptList(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (List<PlanCtgOptVO>) selectList(namespace + "selectPlanCtgOptList", planCtgOptVO);
    }

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param planCtgOptVO
     * @return PlanCtgOptVO
     * @throws Exception throws Exception
     */
    public PlanCtgOptVO selectPlanCtgOptDetail(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (PlanCtgOptVO) selectOne(namespace + "selectPlanCtgOptDetail", planCtgOptVO);
    }

    /**
     * <p>분류 옵션 등록</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertPlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (Integer) update(namespace + "insertPlanCtgOptData", planCtgOptVO);
    }

    /**
     * <p>분류 옵션 수정</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updatePlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (Integer) update(namespace + "updatePlanCtgOptData", planCtgOptVO);
    }

    /**
     * <p>적용 일과 수</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectPlanReportListCnt(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (Integer) selectOne(namespace + "selectPlanReportListCnt", planCtgOptVO);
    }

    /**
     * <p>분류 옵션 삭제</p>
     *
     * @param planCtgOptVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deletePlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (Integer) delete(namespace + "deletePlanCtgOptData", planCtgOptVO);
    }

    /**
     * <p>분류 옵션 엑셀 목록</p>
     *
     * @param planCtgOptVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PlanCtgOptVO> selectPlanCtgOptExcelList(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (List<PlanCtgOptVO>) selectList(namespace + "selectPlanCtgOptExcelList", planCtgOptVO);
    }

}
