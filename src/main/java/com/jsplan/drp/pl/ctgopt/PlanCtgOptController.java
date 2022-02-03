package com.jsplan.drp.pl.ctgopt;

import com.jsplan.drp.cmmn.obj.ComsMenuVO;
import com.jsplan.drp.cmmn.obj.ComsService;
import com.jsplan.drp.cmmn.obj.UserVO;
import com.jsplan.drp.cmmn.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : PlanCtgOptController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 분류 옵션 설정 Controller
 */
@Controller
public class PlanCtgOptController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "PlanCtgOptService")
    private PlanCtgOptService planCtgOptService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    /**
     * <p>분류 옵션 설정</p>
     *
     * @param planCtgOptVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/ctgopt/planCtgOptList.do")
    public ModelAndView planCtgOptList(@ModelAttribute PlanCtgOptVO planCtgOptVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("pl/ctgopt/planCtgOptList");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuVO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuVO = comsService.selectComsMenuDetail(comsMenuVO.getMenuCd());
            mav.addObject("comsMenuVO", comsMenuVO);
            // ***************************** MENU : E *****************************

            // 로그인 정보
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();
            String userId = ((UserVO) principal).getUserId();
            String userNm = ((UserVO) principal).getUserNm();

            // 관리자가 아닐 경우 본인 분류 옵션만 조회 가능
            if ("N".equals(planCtgOptVO.getAuthAdmin())) {
                planCtgOptVO.setUserId(userId);
                planCtgOptVO.setUserNm(userNm);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>분류 옵션 조회</p>
     *
     * @param planCtgOptVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/ctgopt/planCtgOptSearch.do")
    public @ResponseBody
    JSONObject planCtgOptSearch(@ModelAttribute PlanCtgOptVO planCtgOptVO) throws Exception {
        JSONObject planCtgOptList = new JSONObject();

        try {
            planCtgOptList = planCtgOptService.selectPlanCtgOptList(planCtgOptVO);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return planCtgOptList;
    }

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param planCtgOptVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/ctgopt/planCtgOptDetail.do")
    public ModelAndView planCtgOptDetail(@ModelAttribute PlanCtgOptVO planCtgOptVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("pl/ctgopt/planCtgOptDetail");
        String state = planCtgOptVO.getState();

        try {
            // 로그인 정보
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();
            String userId = ((UserVO) principal).getUserId();
            String userNm = ((UserVO) principal).getUserNm();

            if ("I".equals(state)) {
                planCtgOptVO.setUserId(userId);
                planCtgOptVO.setUserNm(userNm);
            }

            if ("U".equals(state)) {
                PlanCtgOptVO vo = planCtgOptService.selectPlanCtgOptDetail(planCtgOptVO);
                vo.setState(state);
                mav.addObject("planCtgOptVO", vo);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>분류 옵션 수정</p>
     *
     * @param planCtgOptVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/ctgopt/planCtgOptUpdate.do")
    public @ResponseBody
    String planCtgOptUpdate(@ModelAttribute PlanCtgOptVO planCtgOptVO) throws Exception {
        String code = null;
        String state = planCtgOptVO.getState();

        try {
            if ("I".equals(state)) {
                code = planCtgOptService.insertPlanCtgOptData(planCtgOptVO);
            }
            if ("U".equals(state)) {
                code = planCtgOptService.updatePlanCtgOptData(planCtgOptVO);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>분류 옵션 삭제</p>
     *
     * @param planCtgOptVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/ctgopt/planCtgOptDelete.do")
    public @ResponseBody
    String planCtgOptDelete(@ModelAttribute PlanCtgOptVO planCtgOptVO) throws Exception {
        String code = null;

        try {
            code = planCtgOptService.deletePlanCtgOptData(planCtgOptVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>분류 옵션 엑셀 출력</p>
     *
     * @param planCtgOptVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/ctgopt/planCtgOptExcel.do")
    public ExcelUtil planCtgOptExcel(@ModelAttribute PlanCtgOptVO planCtgOptVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<PlanCtgOptVO> planCtgOptExcelList = planCtgOptService.selectPlanCtgOptExcelList(planCtgOptVO);

            // 컬럼명 설정
            colName.add("순번");
            colName.add("분류");
            colName.add("분류 코드");
            colName.add("가중치");
            colName.add("권장 시간");
            colName.add("적용 기간");
            colName.add("사용 여부");
            colName.add("담당자");

            // 컬럼 사이즈 설정
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(2000);
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(6000);
            colWidth.add(2000);
            colWidth.add(4000);

            // 데이터 설정
            for (int i = 0; i < planCtgOptExcelList.size(); i++) {
                PlanCtgOptVO vo = (PlanCtgOptVO) planCtgOptExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String rtneCtgNm = vo.getRtneCtgNm();
                String rtneCtgCd = vo.getRtneCtgCd();
                String wtVal = vo.getWtVal();
                String recgMinTime = vo.getRecgMinTime();
                String rtneStartDate = vo.getRtneStartDate();
                String useYn = vo.getUseYn();
                String planUser = vo.getPlanUser();
                String[] arr = {rn, rtneCtgNm, rtneCtgCd, wtVal, recgMinTime, rtneStartDate, useYn,
                    planUser};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "분류 옵션 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }

}
