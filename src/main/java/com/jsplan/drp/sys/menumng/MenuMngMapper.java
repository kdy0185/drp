package com.jsplan.drp.sys.menumng;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : MenuMngMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 메뉴 관리 Mapper
 */
@Repository
@Mapper
public interface MenuMngMapper {

    /**
     * <p>메뉴 목록</p>
     *
     * @param menuMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<MenuMngVO> selectMenuMngList(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuMngVO
     * @return MenuMngVO
     * @throws Exception throws Exception
     */
    MenuMngVO selectMenuMngDetail(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴별 권한 목록</p>
     *
     * @param menuMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<MenuMngVO> selectMenuAuthMngList(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴 등록</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertMenuMngData(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴 수정</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int updateMenuMngData(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴 삭제</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteMenuMngData(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴별 권한 등록</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertMenuAuthMngData(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴별 권한 삭제</p>
     *
     * @param menuMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteMenuAuthMngData(MenuMngVO menuMngVO) throws Exception;

    /**
     * <p>메뉴 엑셀 목록</p>
     *
     * @param menuMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<MenuMngVO> selectMenuMngExcelList(MenuMngVO menuMngVO) throws Exception;
}
