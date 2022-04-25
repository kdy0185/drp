package com.jsplan.drp.domain.sys.usermng.controller;

import com.jsplan.drp.domain.sys.usermng.dto.UserMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngListDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngRequest;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngResponse;
import com.jsplan.drp.domain.sys.usermng.dto.UserMngSearchDTO;
import com.jsplan.drp.domain.sys.usermng.service.UserMngService;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.util.ExcelUtil;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Class : UserMngController
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 Controller
 */
@Controller
@RequiredArgsConstructor
public class UserMngController {

    private final UserMngService userMngService;
    private final ComsService comsService;

    /**
     * <p>사용자 관리</p>
     *
     * @param comsMenuVO (메뉴 VO)
     * @return ModelAndView (사용자 관리 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngList.do")
    public ModelAndView userMngList(@ModelAttribute ComsMenuVO comsMenuVO) {
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

            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);

            // 기본 검색 조건 설정
            mav.addObject("searchDTO", new UserMngSearchDTO("userNm"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>사용자 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (페이징 목록)
     */
    @GetMapping(value = "/sys/usermng/userMngSearch.do")
    public @ResponseBody Page<UserMngListDTO> userMngSearch(
        @ModelAttribute UserMngSearchDTO searchDTO) {
        return userMngService.selectUserMngList(searchDTO);
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param request (사용자 정보)
     * @return ModelAndView (사용자 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngDetail.do")
    public ModelAndView userMngDetail(@ModelAttribute UserMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/usermng/userMngDetail");
        UserMngDetailDTO detailDTO = new UserMngDetailDTO();

        try {
            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);

            // 공통 코드 : 사용자 유형
            List<ComsVO> userTypeList = comsService.selectComsCodeList("USER_TYPE");
            mav.addObject("userTypeList", userTypeList);

            if ("U".equals(request.getState())) {
                detailDTO = userMngService.selectUserMngDetail(request);
            }

            detailDTO.setState(request.getState());
            mav.addObject("detailDTO", detailDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>사용자 아이디 중복 체크</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngIdDupCheck.do")
    public @ResponseBody UserMngResponse userMngIdDupCheck(@ModelAttribute UserMngRequest request) {
        return userMngService.selectUserMngDupCnt(request);
    }

    /**
     * <p>사용자 등록</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @PostMapping(value = "/sys/usermng/userMngInsert.do")
    public @ResponseBody UserMngResponse userMngInsert(
        @ModelAttribute @Valid UserMngRequest request) {
        return userMngService.insertUserMngData(request);
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/usermng/userMngUpdate.do")
    public @ResponseBody UserMngResponse userMngUpdate(
        @ModelAttribute @Valid UserMngRequest request) {
        return userMngService.updateUserMngData(request);
    }

    /**
     * <p>사용자 삭제</p>
     *
     * @param request (사용자 정보)
     * @return UserMngResponse (응답 정보)
     */
    @DeleteMapping(value = "/sys/usermng/userMngDelete.do")
    public @ResponseBody UserMngResponse userMngDelete(@ModelAttribute UserMngRequest request) {
        return userMngService.deleteUserMngData(request);
    }

    /**
     * <p>권한 설정 팝업</p>
     *
     * @param request (권한 정보)
     * @return ModelAndView (권한 설정 페이지 정보)
     */
    @PostMapping(value = "/sys/usermng/userAuthMngPop.do")
    public ModelAndView userAuthMngPop(@ModelAttribute UserMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/usermng/userAuthMngPop");
        mav.addObject("detailDTO", new UserMngDetailDTO(request.getUserId()));
        return mav;
    }

    /**
     * <p>권한 목록 조회</p>
     *
     * @param request (권한 정보)
     * @return JSONObject (권한 목록)
     */
    @GetMapping(value = "/sys/usermng/userAuthMngSearch.do")
    public @ResponseBody JSONObject userAuthMngSearch(@ModelAttribute UserMngRequest request) {
        JSONObject jsonObject = new JSONObject();
        JSONArray userAuthMngList = userMngService.selectUserAuthMngList(request);
        jsonObject.put("userAuthMngList", userAuthMngList);

        return jsonObject;
    }

    /**
     * <p>권한 설정 적용</p>
     *
     * @param userIdList (사용자 아이디 목록)
     * @param authCdList (권한 목록)
     * @return UserMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/usermng/userAuthMngUpdate.do")
    public @ResponseBody UserMngResponse userAuthMngUpdate(
        @RequestParam("userId") String userIdList, @RequestParam("authCd") String authCdList) {
        return userMngService.updateUserAuthMngData(userIdList, authCdList);
    }

    /**
     * <p>사용자 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/sys/usermng/userMngExcel.do")
    public ExcelUtil userMngExcel(@ModelAttribute UserMngSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<UserMngListDTO> excelList = userMngService.selectUserMngExcelList(searchDTO);

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
        for (UserMngListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String grpCd = listDTO.getGrpCd();
            String grpNm = listDTO.getGrpNm();
            String userId = listDTO.getUserId();
            String userNm = listDTO.getUserNm();
            String mobileNum = listDTO.getMobileNum();
            String email = listDTO.getEmail();
            String userType = listDTO.getUserType();
            String useYn = listDTO.getUseYn();
            String regDate = listDTO.getRegDate();
            String modDate = listDTO.getModDate();
            String[] arr = {rn, grpCd, grpNm, userId, userNm, mobileNum, email, userType, useYn,
                regDate, modDate};
            colValue.add(arr);
        }

        map.put("excelName", "사용자 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
