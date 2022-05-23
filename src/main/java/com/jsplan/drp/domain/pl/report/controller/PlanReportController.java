package com.jsplan.drp.domain.pl.report.controller;

import com.jsplan.drp.domain.pl.report.dto.PlanReportListDTO;
import com.jsplan.drp.domain.pl.report.dto.PlanReportSearchDTO;
import com.jsplan.drp.domain.pl.report.service.PlanReportService;
import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
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
 * @Class : PlanReportController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 데일리 리포트 Controller
 */
@Controller
@RequiredArgsConstructor
public class PlanReportController {

    private final PlanReportService planReportService;
    private final ComsService comsService;

    /**
     * <p>데일리 리포트</p>
     *
     * @param comsMenuDTO (메뉴 정보)
     * @return ModelAndView (데일리 리포트 페이지 정보)
     */
    @PostMapping(value = "/pl/report/planReportList.do")
    public ModelAndView planReportList(@ModelAttribute ComsMenuDTO comsMenuDTO) {
        ModelAndView mav = new ModelAndView("pl/report/planReportList");
        PlanReportSearchDTO searchDTO = new PlanReportSearchDTO();

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuDTO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuDTO = comsService.selectComsMenuDetail(comsMenuDTO.getMenuCd());
            mav.addObject("comsMenuDTO", comsMenuDTO);
            // ***************************** MENU : E *****************************

            // ***************************** PAGE : S *****************************
            List<ComsDTO> pageList = comsService.selectComsCodeList("PAGE_SIZE");
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
     * <p>분류 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (분류 목록)
     */
    @GetMapping(value = "/pl/report/planReportRtneCtg.do")
    public @ResponseBody List<PlanReportListDTO> planReportRtneCtg(
        @ModelAttribute PlanReportSearchDTO searchDTO) {
        return planReportService.selectPlanReportRtneCtgList(searchDTO);
    }

    /**
     * <p>일과 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (일과 목록)
     */
    @GetMapping(value = "/pl/report/planReportSearch.do")
    public @ResponseBody Page<PlanReportListDTO> planReportSearch(
        @ModelAttribute PlanReportSearchDTO searchDTO) {
        return planReportService.selectPlanReportList(searchDTO);
    }

    /**
     * <p>데일리 리포트 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/pl/report/planReportExcel.do")
    public ExcelUtil planReportExcel(@ModelAttribute PlanReportSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<PlanReportListDTO> excelList = planReportService.selectPlanReportExcelList(searchDTO);

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
        for (PlanReportListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String rtneId = String.valueOf(listDTO.getRtneId());
            String rtneDate = String.valueOf(listDTO.getRtneDate());
            String rtneOrd = String.valueOf(listDTO.getRtneOrd());
            String rtneStartDate = listDTO.getRtneDateTime();
            String rtneCtgNm = listDTO.getRtneCtgNm();
            String rtneNm = listDTO.getRtneNm();
            String achvRate = listDTO.getAchvRate();
            String concRate = listDTO.getConcRate();
            String planUser = listDTO.getPlanUser();
            String[] arr = {rn, rtneId, rtneDate, rtneOrd, rtneStartDate, rtneCtgNm, rtneNm,
                achvRate, concRate, planUser};
            colValue.add(arr);
        }

        map.put("excelName", "데일리 리포트 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
