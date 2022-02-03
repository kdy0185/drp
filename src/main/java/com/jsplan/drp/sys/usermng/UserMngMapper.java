package com.jsplan.drp.sys.usermng;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jsplan.drp.cmmn.obj.AbstractDAO;

/**
 * @Class : UserMngMapper
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 Mapper
 */
@Repository("UserMngMapper")
public class UserMngMapper extends AbstractDAO {

    String namespace = "UserMng.";

    /**
     * <p>그룹 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<UserMngVO> selectGrpMngList(UserMngVO userMngVO) throws Exception {
        return (List<UserMngVO>) selectList(namespace + "selectGrpMngList", userMngVO);
    }

    /**
     * <p>그룹 목록 수</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectGrpMngListCnt(UserMngVO userMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectGrpMngListCnt", userMngVO);
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param userMngVO
     * @return UserMngVO
     * @throws Exception throws Exception
     */
    public UserMngVO selectGrpMngDetail(UserMngVO userMngVO) throws Exception {
        return (UserMngVO) selectOne(namespace + "selectGrpMngDetail", userMngVO);
    }

    /**
     * <p>그룹 등록</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertGrpMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) update(namespace + "insertGrpMngData", userMngVO);
    }

    /**
     * <p>그룹 수정</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updateGrpMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) update(namespace + "updateGrpMngData", userMngVO);
    }

    /**
     * <p>그룹 삭제</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteGrpMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteGrpMngData", userMngVO);
    }

    /**
     * <p>그룹 엑셀 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<UserMngVO> selectGrpMngExcelList(UserMngVO userMngVO) throws Exception {
        return (List<UserMngVO>) selectList(namespace + "selectGrpMngExcelList", userMngVO);
    }

    /**
     * <p>사용자 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<UserMngVO> selectUserMngList(UserMngVO userMngVO) throws Exception {
        return (List<UserMngVO>) selectList(namespace + "selectUserMngList", userMngVO);
    }

    /**
     * <p>사용자 목록 수</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectUserMngListCnt(UserMngVO userMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectUserMngListCnt", userMngVO);
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param userMngVO
     * @return UserMngVO
     * @throws Exception throws Exception
     */
    public UserMngVO selectUserMngDetail(UserMngVO userMngVO) throws Exception {
        return (UserMngVO) selectOne(namespace + "selectUserMngDetail", userMngVO);
    }

    /**
     * <p>사용자 권한 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<UserMngVO> selectUserAuthMngList(UserMngVO userMngVO) throws Exception {
        return (List<UserMngVO>) selectList(namespace + "selectUserAuthMngList", userMngVO);
    }

    /**
     * <p>사용자 아이디 중복 체크</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectUserMngDupCnt(UserMngVO userMngVO) throws Exception {
        return (Integer) selectOne(namespace + "selectUserMngDupCnt", userMngVO);
    }

    /**
     * <p>사용자 등록</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertUserMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) update(namespace + "insertUserMngData", userMngVO);
    }

    /**
     * <p>사용자 권한 등록</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int insertUserAuthMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) update(namespace + "insertUserAuthMngData", userMngVO);
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int updateUserMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) update(namespace + "updateUserMngData", userMngVO);
    }

    /**
     * <p>사용자 삭제</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteUserMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteUserMngData", userMngVO);
    }

    /**
     * <p>사용자 권한 삭제</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int deleteUserAuthMngData(UserMngVO userMngVO) throws Exception {
        return (Integer) delete(namespace + "deleteUserAuthMngData", userMngVO);
    }

    /**
     * <p>사용자 엑셀 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<UserMngVO> selectUserMngExcelList(UserMngVO userMngVO) throws Exception {
        return (List<UserMngVO>) selectList(namespace + "selectUserMngExcelList", userMngVO);
    }
}
