package com.jsplan.drp.domain.sys.usermng;

import com.jsplan.drp.global.util.StringUtil;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : UserMngService
 * @Author : KDW
 * @Date : 2022-01-25
 * @Description : 사용자 관리 Service
 */
@Service("UserMngService")
public class UserMngService {

    @Resource
    private UserMngMapper userMngMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * <p>그룹 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<UserMngVO> selectGrpMngList(UserMngVO userMngVO) throws Exception {
        return userMngMapper.selectGrpMngList(userMngVO);
    }

    /**
     * <p>그룹 목록 수</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectGrpMngListCnt(UserMngVO userMngVO) throws Exception {
        return userMngMapper.selectGrpMngListCnt(userMngVO);
    }

    /**
     * <p>그룹 상세</p>
     *
     * @param userMngVO
     * @return UserMngVO
     * @throws Exception throws Exception
     */
    public UserMngVO selectGrpMngDetail(UserMngVO userMngVO) throws Exception {
        return userMngMapper.selectGrpMngDetail(userMngVO);
    }

    /**
     * <p>그룹 등록</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String insertGrpMngData(UserMngVO userMngVO) throws Exception {
        return userMngMapper.insertGrpMngData(userMngVO) > 0 ? "S" : "N";
    }

    /**
     * <p>그룹 수정</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateGrpMngData(UserMngVO userMngVO) throws Exception {
        return userMngMapper.updateGrpMngData(userMngVO) > 0 ? "S" : "N";
    }

    /**
     * <p>그룹 삭제</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String deleteGrpMngData(UserMngVO userMngVO) throws Exception {
        String code = null;

        // 1. 그룹 내 사용자 조회
        int cnt = userMngMapper.selectUserMngListCnt(userMngVO);

        if (cnt > 0) {
            code = "F"; // 그룹 내 사용자가 존재할 경우
        } else {
            // 2. 그룹 삭제
            code = userMngMapper.deleteGrpMngData(userMngVO) > 0 ? "S" : "N";
        }

        return code;
    }

    /**
     * <p>그룹 엑셀 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<UserMngVO> selectGrpMngExcelList(UserMngVO userMngVO) throws Exception {
        return (List<UserMngVO>) userMngMapper.selectGrpMngExcelList(userMngVO);
    }

    /**
     * <p>사용자 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<UserMngVO> selectUserMngList(UserMngVO userMngVO) throws Exception {
        return userMngMapper.selectUserMngList(userMngVO);
    }

    /**
     * <p>사용자 목록 수</p>
     *
     * @param userMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectUserMngListCnt(UserMngVO userMngVO) throws Exception {
        return userMngMapper.selectUserMngListCnt(userMngVO);
    }

    /**
     * <p>사용자 상세</p>
     *
     * @param userMngVO
     * @return UserMngVO
     * @throws Exception throws Exception
     */
    public UserMngVO selectUserMngDetail(UserMngVO userMngVO) throws Exception {
        return userMngMapper.selectUserMngDetail(userMngVO);
    }

    /**
     * <p>사용자 권한 목록</p>
     *
     * @param userMngVO
     * @return JSONArray
     * @throws Exception throws Exception
     */
    public JSONArray selectUserAuthMngList(UserMngVO userMngVO) throws Exception {
        String userId = userMngVO.getUserId();
        JSONArray authArray = new JSONArray();
        JSONObject authObject = new JSONObject();
        userMngVO.setUserIdList(Arrays.asList(userId.split(",")));

        // 하위 권한 조회
        List<UserMngVO> userAuthMngList = userMngMapper.selectUserAuthMngList(userMngVO);
        for (UserMngVO authVO : userAuthMngList) {
            authObject = new JSONObject();
            authObject.put("id", authVO.getAuthCd());
            authObject.put("text", authVO.getAuthNm());
            authObject.put("leaf", "Y".equals(authVO.getLastYn()));
            authObject.put("expanded", !"Y".equals(authVO.getLastYn()));
            authObject.put("checked", "Y".equals(authVO.getAuthYn()));
            authArray.add(authObject);
        }

        return authArray;
    }

