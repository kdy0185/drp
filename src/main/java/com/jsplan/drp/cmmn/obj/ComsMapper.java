package com.jsplan.drp.cmmn.obj;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : ComsMapper
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 공통 Mapper
 */

@Repository
@Mapper
public interface ComsMapper {

    /**
     * <p>메뉴 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsMenuVO> selectComsMenuList() throws Exception;

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd
     * @return ComsMenuVO
     * @throws Exception throws Exception
     */
    ComsMenuVO selectComsMenuDetail(String menuCd) throws Exception;

    /**
     * <p>URL별 권한 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsMenuVO> selectComsUrlAuthList() throws Exception;

    /**
     * <p>계층화 권한 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsMenuVO> selectHierarchicalAuthList() throws Exception;

    /**
     * <p>공통 코드 목록</p>
     *
     * @param comCd
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsVO> selectComsCodeList(String comCd) throws Exception;

    /**
     * <p>그룹 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsVO> selectComsGrpList() throws Exception;

    /**
     * <p>담당자 목록</p>
     *
     * @param comsVO
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsVO> selectComsUserList(ComsVO comsVO) throws Exception;

}
