package com.jsplan.drp.sys.usermng;

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

import com.jsplan.drp.cmmn.obj.ComsMenuVO;
import com.jsplan.drp.cmmn.obj.ComsService;
import com.jsplan.drp.cmmn.obj.ComsVO;
import com.jsplan.drp.cmmn.util.ExcelUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Class : UserMngController
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 Controller
 */
@Controller
public class UserMngController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "UserMngService")
    private UserMngService userMngService;

    @Resource(name = "ComsService")
    private ComsService comsService;

    /**
     * <p>그룹 관리</p>
     *
     * @param userMngVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/grpMngList.do")
    public ModelAndView grpMngList(@ModelAttribute UserMngVO userMngVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("sys/usermng/grpMngList");

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
     * <p>그룹 조회</p>
     *
     * @param userMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/grpMngSearch.do")
    public @ResponseBody
    Map<String, Object> grpMngSearch(@ModelAttribute UserMngVO userMngVO) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            int cnt = userMngService.selectGrpMngListCnt(userMngVO);
            userMngVO.setTotalCnt(cnt);
            userMngVO.setPaging();
            List<UserMngVO> grpMngList = userMngService.selectGrpMngList(userMngVO);

            map.put("cnt", cnt);
            map.put("grpMngList", grpMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return map;
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param userMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/grpMngDetail.do")
    public ModelAndView grpMngDetail(@ModelAttribute UserMngVO userMngVO) throws Exception {
        ModelAndView mav = new ModelAndView("sys/usermng/grpMngDetail");
        String state = userMngVO.getState();

        try {
            if ("U".equals(state)) {
                UserMngVO vo = userMngService.selectGrpMngDetail(userMngVO);
                vo.setState(state);
                mav.addObject("userMngVO", vo);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>그룹 수정</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/grpMngUpdate.do")
    public @ResponseBody
    String grpMngUpdate(@ModelAttribute UserMngVO userMngVO) throws Exception {
        String code = null;
        String state = userMngVO.getState();

        try {
            if ("I".equals(state)) {
                code = userMngService.insertGrpMngData(userMngVO);
            }
            if ("U".equals(state)) {
                code = userMngService.updateGrpMngData(userMngVO);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>그룹 삭제</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/grpMngDelete.do")
    public @ResponseBody
    String grpMngDelete(@ModelAttribute UserMngVO userMngVO) throws Exception {
        String code = null;

        try {
            code = userMngService.deleteGrpMngData(userMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>그룹 엑셀 출력</p>
     *
     * @param userMngVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/grpMngExcel.do")
    public ExcelUtil grpMngExcel(@ModelAttribute UserMngVO userMngVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<UserMngVO> grpMngExcelList = userMngService.selectGrpMngExcelList(userMngVO);

            // 컬럼명 설정
            colName.add("순번");
            colName.add("그룹 코드");
            colName.add("그룹명");
            colName.add("그룹 설명");
            colName.add("등록자");
            colName.add("등록 일시");
            colName.add("수정자");
            colName.add("수정 일시");

            // 컬럼 사이즈 설정
            colWidth.add(2000);
            colWidth.add(6000);
            colWidth.add(6000);
            colWidth.add(6000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(4000);

            // 데이터 설정
            for (int i = 0; i < grpMngExcelList.size(); i++) {
                UserMngVO vo = (UserMngVO) grpMngExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String grpCd = vo.getGrpCd();
                String grpNm = vo.getGrpNm();
                String grpDesc = vo.getGrpDesc();
                String regUser = vo.getRegUser();
                String regDate = vo.getRegDate();
                String modUser = vo.getModUser();
                String modDate = vo.getModDate();
                String[] arr = {rn, grpCd, grpNm, grpDesc, regUser, regDate, modUser, modDate};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "그룹 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }

    /**
     * <p>사용자 관리</p>
     *
     * @param userMngVO
     * @param comsMenuVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userMngList.do")
    public ModelAndView userMngList(@ModelAttribute UserMngVO userMngVO, ComsMenuVO comsMenuVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("sys/usermng/userMngList");

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

            userMngVO.setSearchCd("userNm");

            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>사용자 조회</p>
     *
     * @param userMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userMngSearch.do")
    public @ResponseBody
    Map<String, Object> userMngSearch(@ModelAttribute UserMngVO userMngVO) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            int cnt = userMngService.selectUserMngListCnt(userMngVO);
            userMngVO.setTotalCnt(cnt);
            userMngVO.setPaging();
            List<UserMngVO> userMngList = userMngService.selectUserMngList(userMngVO);

            map.put("cnt", cnt);
            map.put("userMngList", userMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return map;
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param userMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userMngDetail.do")
    public ModelAndView userMngDetail(@ModelAttribute UserMngVO userMngVO, ComsVO comsVO)
        throws Exception {
        ModelAndView mav = new ModelAndView("sys/usermng/userMngDetail");
        String state = userMngVO.getState();

        try {
            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);

            // 공통 코드 : 사용자 유형
            List<ComsVO> userTypeList = comsService.selectComsCodeList("USER_TYPE");
            mav.addObject("userTypeList", userTypeList);

            if ("U".equals(state)) {
                // 사용자 기본 정보
                UserMngVO vo = userMngService.selectUserMngDetail(userMngVO);
                vo.setState(state);
                mav.addObject("userMngVO", vo);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return mav;
    }

    /**
     * <p>사용자 아이디 중복 체크</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userMngIdDupCheck.do")
    public @ResponseBody
    String userMngIdDupCheck(@ModelAttribute UserMngVO userMngVO) throws Exception {
        String code = null;

        try {
            code = userMngService.selectUserMngDupCnt(userMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userMngUpdate.do")
    public @ResponseBody
    String userMngUpdate(@ModelAttribute UserMngVO userMngVO) throws Exception {
        String code = null;
        String state = userMngVO.getState();

        try {
            if ("I".equals(state)) {
                code = userMngService.insertUserMngData(userMngVO);
            }
            if ("U".equals(state)) {
                code = userMngService.updateUserMngData(userMngVO);
            }
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>사용자 삭제</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userMngDelete.do")
    public @ResponseBody
    String userMngDelete(@ModelAttribute UserMngVO userMngVO) throws Exception {
        String code = null;

        try {
            code = userMngService.deleteUserMngData(userMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>권한 설정 팝업</p>
     *
     * @param userMngVO
     * @return ModelAndView
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userAuthMngPop.do")
    public ModelAndView userAuthMngPop(@ModelAttribute UserMngVO userMngVO) throws Exception {
        return new ModelAndView("sys/usermng/userAuthMngPop");
    }

    /**
     * <p>권한 목록 조회</p>
     *
     * @param userMngVO
     * @return JSONObject
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userAuthMngSearch.do")
    public @ResponseBody
    JSONObject userAuthMngSearch(@ModelAttribute UserMngVO userMngVO) throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray userAuthMngList = new JSONArray();

        try {
            userAuthMngList = userMngService.selectUserAuthMngList(userMngVO);
            jsonObject.put("userAuthMngList", userAuthMngList);
        } catch (Exception e) {
            logger.error("{}", e);
        }

        return jsonObject;
    }

    /**
     * <p>권한 설정 적용</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userAuthMngUpdate.do")
    public @ResponseBody
    String userAuthMngUpdate(@ModelAttribute UserMngVO userMngVO) throws Exception {
        String code = null;

        try {
            code = userMngService.updateUserAuthMngData(userMngVO);
        } catch (Exception e) {
            logger.error("{}", e);
            code = e.getClass().getName();
        }

        return code;
    }

    /**
     * <p>사용자 엑셀 출력</p>
     *
     * @param userMngVO
     * @param map
     * @return ExcelUtil
     * @throws Exception throws Exception
     */
    @RequestMapping(value = "/sys/usermng/userMngExcel.do")
    public ExcelUtil userMngExcel(@ModelAttribute UserMngVO userMngVO, ModelMap map)
        throws Exception {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        try {
            // 데이터 조회
            List<UserMngVO> userMngExcelList = userMngService.selectUserMngExcelList(userMngVO);

            // 컬럼명 설정
            colName.add("순번");
            colName.add("그룹 코드");
            colName.add("그룹명");
            colName.add("아이디");
            colName.add("성명");
            colName.add("휴대폰 번호");
            colName.add("이메일");
            colName.add("사용자 유형");
            colName.add("사용 여부");
            colName.add("등록 일시");
            colName.add("수정 일시");

            // 컬럼 사이즈 설정
            colWidth.add(2000);
            colWidth.add(4000);
            colWidth.add(6000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(4000);
            colWidth.add(6000);
            colWidth.add(6000);
            colWidth.add(4000);
            colWidth.add(6000);
            colWidth.add(6000);

            // 데이터 설정
            for (int i = 0; i < userMngExcelList.size(); i++) {
                UserMngVO vo = (UserMngVO) userMngExcelList.get(i);
                String rn = String.valueOf(vo.getRn());
                String grpCd = vo.getGrpCd();
                String grpNm = vo.getGrpNm();
                String userId = vo.getUserId();
                String userNm = vo.getUserNm();
                String mobileNum = vo.getMobileNum();
                String email = vo.getEmail();
                String userType = vo.getUserType();
                String useYn = vo.getUseYn();
                String regDate = vo.getRegDate();
                String modDate = vo.getModDate();
                String[] arr = {rn, grpCd, grpNm, userId, userNm, mobileNum, email, userType, useYn,
                    regDate, modDate};
                colValue.add(arr);
            }
        } catch (Exception e) {
            logger.error("{}", e);
        }

        map.put("excelName", "사용자 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
