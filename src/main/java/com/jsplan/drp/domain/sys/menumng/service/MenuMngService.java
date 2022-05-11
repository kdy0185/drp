package com.jsplan.drp.domain.sys.menumng.service;

import com.jsplan.drp.domain.sys.menumng.dto.MenuAuthMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngRequest;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngResponse;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngSearchDTO;
import com.jsplan.drp.domain.sys.menumng.entity.MenuMng;
import com.jsplan.drp.domain.sys.menumng.repository.MenuMngRepository;
import com.jsplan.drp.global.obj.vo.DataStatus;
import com.jsplan.drp.global.util.StringUtil;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : MenuMngService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 메뉴 관리 Service
 */
@Service
@RequiredArgsConstructor
public class MenuMngService {

    private final MenuMngRepository menuMngRepository;

    /**
     * <p>메뉴 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return JSONArray (메뉴 목록)
     */
    public JSONArray selectMenuMngList(MenuMngSearchDTO searchDTO) {
        JSONArray menuMngArray = new JSONArray();
        JSONObject menuMngObject;

        // 하위 메뉴 조회
        List<MenuMngListDTO> menuMngList = menuMngRepository.searchMenuMngList(
            searchDTO.getMenuCd(), searchDTO.getSearchCd(), searchDTO.getSearchWord(),
            searchDTO.getUseYn());
        for (MenuMngListDTO listDTO : menuMngList) {
            menuMngObject = new JSONObject();
            menuMngObject.put("menuNm", listDTO.getMenuNm());
            menuMngObject.put("menuCd", listDTO.getMenuCd());
            menuMngObject.put("menuUrl", listDTO.getMenuUrl());
            menuMngObject.put("menuLv", listDTO.getMenuLv());
            menuMngObject.put("menuOrd", listDTO.getMenuOrd());
            menuMngObject.put("useYn", listDTO.getUseYn());
            menuMngObject.put("leaf", "Y".equals(listDTO.getLastYn()));
            menuMngObject.put("expanded", !"Y".equals(listDTO.getLastYn()));

            menuMngArray.add(menuMngObject);
        }

        return menuMngArray;
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param request (메뉴 정보)
     * @return MenuMngDetailDTO (메뉴 DTO)
     */
    public MenuMngDetailDTO selectMenuMngDetail(MenuMngRequest request) {
        return menuMngRepository.findMenuMngByMenuCd(request.getMenuCd());
    }

    /**
     * <p>메뉴별 권한 목록</p>
     *
     * @param request (권한 정보)
     * @return JSONArray (권한 목록)
     */
    public JSONArray selectMenuAuthMngList(MenuMngRequest request) {
        JSONArray authArray = new JSONArray();
        JSONObject authObject;
        List<String> menuCdList = List.of(request.getMenuCd().split(","));

        // 하위 권한 조회
        List<MenuAuthMngListDTO> menuAuthMngList = menuMngRepository.searchMenuAuthMngList(
            menuCdList, request.getAuthCd());
        for (MenuAuthMngListDTO listDTO : menuAuthMngList) {
            authObject = new JSONObject();
            authObject.put("id", listDTO.getAuthCd());
            authObject.put("text", listDTO.getAuthNm());
            authObject.put("leaf", "Y".equals(listDTO.getLastYn()));
            authObject.put("expanded", !"Y".equals(listDTO.getLastYn()));
            authObject.put("checked", "Y".equals(listDTO.getAuthYn()));
            authArray.add(authObject);
        }

        return authArray;
    }

    /**
     * <p>메뉴 등록</p>
     *
     * @param request (메뉴 정보)
     * @return MenuMngResponse (응답 정보)
     */
    @Transactional
    public MenuMngResponse insertMenuMngData(MenuMngRequest request) {
        if (validateMenuMngDupData(request)) {
            return new MenuMngResponse(null, DataStatus.DUPLICATE);
        } else {
            MenuMng menuMng = menuMngRepository.save(request.toEntity());
            return new MenuMngResponse(menuMng.getMenuCd(), DataStatus.SUCCESS);
        }
    }

    /**
     * <p>중복 메뉴 체크</p>
     *
     * @param request (메뉴 정보)
     * @return boolean (중복 여부)
     */
    private boolean validateMenuMngDupData(MenuMngRequest request) {
        Optional<MenuMng> optionalMenuMng = menuMngRepository.findById(request.getMenuCd());
        return optionalMenuMng.isPresent();
    }

    /**
     * <p>메뉴 수정</p>
     *
     * @param request (메뉴 정보)
     * @return MenuMngResponse (응답 정보)
     */
    @Transactional
    public MenuMngResponse updateMenuMngData(MenuMngRequest request) {
        MenuMng menuMng = menuMngRepository.findById(request.getMenuCd())
            .orElseThrow(NoSuchElementException::new);
        menuMng.updateMenuMng(request);
        return new MenuMngResponse(menuMng.getMenuCd(), DataStatus.SUCCESS);
    }

    /**
     * <p>메뉴 삭제</p>
     *
     * @param request (메뉴 정보)
     * @return MenuMngResponse (응답 정보)
     */
    @Transactional
    public MenuMngResponse deleteMenuMngData(MenuMngRequest request) {
        menuMngRepository.deleteById(request.getMenuCd());
        return new MenuMngResponse(request.getMenuCd(), DataStatus.SUCCESS);
    }

    /**
     * <p>권한 설정 적용</p>
     *
     * @param menuCdList (메뉴 코드 목록)
     * @param authCdList (권한 목록)
     * @return MenuMngResponse (응답 정보)
     */
    @Transactional
    public MenuMngResponse updateMenuAuthMngData(String menuCdList, String authCdList) {
        for (String menuCd : StringUtil.split(menuCdList)) {
            MenuMng menuMng = menuMngRepository.findById(menuCd)
                .orElseThrow(NoSuchElementException::new);
            menuMng.updateMenuAuthMng(authCdList);
        }
        return new MenuMngResponse(menuCdList, DataStatus.SUCCESS);
    }

    /**
     * <p>메뉴 엑셀 목록</p>
     *
     * @param searchDTO (조회 조건)
     * @return List (메뉴 목록)
     */
    public List<MenuMngListDTO> selectMenuMngExcelList(MenuMngSearchDTO searchDTO) {
        return menuMngRepository.searchMenuMngExcelList(searchDTO.getMenuCd(),
            searchDTO.getSearchCd(), searchDTO.getSearchWord(), searchDTO.getUseYn());
    }
}
