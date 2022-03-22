package com.jsplan.drp.domain.sys.usermng.repository;

import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngDetailDto;
import com.jsplan.drp.domain.sys.usermng.entity.UserGrpMngDto.UserGrpMngListDto;
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
    Page<UserGrpMngListDto> searchPageList(String grpNm, Pageable pageable);

    /**
     * <p>그룹 상세</p>
     *
     * @param grpCd (그룹 코드)
     * @return UserGrpMngDto (그룹 DTO)
     */
    UserGrpMngDetailDto findByGrpCd(String grpCd);
}
