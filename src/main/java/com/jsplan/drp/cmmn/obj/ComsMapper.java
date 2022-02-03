package com.jsplan.drp.cmmn.obj;

import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @Class : ComsMapper
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 공통 Mapper
 */

@Repository("ComsMapper")
public class ComsMapper extends AbstractDAO {

    String namespace = "Coms.";

    /**
     * <p>메뉴 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<ComsMenuVO> selectComsMenuList() throws Exception {
        return (List<ComsMenuVO>) selectList(namespace + "selectComsMenuList");
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd
     * @return ComsMenuVO
     * @throws Exception throws Exception
     */
    public ComsMenuVO selectComsMenuDetail(String menuCd) throws Exception {
        return (ComsMenuVO) selectOne(namespace + "selectComsMenuDetail", menuCd);
    }

    /**
     * <p>URL별 권한 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<ComsMenuVO> selectComsUrlAuthList() throws Exception {
        return (List<ComsMenuVO>) selectList(namespace + "selectComsUrlAuthList");
    }

    /**
     * <p>계층화 권한 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<ComsMenuVO> selectHierarchicalAuthList() throws Exception {
        return (List<ComsMenuVO>) selectList(namespace + "selectHierarchicalAuthList");
    }

    /**
     * <p>공통 코드 목록</p>
     *
     * @param comCd
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<ComsVO> selectComsCodeList(String comCd) throws Exception {
        return (List<ComsVO>) selectList(namespace + "selectComsCodeList", comCd);
    }

    /**
     * <p>그룹 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<ComsVO> selectComsGrpList() throws Exception {
        return (List<ComsVO>) selectList(namespace + "selectComsGrpList");
    }

    /**
     * <p>담당자 목록</p>
     *
     * @param comsVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<ComsVO> selectComsUserList(ComsVO comsVO) throws Exception {
        return (List<ComsVO>) selectList(namespace + "selectComsUserList", comsVO);
    }

}
