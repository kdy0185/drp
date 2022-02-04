package com.jsplan.drp.sys.authmng;

import com.jsplan.drp.cmmn.bean.AvailableRoleHierarchy;
import com.jsplan.drp.cmmn.bean.ReloadableFilterInvocationSecurityMetadataSource;
import com.jsplan.drp.cmmn.obj.ComsMenuVO;
import com.jsplan.drp.cmmn.obj.ComsService;
import com.jsplan.drp.cmmn.obj.ComsVO;
import com.jsplan.drp.cmmn.util.ExcelUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @Class : AuthMngController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 권한 관리 Controller
 */
@Controller
public class AuthMngController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "AuthMngService")
    private AuthMngService authMngService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    @Resource
    private ReloadableFilterInvocationSecurityMetadataSource rfisms;

    @Resource
    private AvailableRoleHierarchy arh;

    /**
     * <p>권한 관리</p>
     *
     * @param authMngVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMngList.do")
    public ModelAndView authMngList(@ModelAttribute AuthMngVO authMngVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("sys/authmng/authMngList");

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
     * <p>권한 조회</p>
     *
     * @param authMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMngSearch.do")
    public @ResponseBody
    JSONObject authMngSearch(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        JSONObject authMngObject = new JSONObject();
        JSONArray authMngList = new JSONArray();

        try {
            authMngList = authMngService.selectAuthMngList(authMngVO);
            authMngObject.put("authMngList", authMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return authMngObject;
    }

    /**
     * <p>권한 상세</p>
     *
     * @param authMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMngDetail.do")
    public ModelAndView authMngDetail(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        ModelAndView mav = new ModelAndView("sys/authmng/authMngDetail");
        String state = authMngVO.getState();

        try {
            // 상위 권한 목록
            List<AuthMngVO> authList = authMngService.selectUpperAuthMngList(authMngVO);
            mav.addObject("authList", authList);

            if ("U".equals(state)) {
                AuthMngVO vo = authMngService.selectAuthMngDetail(authMngVO);
                vo.setState(state);
                mav.addObject("authMngVO", vo);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>권한 수정</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMngUpdate.do")
    public @ResponseBody
    String authMngUpdate(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        String code = null;
        String state = authMngVO.getState();

        try {
            if ("I".equals(state)) {
                code = authMngService.insertAuthMngData(authMngVO);
            }
            if ("U".equals(state)) {
                code = authMngService.updateAuthMngData(authMngVO);
            }

            // 권한 계층화 정보 Reload
            arh.setComsService(comsService);
            arh.reload();
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>권한 삭제</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMngDelete.do")
    public @ResponseBody
    String authMngDelete(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        String code = null;

        try {
            code = authMngService.deleteAuthMngData(authMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>사용자 설정 팝업</p>
     *
     * @param authMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authUserMngPop.do")
    public ModelAndView authUserMngPop(@ModelAttribute AuthMngVO authMngVO, ComsVO comsVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("sys/authmng/authUserMngPop");

        try {
            // ***************************** PAGE : S *****************************
            List<ComsVO> pageList = comsService.selectComsCodeList("PAGE_SIZE");
            mav.addObject("pageList", pageList);
            // ***************************** PAGE : E *****************************

            authMngVO.setSearchCd("userNm");

            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>사용자 목록 조회</p>
     *
     * @param authMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authUserMngSearch.do")
    public @ResponseBody
    Map<String, Object> authUserMngSearch(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            int cnt = authMngService.selectAuthUserMngListCnt(authMngVO);
            authMngVO.setTotalCnt(cnt);
            authMngVO.setPaging();
            List<AuthMngVO> authUserMngList = authMngService.selectAuthUserMngList(authMngVO);

            map.put("cnt", cnt);
            map.put("authUserMngList", authUserMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return map;
    }

    /**
     * <p>사용자 설정 적용</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authUserMngUpdate.do")
    public @ResponseBody
    String authUserMngUpdate(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        String code = null;

        try {
            code = authMngService.updateAuthUserMngData(authMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>메뉴 설정 팝업</p>
     *
     * @param authMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMenuMngPop.do")
    public ModelAndView authMenuMngPop(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        return new ModelAndView("sys/authmng/authMenuMngPop");
    }

    /**
     * <p>메뉴 목록 조회</p>
     *
     * @param authMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMenuMngSearch.do")
    public @ResponseBody
    JSONObject authMenuMngSearch(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray authMenuMngList = new JSONArray();

        try {
            authMenuMngList = authMngService.selectAuthMenuMngList(authMngVO);
            jsonObject.put("authMenuMngList", authMenuMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return jsonObject;
    }

    /**
     * <p>메뉴 설정 적용</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMenuMngUpdate.do")
    public @ResponseBody
    String authMenuMngUpdate(@ModelAttribute AuthMngVO authMngVO) throws Exception {
        String code = null;

        try {
            code = authMngService.updateAuthMenuMngData(authMngVO);

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
     * <p>권한 엑셀 출력</p>
     *
     * @param authMngVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/authmng/authMngExcel.do")
    public ExcelUtil authMngExcel(@ModelAttribute AuthMngVO authMngVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<AuthMngVO> authMngExcelList = authMngService.selectAuthMngExcelList(authMngVO);

            // 컬럼명 설정
            colName.add("순번");
            colName.add("권한 코드");
            colName.add("상위 권한 코드");
            colName.add("권한명");
            colName.add("권한 설명");
            colName.add("권한 수준");
            colName.add("권한 순서");
            colName.add("사용 여부");

            // 컬럼 사이즈 설정
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(6000);
            colWidth.add(6000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(4000);

            // 데이터 설정
            for (int i = 0; i < authMngExcelList.size(); i++) {
                AuthMngVO vo = (AuthMngVO) authMngExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String authCd = vo.getAuthCd();
                String upperAuthCd = vo.getUpperAuthCd();
                String authNm = vo.getAuthNm();
                String authDesc = vo.getAuthDesc();
                String authLv = vo.getAuthLv();
                String authOrd = vo.getAuthOrd();
                String useYn = vo.getUseYn();
                String[] arr = {rn, authCd, upperAuthCd, authNm, authDesc, authLv, authOrd, useYn};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "권한 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
