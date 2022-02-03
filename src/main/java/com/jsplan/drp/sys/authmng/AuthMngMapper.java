package com.jsplan.drp.sys.authmng;

import com.jsplan.drp.cmmn.obj.AbstractDAO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @Class : AuthMngMapper
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 권한 관리 Mapper
 */
@Repository("AuthMngMapper")
public class AuthMngMapper extends AbstractDAO {

    String namespace = "AuthMng.";

    /**
     * <p>권한 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AuthMngVO> selectAuthMngList(AuthMngVO authMngVO) throws Exception {
        return (List<AuthMngVO>) selectList(namespace + "selectAuthMngList", authMngVO);
    }

    /**
     * <p>상위 권한 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AuthMngVO> selectUpperAuthMngList(AuthMngVO authMngVO) throws Exception {
        return (List<AuthMngVO>) selectList(namespace + "selectUpperAuthMngList", authMngVO);
    }

    /**
     * <p>권한 상세</p>
     *
     * @param authMngVO
     * @return AuthMngVO
     * @throws Exception throws Exception
     */
    public AuthMngVO selectAuthMngDetail(AuthMngVO authMngVO) throws Exception {
        return (AuthMngVO) selectOne(namespace + "selectAuthMngDetail", authMngVO);
    }

    /**
     * <p>권한 등록</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertAuthMngData(AuthMngVO authMngVO) throws Exception {
        return (Integer) update(namespace + "insertAuthMngData", authMngVO);
    }

    /**
     * <p>권한 수정</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updateAuthMngData(AuthMngVO authMngVO) throws Exception {
        return (Integer) update(namespace + "updateAuthMngData", authMngVO);
    }

    /**
     * <p>권한별 사용자 목록 수</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectAuthUserListCnt(AuthMngVO authMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectAuthUserListCnt", authMngVO);
    }

    /**
     * <p>권한별 메뉴 목록 수</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectAuthMenuListCnt(AuthMngVO authMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectAuthMenuListCnt", authMngVO);
    }

    /**
     * <p>권한 삭제</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteAuthMngData(AuthMngVO authMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteAuthMngData", authMngVO);
    }

    /**
     * <p>권한별 사용자 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AuthMngVO> selectAuthUserMngList(AuthMngVO authMngVO) throws Exception {
        return (List<AuthMngVO>) selectList(namespace + "selectAuthUserMngList", authMngVO);
    }

    /**
     * <p>권한별 사용자 목록 수</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectAuthUserMngListCnt(AuthMngVO authMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectAuthUserMngListCnt", authMngVO);
    }

    /**
     * <p>권한별 사용자 등록</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertAuthUserMngData(AuthMngVO authMngVO) throws Exception {
        return (Integer) update(namespace + "insertAuthUserMngData", authMngVO);
    }

    /**
     * <p>권한별 사용자 삭제</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteAuthUserMngData(AuthMngVO authMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteAuthUserMngData", authMngVO);
    }

    /**
     * <p>권한별 메뉴 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AuthMngVO> selectAuthMenuMngList(AuthMngVO authMngVO) throws Exception {
        return (List<AuthMngVO>) selectList(namespace + "selectAuthMenuMngList", authMngVO);
    }

    /**
     * <p>권한별 메뉴 등록</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertAuthMenuMngData(AuthMngVO authMngVO) throws Exception {
        return (Integer) update(namespace + "insertAuthMenuMngData", authMngVO);
    }

    /**
     * <p>권한별 메뉴 삭제</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteAuthMenuMngData(AuthMngVO authMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteAuthMenuMngData", authMngVO);
    }

    /**
     * <p>권한 엑셀 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<AuthMngVO> selectAuthMngExcelList(AuthMngVO authMngVO) throws Exception {
        return (List<AuthMngVO>) selectList(namespace + "selectAuthMngExcelList", authMngVO);
    }
}