    /**
     * <p>사용자 아이디 중복 체크</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    public String selectUserMngDupCnt(UserMngVO userMngVO) throws Exception {
        int cnt = userMngMapper.selectUserMngDupCnt(userMngVO);
        return !StringUtil.isBlank(userMngVO.getUserId()) && cnt == 0 ? "S" : "D";
    }

    /**
     * <p>사용자 등록</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String insertUserMngData(UserMngVO userMngVO) throws Exception {
        String code = null;

        // 1. 사용자 등록
        // BCrypt 패스워드 암호화
        String encodePw = passwordEncoder.encode(userMngVO.getUserPw());
        userMngVO.setUserPw(encodePw);

        code = userMngMapper.insertUserMngData(userMngVO) > 0 ? "S" : "N";

        // 2-1. 사용자 권한 등록
        if (!StringUtil.isEmpty(userMngVO.getAuthCd())) {
            String[] arrAuthCd = StringUtil.split(userMngVO.getAuthCd());
            for (String authCd : arrAuthCd) {
                userMngVO.setAuthCd(authCd);
                userMngMapper.insertUserAuthMngData(userMngVO);
            }
        }

        // 2-2. 기본 인증 권한 등록
        userMngVO.setAuthCd("IS_AUTHENTICATED_FULLY");
        userMngMapper.insertUserAuthMngData(userMngVO);

        return code;
    }

    /**
     * <p>사용자 수정</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateUserMngData(UserMngVO userMngVO) throws Exception {
        String code = null;

        // 1. 사용자 수정
        // BCrypt 패스워드 암호화
        String userPw = userMngVO.getUserPw();
        if (!StringUtil.isEmpty(userPw)) {
            String encodePw = passwordEncoder.encode(userPw);
            userMngVO.setUserPw(encodePw);
        }

        code = userMngMapper.updateUserMngData(userMngVO) > 0 ? "S" : "N";

        // 2-1. 사용자 권한 삭제
        userMngMapper.deleteUserAuthMngData(userMngVO);

        // 2-2. 사용자 권한 등록
        if (!StringUtil.isEmpty(userMngVO.getAuthCd())) {
            String[] arrAuthCd = StringUtil.split(userMngVO.getAuthCd());
            for (String authCd : arrAuthCd) {
                userMngVO.setAuthCd(authCd);
                userMngMapper.insertUserAuthMngData(userMngVO);
            }
        }

        // 2-3. 기본 인증 권한 등록
        userMngVO.setAuthCd("IS_AUTHENTICATED_FULLY");
        userMngMapper.insertUserAuthMngData(userMngVO);

        return code;
    }

    /**
     * <p>사용자 삭제</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String deleteUserMngData(UserMngVO userMngVO) throws Exception {
        String code = null;

        // 1. 사용자 권한 삭제
        userMngMapper.deleteUserAuthMngData(userMngVO);

        // 2. 사용자 삭제
        code = userMngMapper.deleteUserMngData(userMngVO) > 0 ? "S" : "N";

        return code;
    }

    /**
     * <p>권한 설정 적용</p>
     *
     * @param userMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateUserAuthMngData(UserMngVO userMngVO) throws Exception {
        int cnt = 0;
        String[] arrUserId = StringUtil.split(userMngVO.getUserId());
        String[] arrAuthCd = StringUtil.split(userMngVO.getAuthCd());

        for (String userId : arrUserId) {
            userMngVO.setUserId(userId);

            // 1. 사용자 권한 삭제
            userMngMapper.deleteUserAuthMngData(userMngVO);

            // 2. 사용자 권한 등록
            if (arrAuthCd.length > 0) {
                for (String authCd : arrAuthCd) {
                    userMngVO.setAuthCd(authCd);
                    userMngMapper.insertUserAuthMngData(userMngVO);
                }
            }

            // 3. 기본 인증 권한 등록
            userMngVO.setAuthCd("IS_AUTHENTICATED_FULLY");
            cnt += userMngMapper.insertUserAuthMngData(userMngVO);
        }

        return cnt > 0 ? "S" : "N";
    }

    /**
     * <p>사용자 엑셀 목록</p>
     *
     * @param userMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<UserMngVO> selectUserMngExcelList(UserMngVO userMngVO) throws Exception {
        return (List<UserMngVO>) userMngMapper.selectUserMngExcelList(userMngVO);
    }
}
