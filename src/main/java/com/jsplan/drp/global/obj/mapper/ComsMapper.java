package com.jsplan.drp.global.obj.mapper;

import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
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
    List<ComsMenuDTO> selectComsMenuList() throws Exception;

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd
     * @return ComsMenuDTO
     * @throws Exception throws Exception
     */
    ComsMenuDTO selectComsMenuDetail(String menuCd) throws Exception;

    /**
     * <p>URL별 권한 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsMenuDTO> selectComsUrlAuthList() throws Exception;

    /**
     * <p>계층화 권한 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsMenuDTO> selectHierarchicalAuthList() throws Exception;

    /**
     * <p>공통 코드 목록</p>
     *
     * @param comCd
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsDTO> selectComsCodeList(String comCd) throws Exception;

    /**
     * <p>그룹 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsDTO> selectComsGrpList() throws Exception;

    /**
     * <p>담당자 목록</p>
     *
     * @param comsDTO
     * @return List
     * @throws Exception throws Exception
     */
    List<ComsDTO> selectComsUserList(ComsDTO comsDTO) throws Exception;
}
