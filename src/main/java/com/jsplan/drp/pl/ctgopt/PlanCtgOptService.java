package com.jsplan.drp.pl.ctgopt;

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
     * @return JSONObject
     * @throws Exception throws Exception
     */
    public JSONObject selectPlanCtgOptList(PlanCtgOptVO planCtgOptVO) throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONObject nodeObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        List<PlanCtgOptVO> list = planCtgOptMapper.selectPlanCtgOptList(planCtgOptVO);
        for (PlanCtgOptVO vo : list) {
            nodeObject = new JSONObject();
            nodeObject.put("rtneCtgNm", vo.getRtneCtgNm());
            nodeObject.put("rtneCtgCd", vo.getRtneCtgCd());
            nodeObject.put("wtVal", vo.getWtVal());
            nodeObject.put("recgMinTime", vo.getRecgMinTime());
            nodeObject.put("rtneStartDate", vo.getRtneStartDate());
            nodeObject.put("useYn", vo.getUseYn());
            nodeObject.put("planUser", vo.getPlanUser());
            nodeObject.put("leaf", "Y".equals(vo.getLastYn()));
            nodeObject.put("expanded", !"Y".equals(vo.getLastYn()));

            jsonArray.add(nodeObject);
        }

        jsonObject.put("planCtgOptList", jsonArray);
        return jsonObject;
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
