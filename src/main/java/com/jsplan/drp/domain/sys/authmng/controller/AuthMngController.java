package com.jsplan.drp.domain.sys.authmng.controller;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngRequest;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngResponse;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngSearchDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthUserMngListDTO;
import com.jsplan.drp.domain.sys.authmng.service.AuthMngService;
import com.jsplan.drp.global.bean.AvailableRoleHierarchy;
import com.jsplan.drp.global.bean.ReloadableFilterInvocationSecurityMetadataSource;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.entity.DataStatus;
import com.jsplan.drp.global.obj.entity.DetailStatus;
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
 * @Class : AuthMngController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 권한 관리 Controller
 */
@Controller
@RequiredArgsConstructor
public class AuthMngController {

    private final AuthMngService authMngService;
    private final ComsService comsService;
    private final AvailableRoleHierarchy arh;
    private final ReloadableFilterInvocationSecurityMetadataSource rfisms;

    /**
     * <p>권한 관리</p>
     *
     * @param comsMenuVO (메뉴 VO)
     * @return ModelAndView (권한 관리 페이지 정보)
     */
    @PostMapping(value = "/sys/authmng/authMngList.do")
    public ModelAndView authMngList(@ModelAttribute ComsMenuVO comsMenuVO) {
        ModelAndView mav = new ModelAndView("sys/authmng/authMngList");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuVO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuVO = comsService.selectComsMenuDetail(comsMenuVO.getMenuCd());
            mav.addObject("comsMenuVO", comsMenuVO);
            // ***************************** MENU : E *****************************

            // 기본 검색 조건 설정
            mav.addObject("searchDTO", new AuthMngSearchDTO("authCd"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>권한 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONObject (권한 목록)
     */
    @GetMapping(value = "/sys/authmng/authMngSearch.do")
    public @ResponseBody JSONObject authMngSearch(@ModelAttribute AuthMngSearchDTO searchDTO) {
        JSONObject authMngObject = new JSONObject();
        JSONArray authMngList = authMngService.selectAuthMngList(searchDTO);
        authMngObject.put("authMngList", authMngList);

        return authMngObject;
    }

    /**
     * <p>권한 상세</p>
     *
     * @param request (권한 정보)
     * @return ModelAndView (권한 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/authmng/authMngDetail.do")
    public ModelAndView authMngDetail(@ModelAttribute AuthMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/authmng/authMngDetail");
        AuthMngDetailDTO detailDTO = new AuthMngDetailDTO();

        // 상위 권한 목록
        List<AuthMngListDTO> authList = authMngService.selectUpperAuthMngList();
        mav.addObject("authList", authList);

        if (DetailStatus.UPDATE.equals(request.getDetailStatus())) {
            detailDTO = authMngService.selectAuthMngDetail(request);
        }

        detailDTO.setDetailStatus(request.getDetailStatus());
        mav.addObject("detailDTO", detailDTO);
        return mav;
    }

    /**
     * <p>권한 등록</p>
     *
     * @param request (권한 정보)
     * @return AuthMngResponse (응답 정보)
     */
    @PostMapping(value = "/sys/authmng/authMngInsert.do")
    public @ResponseBody AuthMngResponse authMngInsert(
        @ModelAttribute @Valid AuthMngRequest request) {
        AuthMngResponse response = authMngService.insertAuthMngData(request);
        if (DataStatus.SUCCESS == response.getDataStatus()) {
            reloadRoleHierarchy();
        }
        return response;
    }

    /**
     * <p>권한 수정</p>
     *
     * @param request (권한 정보)
     * @return AuthMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/authmng/authMngUpdate.do")
    public @ResponseBody AuthMngResponse authMngUpdate(
        @ModelAttribute @Valid AuthMngRequest request) {
        AuthMngResponse response = authMngService.updateAuthMngData(request);
        if (DataStatus.SUCCESS == response.getDataStatus()) {
            reloadRoleHierarchy();
        }
        return response;
    }

    /**
     * <p>권한 삭제</p>
     *
     * @param request (권한 정보)
     * @return AuthMngResponse (응답 정보)
     */
    @DeleteMapping(value = "/sys/authmng/authMngDelete.do")
    public @ResponseBody AuthMngResponse authMngDelete(@ModelAttribute AuthMngRequest request) {
        return authMngService.deleteAuthMngData(request);
    }

    /**
     * <p>권한 계층화 정보 Reload</p>
     */
    private void reloadRoleHierarchy() {
        try {
            arh.setComsService(comsService);
            arh.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>사용자 설정 팝업</p>
     *
     * @param searchDTO (조회 조건)
     * @return ModelAndView (사용자 설정 페이지 정보)
     */
    @PostMapping(value = "/sys/authmng/authUserMngPop.do")
    public ModelAndView authUserMngPop(@ModelAttribute AuthMngSearchDTO searchDTO) {
        ModelAndView mav = new ModelAndView("sys/authmng/authUserMngPop");

        try {
            // ***************************** PAGE : S *****************************
            List<ComsVO> pageList = comsService.selectComsCodeList("PAGE_SIZE");
            mav.addObject("pageList", pageList);
            // ***************************** PAGE : E *****************************

            // 기본 검색 조건 설정
            searchDTO.setSearchCd("userNm");
            mav.addObject("searchDTO", searchDTO);

            // 공통 코드 : 그룹
            List<ComsVO> grpList = comsService.selectComsGrpList();
            mav.addObject("grpList", grpList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>사용자 목록 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return Page (사용자 목록)
     */
    @GetMapping(value = "/sys/authmng/authUserMngSearch.do")
    public @ResponseBody Page<AuthUserMngListDTO> authUserMngSearch(
        @ModelAttribute AuthMngSearchDTO searchDTO) {
        return authMngService.selectAuthUserMngList(searchDTO);
    }

    /**
     * <p>사용자 설정 적용</p>
     *
     * @param authCdList (권한 목록)
     * @param userIdList (사용자 아이디 목록)
     * @param authYn     (권한 허용 여부)
     * @return AuthMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/authmng/authUserMngUpdate.do")
    public @ResponseBody AuthMngResponse authUserMngUpdate(
        @RequestParam("authCd") String authCdList, @RequestParam("userId") String userIdList,
        @RequestParam String authYn) {
        return authMngService.updateAuthUserMngData(authCdList, userIdList, authYn);
    }

    /**
     * <p>메뉴 설정 팝업</p>
     *
     * @param searchDTO (조회 조건)
     * @return ModelAndView (메뉴 설정 페이지 정보)
     */
    @PostMapping(value = "/sys/authmng/authMenuMngPop.do")
    public ModelAndView authMenuMngPop(@ModelAttribute AuthMngSearchDTO searchDTO) {
        ModelAndView mav = new ModelAndView("sys/authmng/authMenuMngPop");
        mav.addObject("detailDTO", new AuthMngDetailDTO(searchDTO.getAuthCd()));
        return mav;
    }

    /**
     * <p>메뉴 목록 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONObject (메뉴 목록)
     */
    @GetMapping(value = "/sys/authmng/authMenuMngSearch.do")
    public @ResponseBody JSONObject authMenuMngSearch(@ModelAttribute AuthMngSearchDTO searchDTO) {
        JSONObject authMenuMngObject = new JSONObject();
        JSONArray authMenuMngList = authMngService.selectAuthMenuMngList(searchDTO);
        authMenuMngObject.put("authMenuMngList", authMenuMngList);

        return authMenuMngObject;
    }

    /**
     * <p>메뉴 설정 적용</p>
     *
     * @param authCdList (권한 목록)
     * @param menuCdList (메뉴 코드 목록)
     * @return AuthMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/authmng/authMenuMngUpdate.do")
    public @ResponseBody AuthMngResponse authMenuMngUpdate(
        @RequestParam("authCd") String authCdList, @RequestParam("menuCd") String menuCdList) {
        AuthMngResponse response = authMngService.updateAuthMenuMngData(authCdList, menuCdList);
        if (DataStatus.SUCCESS == response.getDataStatus()) {
            reloadUrlAuthMapping();
        }
        return response;
    }

    /**
     * <p>URL별 권한 정보 Reload</p>
     */
    private void reloadUrlAuthMapping() {
        try {
            rfisms.setComsService(comsService);
            rfisms.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>권한 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/sys/authmng/authMngExcel.do")
    public ExcelUtil authMngExcel(@ModelAttribute AuthMngSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<AuthMngListDTO> excelList = authMngService.selectAuthMngExcelList(searchDTO);

        // 컬럼명 설정
        colName.add("순번");
        colName.add("권한 코드");
        colName.add("권한명");
        colName.add("권한 설명");
        colName.add("권한 수준");
        colName.add("권한 순서");
        colName.add("사용 여부");

        // 컬럼 사이즈 설정
        colWidth.add(2000);
        colWidth.add(4000);
        colWidth.add(6000);
        colWidth.add(6000);
        colWidth.add(4000);
        colWidth.add(4000);
        colWidth.add(4000);

        // 데이터 설정
        for (AuthMngListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String authCd = listDTO.getAuthCd();
            String authNm = listDTO.getAuthNm();
            String authDesc = listDTO.getAuthDesc();
            String authLv = String.valueOf(listDTO.getAuthLv());
            String authOrd = String.valueOf(listDTO.getAuthOrd());
            String useYn = listDTO.getUseYn();
            String[] arr = {rn, authCd, authNm, authDesc, authLv, authOrd, useYn};
            colValue.add(arr);
        }

        map.put("excelName", "권한 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
