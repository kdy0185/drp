package com.jsplan.drp.domain.sys.menumng.repository;

import com.jsplan.drp.domain.sys.menumng.dto.MenuMngDetailDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuMngListDTO;
import com.jsplan.drp.domain.sys.menumng.dto.MenuAuthMngListDTO;
import com.jsplan.drp.global.obj.entity.UseStatus;
import java.util.List;

/**
 * @Class : MenuMngCustomRepository
 * @Author : KDW
 * @Date : 2022-04-25
 * @Description : 메뉴 관리 Custom Repository
 */
public interface MenuMngCustomRepository {

    /**
     * <p>메뉴 목록</p>
     *
     * @param menuCd     (메뉴 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (메뉴 목록)
     */
    List<MenuMngListDTO> searchMenuMngList(String menuCd, String searchCd, String searchWord,
        UseStatus useYn);

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd (메뉴 코드)
     * @return MenuMngDetailDTO (메뉴 DTO)
     */
    MenuMngDetailDTO findMenuMngByMenuCd(String menuCd);

    /**
     * <p>메뉴 권한 목록</p>
     *
     * @param menuCdList (메뉴 코드 목록)
     * @param authCd     (권한 코드)
     * @return List (권한 목록)
     */
    List<MenuAuthMngListDTO> searchMenuAuthMngList(List<String> menuCdList, String authCd);

    /**
     * <p>메뉴 엑셀 목록</p>
     *
     * @param menuCd     (메뉴 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (메뉴 목록)
     */
    List<MenuMngListDTO> searchMenuMngExcelList(String menuCd, String searchCd, String searchWord,
        UseStatus useYn);
}
