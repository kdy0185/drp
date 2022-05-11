package com.jsplan.drp.domain.sys.authmng.repository;

import com.jsplan.drp.domain.sys.authmng.dto.AuthMenuMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngDetailDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthMngListDTO;
import com.jsplan.drp.domain.sys.authmng.dto.AuthUserMngListDTO;
import com.jsplan.drp.global.obj.vo.DataStatus.UseStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Class : AuthMngCustomRepository
 * @Author : KDW
 * @Date : 2022-04-27
 * @Description : 권한 관리 Custom Repository
 */
public interface AuthMngCustomRepository {

    /**
     * <p>권한 목록</p>
     *
     * @param authCd     (권한 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (권한 목록)
     */
    List<AuthMngListDTO> searchAuthMngList(String authCd, String searchCd, String searchWord,
        UseStatus useYn);

    /**
     * <p>권한 상세</p>
     *
     * @param authCd (권한 코드)
     * @return AuthMngDetailDTO (권한 DTO)
     */
    AuthMngDetailDTO findAuthMngByAuthCd(String authCd);

    /**
     * <p>권한별 사용자 확인</p>
     *
     * @param authCd (권한 코드)
     * @return boolean (사용자 존재 여부)
     */
    boolean existsAuthUserMngByAuthCd(String authCd);

    /**
     * <p>권한별 메뉴 확인</p>
     *
     * @param authCd (권한 코드)
     * @return boolean (메뉴 존재 여부)
     */
    boolean existsAuthMenuMngByAuthCd(String authCd);

    /**
     * <p>권한별 사용자 목록</p>
     *
     * @param authCdList (권한 코드 목록)
     * @param grpCd      (그룹 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param pageable   (페이징 정보)
     * @return Page (사용자 목록)
     */
    Page<AuthUserMngListDTO> searchAuthUserMngList(List<String> authCdList, String grpCd,
        String searchCd, String searchWord, Pageable pageable);

    /**
     * <p>권한별 메뉴 목록</p>
     *
     * @param authCdList (권한 코드 목록)
     * @param menuCd     (메뉴 코드)
     * @return List (메뉴 목록)
     */
    List<AuthMenuMngListDTO> searchAuthMenuMngList(List<String> authCdList, String menuCd);

    /**
     * <p>권한 엑셀 목록</p>
     *
     * @param authCd     (권한 코드)
     * @param searchCd   (검색 조건)
     * @param searchWord (검색어)
     * @param useYn      (사용 여부)
     * @return List (권한 목록)
     */
    List<AuthMngListDTO> searchAuthMngExcelList(String authCd, String searchCd, String searchWord,
        UseStatus useYn);
}
