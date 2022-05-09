package com.jsplan.drp.domain.sys.menumng.controller;

import com.jsplan.drp.domain.sys.menumng.dto.MenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequest;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngResponse;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngSearchDTO;
import com.jsplan.drp.domain.sys.menumng.service.MenuMngService;
import com.jsplan.drp.global.bean.ReloadableFilterInvocationSecurityMetadataSource;
import com.jsplan.drp.global.obj.entity.ComsMenuVO;
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
 * @Class : MenuMngController
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 메뉴 관리 Controller
 */
@Controller
@RequiredArgsConstructor
public class MenuMngController {

    private final MenuMngService menuMngService;
    private final ComsService comsService;
    private final ReloadableFilterInvocationSecurityMetadataSource rfisms;

    /**
     * <p>메뉴 관리</p>
     *
     * @param comsMenuVO (메뉴 VO)
     * @return ModelAndView (메뉴 관리 페이지 정보)
     */
    @PostMapping(value = "/sys/menumng/menuMngList.do")
    public ModelAndView menuMngList(@ModelAttribute ComsMenuVO comsMenuVO) {
        ModelAndView mav = new ModelAndView("sys/menumng/menuMngList");

        try {
            // ***************************** MENU : S *****************************
            List<ComsMenuVO> menuList = comsService.selectComsMenuList();
            mav.addObject("menuList", menuList);
            comsMenuVO = comsService.selectComsMenuDetail(comsMenuVO.getMenuCd());
            mav.addObject("comsMenuVO", comsMenuVO);
            // ***************************** MENU : E *****************************

            // 기본 검색 조건 설정
            mav.addObject("searchDTO", new MenuMngSearchDTO("menuCd"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * <p>메뉴 조회</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONObject (메뉴 목록)
     */
    @GetMapping(value = "/sys/menumng/menuMngSearch.do")
    public @ResponseBody JSONObject menuMngSearch(@ModelAttribute MenuMngSearchDTO searchDTO) {
        JSONObject menuMngObject = new JSONObject();
        JSONArray menuMngList = menuMngService.selectMenuMngList(searchDTO);
        menuMngObject.put("menuMngList", menuMngList);

        return menuMngObject;
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param request (메뉴 정보)
     * @return ModelAndView (메뉴 상세 페이지 정보)
     */
    @PostMapping(value = "/sys/menumng/menuMngDetail.do")
    public ModelAndView menuMngDetail(@ModelAttribute MenuMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/menumng/menuMngDetail");
        MenuMngDetailDTO detailDTO = new MenuMngDetailDTO();

        if (DetailStatus.UPDATE.equals(request.getDetailStatus())) {
            detailDTO = menuMngService.selectMenuMngDetail(request);
        }

        detailDTO.setDetailStatus(request.getDetailStatus());
        mav.addObject("detailDTO", detailDTO);
        return mav;
    }

    /**
     * <p>메뉴 등록</p>
     *
     * @param request (메뉴 정보)
     * @return MenuMngResponse (응답 정보)
     */
    @PostMapping(value = "/sys/menumng/menuMngInsert.do")
    public @ResponseBody MenuMngResponse menuMngInsert(
        @ModelAttribute @Valid MenuMngRequest request) {
        MenuMngResponse response = menuMngService.insertMenuMngData(request);
        if (DataStatus.SUCCESS == response.getDataStatus()) {
            reloadUrlAuthMapping();
        }
        return response;
    }

    /**
     * <p>메뉴 수정</p>
     *
     * @param request (메뉴 정보)
     * @return MenuMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/menumng/menuMngUpdate.do")
    public @ResponseBody MenuMngResponse menuMngUpdate(
        @ModelAttribute @Valid MenuMngRequest request) {
        MenuMngResponse response = menuMngService.updateMenuMngData(request);
        if (DataStatus.SUCCESS == response.getDataStatus()) {
            reloadUrlAuthMapping();
        }
        return response;
    }

    /**
     * <p>메뉴 삭제</p>
     *
     * @param request (메뉴 정보)
     * @return MenuMngResponse (응답 정보)
     */
    @DeleteMapping(value = "/sys/menumng/menuMngDelete.do")
    public @ResponseBody MenuMngResponse menuMngDelete(@ModelAttribute MenuMngRequest request) {
        return menuMngService.deleteMenuMngData(request);
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
     * <p>권한 설정 팝업</p>
     *
     * @param request (권한 정보)
     * @return ModelAndView (권한 설정 페이지 정보)
     */
    @PostMapping(value = "/sys/menumng/menuAuthMngPop.do")
    public ModelAndView menuAuthMngPop(@ModelAttribute MenuMngRequest request) {
        ModelAndView mav = new ModelAndView("sys/menumng/menuAuthMngPop");
        mav.addObject("detailDTO", new MenuMngDetailDTO(request.getMenuCd()));
        return mav;
    }

    /**
     * <p>권한 목록 조회</p>
     *
     * @param request (권한 정보)
     * @return JSONObject (권한 목록)
     */
    @GetMapping(value = "/sys/menumng/menuAuthMngSearch.do")
    public @ResponseBody JSONObject menuAuthMngSearch(@ModelAttribute MenuMngRequest request) {
        JSONObject menuAuthMngObject = new JSONObject();
        JSONArray menuAuthMngList = menuMngService.selectMenuAuthMngList(request);
        menuAuthMngObject.put("menuAuthMngList", menuAuthMngList);

        return menuAuthMngObject;
    }

    /**
     * <p>권한 설정 적용</p>
     *
     * @param menuCdList (메뉴 코드 목록)
     * @param authCdList (권한 목록)
     * @return MenuMngResponse (응답 정보)
     */
    @PutMapping(value = "/sys/menumng/menuAuthMngUpdate.do")
    public @ResponseBody MenuMngResponse menuAuthMngUpdate(
        @RequestParam("menuCd") String menuCdList, @RequestParam("authCd") String authCdList) {
        MenuMngResponse response = menuMngService.updateMenuAuthMngData(menuCdList, authCdList);
        reloadUrlAuthMapping();
        return response;
    }

    /**
     * <p>메뉴 엑셀 출력</p>
     *
     * @param searchDTO (조회 조건)
     * @param map       (엑셀 출력 정보)
     * @return ExcelUtil (엑셀 다운로드)
     */
    @PostMapping(value = "/sys/menumng/menuMngExcel.do")
    public ExcelUtil menuMngExcel(@ModelAttribute MenuMngSearchDTO searchDTO, ModelMap map) {
        List<String> colName = new ArrayList<String>();
        List<Integer> colWidth = new ArrayList<Integer>();
        List<String[]> colValue = new ArrayList<String[]>();

        // 데이터 조회
        List<MenuMngListDTO> excelList = menuMngService.selectMenuMngExcelList(searchDTO);

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
        for (MenuMngListDTO listDTO : excelList) {
            String rn = String.valueOf(listDTO.getRn());
            String menuCd = listDTO.getMenuCd();
            String menuNm = listDTO.getMenuNm();
            String menuUrl = listDTO.getMenuUrl();
            String menuDesc = listDTO.getMenuDesc();
            String menuLv = String.valueOf(listDTO.getMenuLv());
            String menuOrd = String.valueOf(listDTO.getMenuOrd());
            String useYn = listDTO.getUseYn();
            String[] arr = {rn, menuCd, menuNm, menuUrl, menuDesc, menuLv, menuOrd, useYn};
            colValue.add(arr);
        }

        map.put("excelName", "메뉴 목록");
        map.put("colName", colName);
        map.put("colWidth", colWidth);
        map.put("colValue", colValue);
        return new ExcelUtil();
    }
}
