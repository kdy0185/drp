package com.jsplan.drp.domain.sys.codemng.controller;

import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeGrpMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngDetailDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngListDTO;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngRequest;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngResponse;
import com.jsplan.drp.domain.sys.codemng.dto.CodeMngSearchDTO;
import com.jsplan.drp.domain.sys.codemng.service.CodeMngService;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
 * @Class : CodeMngController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 코드 관리 Controller
 */
@Controller
@RequiredArgsConstructor
public class CodeMngController {

    private final CodeMngService codeMngService;
    private final ComsService comsService;

    /**
     * <p>코드 관리</p>
     *
     * @param comsMenuVO (메뉴 VO)
     * @return ModelAndView (코드 관리 페이지 정보)
     */
    @PostMapping(value = "/sys/codemng/codeMngList.do")
    public ModelAndView codeMngList(@ModelAttribute ComsMenuVO comsMenuVO) {
        ModelAndView mav = new ModelAndView("sys/codemng/codeMngList");

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
            mav.addObject("searchDTO", new CodeMngSearchDTO("grpNm"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>그룹 코드 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (그룹 코드 목록)
     */
    @GetMapping(value = "/sys/codemng/codeGrpMngSearch.do")
    public @ResponseBody Page<CodeGrpMngListDTO> codeGrpMngSearch(
        @ModelAttribute CodeMngSearchDTO searchDTO) {
        return codeMngService.selectCodeGrpMngList(searchDTO);
    }

    /**
     * <p>공통 코드 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (공통 코드 목록)
     */
    @GetMapping(value = "/sys/codemng/codeMngSearch.do")
    public @ResponseBody Page<CodeMngListDTO> codeMngSearch(
        @ModelAttribute CodeMngSearchDTO searchDTO) {
        return codeMngService.selectCodeMngList(searchDTO);
    }

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param request (그룹 코드 정보)
     * @return ModelAndView (그룹 코드 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/codemng/codeGrpMngDetail.do")
    public ModelAndView codeGrpMngDetail(@ModelAttribute CodeGrpMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/codemng/codeGrpMngDetail");
        CodeGrpMngDetailDTO detailDTO = codeMngService.selectCodeGrpMngDetail(request);

        mav.addObject("detailDTO", detailDTO);
        return mav;
    }

    /**
     * <p>공통 코드 상세</p>
     *
     * @param request (공통 코드 정보)
     * @return ModelAndView (공통 코드 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/codemng/codeMngDetail.do")
    public ModelAndView codeMngDetail(@ModelAttribute CodeMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/codemng/codeMngDetail");
        CodeMngDetailDTO detailDTO = codeMngService.selectCodeMngDetail(request);

        mav.addObject("detailDTO", detailDTO);
        return mav;
    }

    /**
     * <p>그룹 코드 수정</p>
     *
     * @param request (그룹 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/codemng/codeGrpMngUpdate.do")
    public @ResponseBody CodeMngResponse codeGrpMngUpdate(
        @ModelAttribute @Valid CodeGrpMngRequest request) {
        return codeMngService.updateCodeGrpMngData(request);
    }

    /**
     * <p>공통 코드 수정</p>
     *
     * @param request (공통 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/codemng/codeMngUpdate.do")
    public @ResponseBody CodeMngResponse codeMngUpdate(
        @ModelAttribute @Valid CodeMngRequest request) {
        return codeMngService.updateCodeMngData(request);
    }

    /**
     * <p>그룹 코드 삭제</p>
     *
     * @param request (그룹 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @DeleteMapping(value = "/sys/codemng/codeGrpMngDelete.do")
    public @ResponseBody CodeMngResponse codeGrpMngDelete(
        @ModelAttribute CodeGrpMngRequest request) {
        return codeMngService.deleteCodeGrpMngData(request);
    }

    /**
     * <p>공통 코드 삭제</p>
     *
     * @param request (공통 코드 정보)
     * @return CodeMngResponse (응답 정보)
     */
    @DeleteMapping(value = "/sys/codemng/codeMngDelete.do")
    public @ResponseBody CodeMngResponse codeMngDelete(@ModelAttribute CodeMngRequest request) {
        return codeMngService.deleteCodeMngData(request);
    }

    /**
     * <p>그룹 코드 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/sys/codemng/codeGrpMngExcel.do")
    public ExcelUtil codeGrpMngExcel(@ModelAttribute CodeMngSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<CodeGrpMngListDTO> excelList = codeMngService.selectCodeGrpMngExcelList(searchDTO);

        // 컬럼명 설정
        colName.add("순번");
        colName.add("그룹 코드");
        colName.add("그룹 코드명");
        colName.add("사용 여부");
        colName.add("비고");
        colName.add("등록자");
        colName.add("등록 일시");
        colName.add("수정자");
        colName.add("수정 일시");

        // 컬럼 사이즈 설정
        colWidth.add(2000);
        colWidth.add(6000);
        colWidth.add(6000);
        colWidth.add(4000);
        colWidth.add(6000);
        colWidth.add(4000);
        colWidth.add(6000);
        colWidth.add(4000);
        colWidth.add(6000);

        // 데이터 설정
        for (CodeGrpMngListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String grpCd = listDTO.getGrpCd();
            String grpNm = listDTO.getGrpNm();
            String useYn = listDTO.getUseYn();
            String detl = listDTO.getDetl();
            String regUser = listDTO.getRegUser();
            String regDate = listDTO.getRegDate();
            String modUser = listDTO.getModUser();
            String modDate = listDTO.getModDate();
            String[] arr = {rn, grpCd, grpNm, useYn, detl, regUser, regDate, modUser, modDate};
            colValue.add(arr);
        }

        map.put("excelName", "그룹 코드 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }

    /**
     * <p>공통 코드 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/sys/codemng/codeMngExcel.do")
    public ExcelUtil codeMngExcel(@ModelAttribute CodeMngSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<CodeMngListDTO> excelList = codeMngService.selectCodeMngExcelList(searchDTO);

        // 컬럼명 설정
        colName.add("순번");
        colName.add("그룹 코드");
        colName.add("그룹 코드명");
        colName.add("공통 코드");
        colName.add("공통 코드명");
        colName.add("사용 여부");
        colName.add("비고");
        colName.add("정렬 순서");
        colName.add("등록자");
        colName.add("등록 일시");
        colName.add("수정자");
        colName.add("수정 일시");

        // 컬럼 사이즈 설정
        colWidth.add(2000);
        colWidth.add(6000);
        colWidth.add(6000);
        colWidth.add(6000);
        colWidth.add(6000);
        colWidth.add(4000);
        colWidth.add(6000);
        colWidth.add(4000);
        colWidth.add(4000);
        colWidth.add(6000);
        colWidth.add(4000);
        colWidth.add(6000);

        // 데이터 설정
        for (CodeMngListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String grpCd = listDTO.getGrpCd();
            String grpNm = listDTO.getGrpNm();
            String comCd = listDTO.getComCd();
            String comNm = listDTO.getComNm();
            String useYn = listDTO.getUseYn();
            String detl = listDTO.getDetl();
            String ord = String.valueOf(listDTO.getOrd());
            String regUser = listDTO.getRegUser();
            String regDate = listDTO.getRegDate();
            String modUser = listDTO.getModUser();
            String modDate = listDTO.getModDate();
            String[] arr = {rn, grpCd, grpNm, comCd, comNm, useYn, detl, ord, regUser, regDate,
                modUser, modDate};
            colValue.add(arr);
        }

        map.put("excelName", "공통 코드 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
