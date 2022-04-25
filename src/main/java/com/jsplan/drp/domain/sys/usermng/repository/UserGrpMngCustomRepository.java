package com.jsplan.drp.domain.sys.usermng.repository;

import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngDetailDTO;
import com.jsplan.drp.domain.sys.usermng.dto.UserGrpMngListDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : UserGrpMngCustomRepository
 * @Author : KDW
 * @Date : 2022-03-07
 * @Description : 그룹 관리 Custom Repository
 */
public interface UserGrpMngCustomRepository {

    /**
     * <p>그룹 목록</p>
     *
     * @param grpNm    (그룹명)
     * @param pageable (페이징 정보)
     * @return Page (페이징 목록)
     */
    Page<UserGrpMngListDTO> searchPageList(String grpNm, Pageable pageable);

    /**
     * <p>그룹 상세</p>
     *
     * @param grpCd (그룹 코드)
     * @return UserGrpMngDetailDTO (그룹 DTO)
     */
    UserGrpMngDetailDTO findByGrpCd(String grpCd);

    /**
     * <p>사용자 확인</p>
     *
     * @param grpCd (그룹 코드)
     * @return boolean (사용자 존재 여부)
     */
    boolean existsUserMngByGrpCd(String grpCd);

    /**
     * <p>그룹 엑셀 목록</p>
     *
     * @param grpNm    (그룹명)
     * @return List (그룹 목록)
     */
    List<UserGrpMngListDTO> searchExcelList(String grpNm);
}
