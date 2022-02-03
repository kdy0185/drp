package com.jsplan.drp.sys.menumng;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jsplan.drp.cmmn.obj.AbstractDAO;

/**
 * @Class : MenuMngMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 메뉴 관리 Mapper
 */
@Repository("MenuMngMapper")
public class MenuMngMapper extends AbstractDAO {

    String namespace = "MenuMng.";

    /**
     * <p>메뉴 목록</p>
     *
     * @param menuMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<MenuMngVO> selectMenuMngList(MenuMngVO menuMngVO) throws Exception {
        return (List<MenuMngVO>) selectList(namespace + "selectMenuMngList", menuMngVO);
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuMngVO
     * @return MenuMngVO
     * @throws Exception throws Exception
     */
    public MenuMngVO selectMenuMngDetail(MenuMngVO menuMngVO) throws Exception {
        return (MenuMngVO) selectOne(namespace + "selectMenuMngDetail", menuMngVO);
    }

    /**
     * <p>메뉴별 권한 목록</p>
     *
     * @param menuMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<MenuMngVO> selectMenuAuthMngList(MenuMngVO menuMngVO) throws Exception {
        return (List<MenuMngVO>) selectList(namespace + "selectMenuAuthMngList", menuMngVO);
    }

    /**
     * <p>메뉴 등록</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertMenuMngData(MenuMngVO menuMngVO) throws Exception {
        return (Integer) update(namespace + "insertMenuMngData", menuMngVO);
    }

    /**
     * <p>메뉴 수정</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updateMenuMngData(MenuMngVO menuMngVO) throws Exception {
        return (Integer) update(namespace + "updateMenuMngData", menuMngVO);
    }

    /**
     * <p>메뉴 삭제</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteMenuMngData(MenuMngVO menuMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteMenuMngData", menuMngVO);
    }

    /**
     * <p>메뉴별 권한 등록</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertMenuAuthMngData(MenuMngVO menuMngVO) throws Exception {
        return (Integer) update(namespace + "insertMenuAuthMngData", menuMngVO);
    }

    /**
     * <p>메뉴별 권한 삭제</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteMenuAuthMngData(MenuMngVO menuMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteMenuAuthMngData", menuMngVO);
    }

    /**
     * <p>메뉴 엑셀 목록</p>
     *
     * @param menuMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<MenuMngVO> selectMenuMngExcelList(MenuMngVO menuMngVO) throws Exception {
        return (List<MenuMngVO>) selectList(namespace + "selectMenuMngExcelList", menuMngVO);
    }
}
