package com.jsplan.drp.sys.usermng;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Class : UserMngMapper
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 Mapper
 */
@Repository
@Mapper
public interface UserMngMapper {

    /**
     * <p>그룹 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<UserMngVO> selectGrpMngList(UserMngVO userMngVO) throws Exception;

    /**
     * <p>그룹 목록 수</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectGrpMngListCnt(UserMngVO userMngVO) throws Exception;

    /**
     * <p>그룹 상세</p>
     *
     * @param userMngVO
     * @return UserMngVO
     * @throws Exception throws Exception
     */
    UserMngVO selectGrpMngDetail(UserMngVO userMngVO) throws Exception;

    /**
     * <p>그룹 등록</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertGrpMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>그룹 수정</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int updateGrpMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>그룹 삭제</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteGrpMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>그룹 엑셀 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<UserMngVO> selectGrpMngExcelList(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<UserMngVO> selectUserMngList(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 목록 수</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectUserMngListCnt(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 상세</p>
     *
     * @param userMngVO
     * @return UserMngVO
     * @throws Exception throws Exception
     */
    UserMngVO selectUserMngDetail(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 권한 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<UserMngVO> selectUserAuthMngList(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 아이디 중복 체크</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int selectUserMngDupCnt(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 등록</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertUserMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 권한 등록</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int insertUserAuthMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 수정</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int updateUserMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 삭제</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteUserMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 권한 삭제</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    int deleteUserAuthMngData(UserMngVO userMngVO) throws Exception;

    /**
     * <p>사용자 엑셀 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    List<UserMngVO> selectUserMngExcelList(UserMngVO userMngVO) throws Exception;
}
