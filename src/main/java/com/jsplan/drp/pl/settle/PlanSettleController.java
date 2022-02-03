package com.jsplan.drp.pl.settle;

import com.jsplan.drp.cmmn.obj.ComsMenuVO;
import com.jsplan.drp.cmmn.obj.ComsService;
import com.jsplan.drp.cmmn.obj.ComsVO;
import com.jsplan.drp.cmmn.obj.UserVO;
import com.jsplan.drp.cmmn.util.ExcelUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
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
 * @Class : PlanSettleController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 일일 결산 Controller
 */
@Controller
public class PlanSettleController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "PlanSettleService")
    private PlanSettleService planSettleService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    /**
     * <p>일일 결산</p>
     *
     * @param planSettleVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/settle/planSettleDayList.do")
    public ModelAndView planSettleDayList(@ModelAttribute PlanSettleVO planSettleVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("pl/settle/planSettleDayList");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuVO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuVO = comsService.selectComsMenuDetail(comsMenuVO.getMenuCd());
            mav.addObject("comsMenuVO", comsMenuVO);
            // ***************************** MENU : E *****************************

            // ***************************** PAGE : S *****************************
            List<ComsVO> pageList = comsService.selectComsCodeList("PAGE_SIZE");
            mav.addObject("pageList", pageList);
            // ***************************** PAGE : E *****************************

            // 로그인 정보
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();
            String userId = ((UserVO) principal).getUserId();
            String userNm = ((UserVO) principal).getUserNm();

            // 관리자가 아닐 경우 본인 일과만 조회 가능
            if ("N".equals(planSettleVO.getAuthAdmin())) {
                planSettleVO.setUserId(userId);
                planSettleVO.setUserNm(userNm);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>일일 결산 조회</p>
     *
     * @param planSettleVO
     * @return Map
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/settle/planSettleDaySearch.do")
    public @ResponseBody
    Map<String, Object> planSettleDaySearch(@ModelAttribute PlanSettleVO planSettleVO) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            int cnt = planSettleService.selectPlanSettleDayListCnt(planSettleVO);
            planSettleVO.setTotalCnt(cnt);
            planSettleVO.setPaging();
            List<PlanSettleVO> planSettleDayList = planSettleService.selectPlanSettleDayList(planSettleVO);

            map.put("cnt", cnt);
            map.put("planSettleDayList", planSettleDayList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return map;
    }

    /**
     * <p>일일 결산 상세</p>
     *
     * @param planSettleVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/settle/planSettleDayDetail.do")
    public ModelAndView planSettleDayDetail(@ModelAttribute PlanSettleVO planSettleVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("pl/settle/planSettleDayDetail");

        try {
            // 분류별 할당 시간
            List<PlanSettleVO> timeList = planSettleService.selectPlanSettleDayTime(planSettleVO);
            mav.addObject("timeList", timeList);

            // 일과별 달성률
            List<PlanSettleVO> achvRateList = planSettleService.selectPlanSettleDayAchvRate(planSettleVO);
            mav.addObject("achvRateList", achvRateList);

            // 일과별 몰입도
            List<PlanSettleVO> concRateList = planSettleService.selectPlanSettleDayConcRate(planSettleVO);
            mav.addObject("concRateList", concRateList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>일일 결산 엑셀 출력</p>
     *
     * @param planSettleVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/settle/planSettleDayExcel.do")
    public ExcelUtil planSettleDayExcel(@ModelAttribute PlanSettleVO planSettleVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<PlanSettleVO> planSettleDayExcelList = planSettleService.selectPlanSettleDayExcelList(planSettleVO);

            // 컬럼명 설정
            colName.add("순번");
            colName.add("일자");
            colName.add("달성률");
            colName.add("몰입도");
            colName.add("점수");
            colName.add("메모");
            colName.add("담당자");

            // 컬럼 사이즈 설정
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(2000);
            colWidth.add(8000);
            colWidth.add(4000);

            // 데이터 설정
            for (int i = 0; i < planSettleDayExcelList.size(); i++) {
                PlanSettleVO vo = (PlanSettleVO) planSettleDayExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String rtneDate = vo.getRtneDate();
                String achvRate = vo.getAchvRate();
                String concRate = vo.getConcRate();
                String dailyScore = vo.getDailyScore();
                String memo = vo.getMemo();
                String planUser = vo.getPlanUser();
                String[] arr = {rn, rtneDate, achvRate, concRate, dailyScore, memo, planUser};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "일일 결산 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
