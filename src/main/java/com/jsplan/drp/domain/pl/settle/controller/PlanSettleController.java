package com.jsplan.drp.domain.pl.settle.controller;

import com.jsplan.drp.domain.pl.settle.dto.PlanSettleDetailDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleListDTO;
import com.jsplan.drp.domain.pl.settle.dto.PlanSettleSearchDTO;
import com.jsplan.drp.domain.pl.settle.service.PlanSettleService;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : PlanSettleController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 일일 결산 Controller
 */
@Controller
@RequiredArgsConstructor
public class PlanSettleController {

    private final PlanSettleService planSettleService;
    private final ComsService comsService;

    /**
     * <p>일일 결산</p>
     *
     * @param comsMenuVO (메뉴 VO)
     * @return ModelAndView (일일 결산 페이지 정보)
     */
    @PostMapping(value = "/pl/settle/planSettleDayList.do")
    public ModelAndView planSettleDayList(@ModelAttribute ComsMenuVO comsMenuVO) {
        ModelAndView mav = new ModelAndView("pl/settle/planSettleDayList");
        PlanSettleSearchDTO searchDTO = new PlanSettleSearchDTO();

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

            // 기본 검색 조건 설정
            searchDTO.fixUserInfo();
            mav.addObject("searchDTO", searchDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>일일 결산 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (일일 결산 목록)
     */
    @GetMapping(value = "/pl/settle/planSettleDaySearch.do")
    public @ResponseBody Page<PlanSettleListDTO> planSettleDaySearch(
        @ModelAttribute PlanSettleSearchDTO searchDTO) {
        return planSettleService.selectPlanSettleDayList(searchDTO);
    }

    /**
     * <p>일일 결산 상세</p>
     *
     * @param searchDTO (조회 조건)
     * @return ModelAndView (일일 결산 상세 페이지 정보)
     */
    @PostMapping(value = "/pl/settle/planSettleDayDetail.do")
    public ModelAndView planSettleDayDetail(@ModelAttribute PlanSettleSearchDTO searchDTO) {
        ModelAndView mav = new ModelAndView("pl/settle/planSettleDayDetail");
        PlanSettleDetailDTO detailDTO = new PlanSettleDetailDTO();

        // 분류별 할당 시간
        List<PlanSettleDetailDTO> timeList = planSettleService.selectPlanSettleDayTime(searchDTO);
        mav.addObject("timeList", timeList);

        // 일과별 달성률
        List<PlanSettleDetailDTO> achvRateList = planSettleService.selectPlanSettleDayAchvRate(
            searchDTO);
        mav.addObject("achvRateList", achvRateList);

        // 일과별 몰입도
        List<PlanSettleDetailDTO> concRateList = planSettleService.selectPlanSettleDayConcRate(
            searchDTO);
        mav.addObject("concRateList", concRateList);

        detailDTO.setRtneDate(searchDTO.getRtneDate());
        mav.addObject("detailDTO", detailDTO);
        return mav;
    }

    /**
     * <p>분류별 할당 시간 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (분류별 할당 시간 목록)
     */
    @GetMapping(value = "/pl/settle/planSettleDayTimeSearch.do")
    public @ResponseBody
    List<PlanSettleDetailDTO> planSettleDayTimeSearch(
        @ModelAttribute PlanSettleSearchDTO searchDTO) {
        return planSettleService.selectPlanSettleDayTime(searchDTO);
    }

    /**
     * <p>일과별 달성률 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (일과별 달성률 목록)
     */
    @GetMapping(value = "/pl/settle/planSettleDayAchvRateSearch.do")
    public @ResponseBody
    List<PlanSettleDetailDTO> planSettleDayAchvRateSearch(
        @ModelAttribute PlanSettleSearchDTO searchDTO) {
        return planSettleService.selectPlanSettleDayAchvRate(searchDTO);
    }

    /**
     * <p>일과별 몰입도 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (일과별 몰입도 목록)
     */
    @GetMapping(value = "/pl/settle/planSettleDayConcRateSearch.do")
    public @ResponseBody
    List<PlanSettleDetailDTO> planSettleDayConcRateSearch(
        @ModelAttribute PlanSettleSearchDTO searchDTO) {
        return planSettleService.selectPlanSettleDayConcRate(searchDTO);
    }

    /**
     * <p>일일 결산 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/pl/settle/planSettleDayExcel.do")
    public ExcelUtil planSettleDayExcel(@ModelAttribute PlanSettleSearchDTO searchDTO,
        ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<PlanSettleListDTO> excelList = planSettleService.selectPlanSettleDayExcelList(
            searchDTO);

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
        for (PlanSettleListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String rtneDate = String.valueOf(listDTO.getRtneDate());
            String achvRate = listDTO.getAchvRate();
            String concRate = listDTO.getConcRate();
            String dailyScore = String.valueOf(listDTO.getDailyScore());
            String memo = listDTO.getMemo();
            String planUser = listDTO.getPlanUser();
            String[] arr = {rn, rtneDate, achvRate, concRate, dailyScore, memo, planUser};
            colValue.add(arr);
        }

        map.put("excelName", "일일 결산 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
