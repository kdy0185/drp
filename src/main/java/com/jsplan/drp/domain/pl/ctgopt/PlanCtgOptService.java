package com.jsplan.drp.domain.pl.ctgopt;

import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : PlanCtgOptService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 분류 옵션 설정 Service
 */
@Service("PlanCtgOptService")
public class PlanCtgOptService {

    @Resource
    private PlanCtgOptMapper planCtgOptMapper;

    /**
     * <p>분류 옵션 목록</p>
     *
     * @param planCtgOptVO
     * @return JSONArray
     * @throws Exception throws Exception
     */
    public JSONArray selectPlanCtgOptList(PlanCtgOptVO planCtgOptVO) throws Exception {
        JSONArray planCtgOptArray = new JSONArray();
        JSONObject planCtgOptObject = new JSONObject();

        // 하위 분류 옵션 조회
        List<PlanCtgOptVO> planCtgOptList = planCtgOptMapper.selectPlanCtgOptList(planCtgOptVO);
        for (PlanCtgOptVO vo : planCtgOptList) {
            planCtgOptObject = new JSONObject();
            planCtgOptObject.put("rtneCtgNm", vo.getRtneCtgNm());
            planCtgOptObject.put("rtneCtgCd", vo.getRtneCtgCd());
            planCtgOptObject.put("wtVal", vo.getWtVal());
            planCtgOptObject.put("recgMinTime", vo.getRecgMinTime());
            planCtgOptObject.put("rtneStartDate", vo.getRtneStartDate());
            planCtgOptObject.put("useYn", vo.getUseYn());
            planCtgOptObject.put("planUser", vo.getPlanUser());
            planCtgOptObject.put("leaf", "Y".equals(vo.getLastYn()));
            planCtgOptObject.put("expanded", !"Y".equals(vo.getLastYn()));

            planCtgOptArray.add(planCtgOptObject);
        }

        return planCtgOptArray;
    }

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param planCtgOptVO
     * @return PlanCtgOptVO
     * @throws Exception throws Exception
     */
    public PlanCtgOptVO selectPlanCtgOptDetail(PlanCtgOptVO planCtgOptVO) throws Exception {
        return planCtgOptMapper.selectPlanCtgOptDetail(planCtgOptVO);
    }

    /**
     * <p>분류 옵션 등록</p>
     *
     * @param planCtgOptVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String insertPlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception {
        return planCtgOptMapper.insertPlanCtgOptData(planCtgOptVO) > 0 ? "S" : "N";
    }

    /**
     * <p>분류 옵션 수정</p>
     *
     * @param planCtgOptVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updatePlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception {
        return planCtgOptMapper.updatePlanCtgOptData(planCtgOptVO) > 0 ? "S" : "N";
    }

    /**
     * <p>분류 옵션 삭제</p>
     *
     * @param planCtgOptVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String deletePlanCtgOptData(PlanCtgOptVO planCtgOptVO) throws Exception {
        String code = null;

        // 1. 분류 옵션이 적용된 일과 조회
        int cnt = planCtgOptMapper.selectPlanReportListCnt(planCtgOptVO);

        if (cnt > 0) {
            code = "F"; // 일과가 존재할 경우
        } else {
            // 2. 분류 옵션 삭제
            code = planCtgOptMapper.deletePlanCtgOptData(planCtgOptVO) > 0 ? "S" : "N";
        }

        return code;
    }

    /**
     * <p>분류 옵션 엑셀 목록</p>
     *
     * @param planCtgOptVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<PlanCtgOptVO> selectPlanCtgOptExcelList(PlanCtgOptVO planCtgOptVO) throws Exception {
        return (List<PlanCtgOptVO>) planCtgOptMapper.selectPlanCtgOptExcelList(planCtgOptVO);
    }

}
