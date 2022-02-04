package com.jsplan.drp.sys.menumng;

import com.jsplan.drp.cmmn.bean.ReloadableFilterInvocationSecurityMetadataSource;
import com.jsplan.drp.cmmn.obj.ComsMenuVO;
import com.jsplan.drp.cmmn.obj.ComsService;
import com.jsplan.drp.cmmn.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : MenuMngController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 메뉴 관리 Controller
 */
@Controller
public class MenuMngController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "MenuMngService")
    private MenuMngService menuMngService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    @Resource
    private ReloadableFilterInvocationSecurityMetadataSource rfisms;

    /**
     * <p>메뉴 관리</p>
     *
     * @param menuMngVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuMngList.do")
    public ModelAndView menuMngList(@ModelAttribute MenuMngVO menuMngVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("sys/menumng/menuMngList");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuVO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuVO = comsService.selectComsMenuDetail(comsMenuVO.getMenuCd());
            mav.addObject("comsMenuVO", comsMenuVO);
            // ***************************** MENU : E *****************************
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>메뉴 조회</p>
     *
     * @param menuMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuMngSearch.do")
    public @ResponseBody
    JSONObject menuMngSearch(@ModelAttribute MenuMngVO menuMngVO) throws Exception {
        JSONObject menuMngObject = new JSONObject();
        JSONArray menuMngList = new JSONArray();

        try {
            menuMngList = menuMngService.selectMenuMngList(menuMngVO);
            menuMngObject.put("menuMngList", menuMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return menuMngObject;
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuMngDetail.do")
    public ModelAndView menuMngDetail(@ModelAttribute MenuMngVO menuMngVO) throws Exception {
        ModelAndView mav = new ModelAndView("sys/menumng/menuMngDetail");
        String state = menuMngVO.getState();

        try {
            if ("U".equals(state)) {
                MenuMngVO vo = menuMngService.selectMenuMngDetail(menuMngVO);
                vo.setState(state);
                mav.addObject("menuMngVO", vo);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>메뉴 수정</p>
     *
     * @param menuMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuMngUpdate.do")
    public @ResponseBody
    String menuMngUpdate(@ModelAttribute MenuMngVO menuMngVO) throws Exception {
        String code = null;
        String state = menuMngVO.getState();

        try {
            if ("I".equals(state)) {
                code = menuMngService.insertMenuMngData(menuMngVO);
            }
            if ("U".equals(state)) {
                code = menuMngService.updateMenuMngData(menuMngVO);
            }

            // URL별 권한 정보 Reload
            rfisms.setComsService(comsService);
            rfisms.reload();
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>메뉴 삭제</p>
     *
     * @param menuMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuMngDelete.do")
    public @ResponseBody
    String menuMngDelete(@ModelAttribute MenuMngVO menuMngVO) throws Exception {
        String code = null;

        try {
            code = menuMngService.deleteMenuMngData(menuMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>권한 설정 팝업</p>
     *
     * @param menuMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuAuthMngPop.do")
    public ModelAndView menuAuthMngPop(@ModelAttribute MenuMngVO menuMngVO) throws Exception {
        return new ModelAndView("sys/menumng/menuAuthMngPop");
    }

    /**
     * <p>권한 목록 조회</p>
     *
     * @param menuMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuAuthMngSearch.do")
    public @ResponseBody
    JSONObject menuAuthMngSearch(@ModelAttribute MenuMngVO menuMngVO) throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray menuAuthMngList = new JSONArray();

        try {
            menuAuthMngList = menuMngService.selectMenuAuthMngList(menuMngVO);
            jsonObject.put("menuAuthMngList", menuAuthMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return jsonObject;
    }

    /**
     * <p>권한 설정 적용</p>
     *
     * @param menuMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuAuthMngUpdate.do")
    public @ResponseBody
    String menuAuthMngUpdate(@ModelAttribute MenuMngVO menuMngVO) throws Exception {
        String code = null;

        try {
            code = menuMngService.updateMenuAuthMngData(menuMngVO);

            // URL별 권한 정보 Reload
            rfisms.setComsService(comsService);
            rfisms.reload();
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>메뉴 엑셀 출력</p>
     *
     * @param menuMngVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/menumng/menuMngExcel.do")
    public ExcelUtil menuMngExcel(@ModelAttribute MenuMngVO menuMngVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<MenuMngVO> menuMngExcelList = menuMngService.selectMenuMngExcelList(menuMngVO);

            // 컬럼명 설정
            colName.add("순번");
            colName.add("메뉴 코드");
            colName.add("메뉴명");
            colName.add("이동 주소");
            colName.add("메뉴 설명");
            colName.add("메뉴 수준");
            colName.add("메뉴 순서");
            colName.add("사용 여부");

            // 컬럼 사이즈 설정
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(6000);
            colWidth.add(6000);
            colWidth.add(6000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(4000);

            // 데이터 설정
            for (int i = 0; i < menuMngExcelList.size(); i++) {
                MenuMngVO vo = (MenuMngVO) menuMngExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String menuCd = vo.getMenuCd();
                String menuNm = vo.getMenuNm();
                String menuUrl = vo.getMenuUrl();
                String menuDesc = vo.getMenuDesc();
                String menuLv = vo.getMenuLv();
                String menuOrd = vo.getMenuOrd();
                String useYn = vo.getUseYn();
                String[] arr = {rn, menuCd, menuNm, menuUrl, menuDesc, menuLv, menuOrd, useYn};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "메뉴 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }

}
