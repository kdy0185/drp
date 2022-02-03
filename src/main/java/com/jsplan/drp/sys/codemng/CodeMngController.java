package com.jsplan.drp.sys.codemng;

import com.jsplan.drp.cmmn.obj.ComsMenuVO;
import com.jsplan.drp.cmmn.obj.ComsService;
import com.jsplan.drp.cmmn.obj.ComsVO;
import com.jsplan.drp.cmmn.util.ExcelUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : CodeMngController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 코드 관리 Controller
 */
@Controller
public class CodeMngController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "CodeMngService")
    private CodeMngService codeMngService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    /**
     * <p>코드 관리</p>
     *
     * @param codeMngVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/codeMngList.do")
    public ModelAndView codeMngList(@ModelAttribute CodeMngVO codeMngVO, ComsMenuVO comsMenuVO)
        throws Exception {
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
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>그룹 코드 조회</p>
     *
     * @param codeMngVO
     * @return Map
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/grpMngSearch.do")
    public @ResponseBody
    Map<String, Object> grpMngSearch(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            int cnt = codeMngService.selectGrpMngListCnt(codeMngVO);
            codeMngVO.setTotalCnt(cnt);
            codeMngVO.setPaging();
            List<CodeMngVO> grpMngList = codeMngService.selectGrpMngList(codeMngVO);

            map.put("cnt", cnt);
            map.put("grpMngList", grpMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return map;
    }

    /**
     * <p>공통 코드 조회</p>
     *
     * @param codeMngVO
     * @return Map
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/codeMngSearch.do")
    public @ResponseBody
    Map<String, Object> codeMngSearch(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            int cnt = codeMngService.selectCodeMngListCnt(codeMngVO);
            codeMngVO.setTotalCnt(cnt);
            codeMngVO.setPaging();
            List<CodeMngVO> codeMngList = codeMngService.selectCodeMngList(codeMngVO);

            map.put("cnt", cnt);
            map.put("codeMngList", codeMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return map;
    }

    /**
     * <p>그룹 코드 상세</p>
     *
     * @param codeMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/grpMngDetail.do")
    public ModelAndView grpMngDetail(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        ModelAndView mav = new ModelAndView("sys/codemng/grpMngDetail");

        try {
            CodeMngVO vo = codeMngService.selectGrpMngDetail(codeMngVO);
            mav.addObject("codeMngVO", vo);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>공통 코드 상세</p>
     *
     * @param codeMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/codeMngDetail.do")
    public ModelAndView codeMngDetail(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        ModelAndView mav = new ModelAndView("sys/codemng/codeMngDetail");

        try {
            CodeMngVO vo = codeMngService.selectCodeMngDetail(codeMngVO);
            mav.addObject("codeMngVO", vo);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>그룹 코드 수정</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/grpMngUpdate.do")
    public @ResponseBody
    String grpMngUpdate(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        String code = null;

        try {
            code = codeMngService.updateGrpMngData(codeMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>공통 코드 수정</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/codeMngUpdate.do")
    public @ResponseBody
    String codeMngUpdate(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        String code = null;

        try {
            code = codeMngService.updateCodeMngData(codeMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>그룹 코드 삭제</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/grpMngDelete.do")
    public @ResponseBody
    String grpMngDelete(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        String code = null;

        try {
            code = codeMngService.deleteGrpMngData(codeMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>공통 코드 삭제</p>
     *
     * @param codeMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/codeMngDelete.do")
    public @ResponseBody
    String codeMngDelete(@ModelAttribute CodeMngVO codeMngVO) throws Exception {
        String code = null;

        try {
            code = codeMngService.deleteCodeMngData(codeMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>그룹 코드 엑셀 출력</p>
     *
     * @param codeMngVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/grpMngExcel.do")
    public ExcelUtil grpMngExcel(@ModelAttribute CodeMngVO codeMngVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<CodeMngVO> grpMngExcelList = codeMngService.selectGrpMngExcelList(codeMngVO);

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
            for (int i = 0; i < grpMngExcelList.size(); i++) {
                CodeMngVO vo = (CodeMngVO) grpMngExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String grpCd = vo.getGrpCd();
                String grpNm = vo.getGrpNm();
                String useYn = vo.getUseYn();
                String detl = vo.getDetl();
                String regUser = vo.getRegUser();
                String regDate = vo.getRegDate();
                String modUser = vo.getModUser();
                String modDate = vo.getModDate();
                String[] arr = {rn, grpCd, grpNm, useYn, detl, regUser, regDate, modUser, modDate};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
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
     * @param codeMngVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/codemng/codeMngExcel.do")
    public ExcelUtil codeMngExcel(@ModelAttribute CodeMngVO codeMngVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<CodeMngVO> codeMngExcelList = codeMngService.selectCodeMngExcelList(codeMngVO);

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
            for (int i = 0; i < codeMngExcelList.size(); i++) {
                CodeMngVO vo = (CodeMngVO) codeMngExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String grpCd = vo.getGrpCd();
                String grpNm = vo.getGrpNm();
                String comCd = vo.getComCd();
                String comNm = vo.getComNm();
                String useYn = vo.getUseYn();
                String detl = vo.getDetl();
                String ord = vo.getOrd();
                String regUser = vo.getRegUser();
                String regDate = vo.getRegDate();
                String modUser = vo.getModUser();
                String modDate = vo.getModDate();
                String[] arr = {rn, grpCd, grpNm, comCd, comNm, useYn, detl, ord, regUser, regDate,
                    modUser, modDate};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "공통 코드 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
