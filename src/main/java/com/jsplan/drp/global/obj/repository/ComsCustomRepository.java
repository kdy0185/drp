package com.jsplan.drp.global.obj.repository;

import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
import java.util.List;

/**
 * @Class : ComsCustomRepository
 * @Author : KDW
 * @Date : 2022-05-20
 * @Description : 공통 Custom Repository
 */
public interface ComsCustomRepository {

    /**
     * <p>메뉴 목록</p>
     *
     * @return List (메뉴 목록)
     */
    List<ComsMenuDTO> selectComsMenuList();

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd (메뉴 코드)
     * @return ComsMenuDTO (메뉴 정보)
     */
    ComsMenuDTO selectComsMenuDetail(String menuCd);

    /**
     * <p>URL별 권한 목록</p>
     *
     * @return List (URL별 권한 목록)
     */
    List<ComsMenuDTO> selectComsUrlAuthList();

    /**
     * <p>계층화 권한 목록</p>
     *
     * @return List (계층화 권한 목록)
     */
    List<ComsMenuDTO> selectHierarchicalAuthList();

    /**
     * <p>공통 코드 목록</p>
     *
     * @param grpCd (그룹 코드)
     * @return List (공통 코드 목록)
     */
    List<ComsDTO> selectComsCodeList(String grpCd);

    /**
     * <p>그룹 목록</p>
     *
     * @return List (그룹 목록)
     */
    List<ComsDTO> selectComsGrpList();

    /**
     * <p>담당자 목록</p>
     *
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @return List (담당자 목록)
     */
    List<ComsDTO> selectComsUserList(String grpCd, String searchCd, String searchWord);
}
