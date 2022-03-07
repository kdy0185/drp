package com.jsplan.drp.domain.pl.report.controller;

import com.jsplan.drp.domain.pl.report.service.PlanReportService;
import com.jsplan.drp.domain.pl.report.entity.PlanReportVO;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.entity.UserVO;
import com.jsplan.drp.global.util.ExcelUtil;
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
 * @Class : PlanReportController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 데일리 리포트 Controller
 */
@Controller
public class PlanReportController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "PlanReportService")
    private PlanReportService planReportService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    /**
     * <p>데일리 리포트</p>
     *
     * @param planReportVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/report/planReportList.do")
    public ModelAndView planReportList(@ModelAttribute PlanReportVO planReportVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("pl/report/planReportList");

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
            if ("N".equals(planReportVO.getAuthAdmin())) {
                planReportVO.setUserId(userId);
                planReportVO.setUserNm(userNm);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>분류 조회</p>
     *
     * @param planReportVO
     * @return List
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/report/planReportRtneCtg.do")
    public @ResponseBody
    List<PlanReportVO> planReportRtneCtg(
        @ModelAttribute PlanReportVO planReportVO) throws Exception {
        List<PlanReportVO> rtneCtgList = null;

        try {
            rtneCtgList = planReportService.selectPlanReportRtneCtgList(planReportVO);
        } catch (Exception e) {
            logger.error("{}", e);
        }
        return rtneCtgList;
    }

    /**
     * <p>일과 조회</p>
     *
     * @param planReportVO
     * @return Map
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/report/planReportSearch.do")
    public @ResponseBody
    Map<String, Object> planReportSearch(@ModelAttribute PlanReportVO planReportVO) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            int cnt = planReportService.selectPlanReportListCnt(planReportVO);
            planReportVO.setTotalCnt(cnt);
            planReportVO.setPaging();
            List<PlanReportVO> planReportList = planReportService.selectPlanReportList(planReportVO);

            map.put("cnt", cnt);
            map.put("planReportList", planReportList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return map;
    }

    /**
     * <p>데일리 리포트 엑셀 출력</p>
     *
     * @param planReportVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/pl/report/planReportExcel.do")
    public ExcelUtil planReportExcel(@ModelAttribute PlanReportVO planReportVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<PlanReportVO> planReportExcelList = planReportService.selectPlanReportExcelList(planReportVO);

            // 컬럼명 설정
            colName.add("순번");
            colName.add("루틴 코드");
            colName.add("일자");
            colName.add("루틴 순서");
            colName.add("일시");
            colName.add("분류");
            colName.add("일과");
            colName.add("달성률");
            colName.add("몰입도");
            colName.add("담당자");

            // 컬럼 사이즈 설정
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(2000);
            colWidth.add(8000);
            colWidth.add(4000);
            colWidth.add(8000);
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(4000);

            // 데이터 설정
            for (int i = 0; i < planReportExcelList.size(); i++) {
                PlanReportVO vo = (PlanReportVO) planReportExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String rtneCd = vo.getRtneCd();
                String rtneDate = vo.getRtneDate();
                String rtneOrd = vo.getRtneOrd();
                String rtneStartDate = vo.getRtneStartDate();
                String rtneCtgNm = vo.getRtneCtgNm();
                String rtneNm = vo.getRtneNm();
                String achvRate = vo.getAchvRate();
                String concRate = vo.getConcRate();
                String planUser = vo.getPlanUser();
                String[] arr = {rn, rtneCd, rtneDate, rtneOrd, rtneStartDate, rtneCtgNm, rtneNm,
                    achvRate, concRate, planUser};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "데일리 리포트 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
