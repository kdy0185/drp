package com.jsplan.drp.domain.pl.ctgopt.controller;

import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptDetailDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptListDTO;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptRequest;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptResponse;
import com.jsplan.drp.domain.pl.ctgopt.dto.PlanCtgOptSearchDTO;
import com.jsplan.drp.domain.pl.ctgopt.service.PlanCtgOptService;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.obj.vo.DetailStatus;
import com.jsplan.drp.global.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : PlanCtgOptController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 분류 옵션 설정 Controller
 */
@Controller
@RequiredArgsConstructor
public class PlanCtgOptController {

    private final PlanCtgOptService planCtgOptService;
    private final ComsService comsService;

    /**
     * <p>분류 옵션 설정</p>
     *
     * @param comsMenuDTO (메뉴 정보)
     * @return ModelAndView (분류 옵션 설정 페이지 정보)
     */
    @PostMapping(value = "/pl/ctgopt/planCtgOptList.do")
    public ModelAndView planCtgOptList(@ModelAttribute ComsMenuDTO comsMenuDTO) {
        ModelAndView mav = new ModelAndView("pl/ctgopt/planCtgOptList");
        PlanCtgOptSearchDTO searchDTO = new PlanCtgOptSearchDTO();

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuDTO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuDTO = comsService.selectComsMenuDetail(comsMenuDTO.getMenuCd());
            mav.addObject("comsMenuDTO", comsMenuDTO);
            // ***************************** MENU : E *****************************

            // 기본 검색 조건 설정
            searchDTO.fixUserInfo();
            mav.addObject("searchDTO", searchDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>분류 옵션 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONObject (분류 옵션 목록)
     */
    @GetMapping(value = "/pl/ctgopt/planCtgOptSearch.do")
    public @ResponseBody JSONObject planCtgOptSearch(
        @ModelAttribute PlanCtgOptSearchDTO searchDTO) {
        JSONObject planCtgOptObject = new JSONObject();
        JSONArray planCtgOptList = planCtgOptService.selectPlanCtgOptList(searchDTO);
        planCtgOptObject.put("planCtgOptList", planCtgOptList);

        return planCtgOptObject;
    }

    /**
     * <p>분류 옵션 상세</p>
     *
     * @param request (분류 옵션 정보)
     * @return ModelAndView (분류 옵션 설정 상세 페이지 정보)
     */
    @PostMapping(value = "/pl/ctgopt/planCtgOptDetail.do")
    public ModelAndView planCtgOptDetail(@ModelAttribute PlanCtgOptRequest request) {
        ModelAndView mav = new ModelAndView("pl/ctgopt/planCtgOptDetail");
        PlanCtgOptDetailDTO detailDTO = new PlanCtgOptDetailDTO();

        if (DetailStatus.INSERT.equals(request.getDetailStatus())) {
            detailDTO.fixUserInfo(); // 로그인 정보 설정
        }

        if (DetailStatus.UPDATE.equals(request.getDetailStatus())) {
            detailDTO = planCtgOptService.selectPlanCtgOptDetail(request);
        }

        detailDTO.setDetailStatus(request.getDetailStatus());
        mav.addObject("detailDTO", detailDTO);
        return mav;
    }

    /**
     * <p>분류 옵션 등록</p>
     *
     * @param request (분류 옵션 정보)
     * @return PlanCtgOptResponse (응답 정보)
     */
    @PostMapping(value = "/pl/ctgopt/planCtgOptInsert.do")
    public @ResponseBody PlanCtgOptResponse planCtgOptInsert(
        @ModelAttribute @Valid PlanCtgOptRequest request) {
        return planCtgOptService.insertPlanCtgOptData(request);
    }

    /**
     * <p>분류 옵션 수정</p>
     *
     * @param request (분류 옵션 정보)
     * @return PlanCtgOptResponse (응답 정보)
     */
    @PutMapping(value = "/pl/ctgopt/planCtgOptUpdate.do")
    public @ResponseBody PlanCtgOptResponse planCtgOptUpdate(
        @ModelAttribute @Valid PlanCtgOptRequest request) {
        return planCtgOptService.updatePlanCtgOptData(request);
    }

    /**
     * <p>분류 옵션 삭제</p>
     *
     * @param request (분류 옵션 정보)
     * @return PlanCtgOptResponse (응답 정보)
     */
    @DeleteMapping(value = "/pl/ctgopt/planCtgOptDelete.do")
    public @ResponseBody PlanCtgOptResponse planCtgOptDelete(
        @ModelAttribute PlanCtgOptRequest request) {
        return planCtgOptService.deletePlanCtgOptData(request);
    }

    /**
     * <p>분류 옵션 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/pl/ctgopt/planCtgOptExcel.do")
    public ExcelUtil planCtgOptExcel(@ModelAttribute PlanCtgOptSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<PlanCtgOptListDTO> excelList = planCtgOptService.selectPlanCtgOptExcelList(searchDTO);

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
        for (PlanCtgOptListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String rtneCtgNm = listDTO.getRtneCtgNm();
            String rtneCtgCd = listDTO.getRtneCtgCd();
            String wtVal = String.valueOf(listDTO.getWtVal());
            String recgTime = listDTO.getRecgTime();
            String rtneDate = listDTO.getRtneDate();
            String useYn = listDTO.getUseYn();
            String planUser = listDTO.getPlanUser();
            String[] arr = {rn, rtneCtgNm, rtneCtgCd, wtVal, recgTime, rtneDate, useYn, planUser};
            colValue.add(arr);
        }

        map.put("excelName", "분류 옵션 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
