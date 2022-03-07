package com.jsplan.drp.domain.sys.authmng.mapper;

import com.jsplan.drp.domain.sys.authmng.entity.AuthMngVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : AuthMngMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 권한 관리 Mapper
 */
@Repository
@Mapper
public interface AuthMngMapper {

    /**
     * <p>권한 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<AuthMngVO> selectAuthMngList(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>상위 권한 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<AuthMngVO> selectUpperAuthMngList(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한 상세</p>
     *
     * @param authMngVO
     * @return AuthMngVO
     * @throws Exception throws Exception
     */
    AuthMngVO selectAuthMngDetail(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한 등록</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertAuthMngData(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한 수정</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int updateAuthMngData(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 사용자 목록 수</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectAuthUserListCnt(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 메뉴 목록 수</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectAuthMenuListCnt(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한 삭제</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteAuthMngData(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 사용자 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<AuthMngVO> selectAuthUserMngList(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 사용자 목록 수</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectAuthUserMngListCnt(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 사용자 등록</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertAuthUserMngData(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 사용자 삭제</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteAuthUserMngData(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 메뉴 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<AuthMngVO> selectAuthMenuMngList(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 메뉴 등록</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertAuthMenuMngData(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한별 메뉴 삭제</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteAuthMenuMngData(AuthMngVO authMngVO) throws Exception;

    /**
     * <p>권한 엑셀 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<AuthMngVO> selectAuthMngExcelList(AuthMngVO authMngVO) throws Exception;
}
