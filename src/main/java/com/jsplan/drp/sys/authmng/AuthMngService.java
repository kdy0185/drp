package com.jsplan.drp.sys.authmng;

import com.jsplan.drp.cmmn.util.StringUtil;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : AuthMngService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 권한 관리 Service
 */
@Service("AuthMngService")
public class AuthMngService {

    @Resource
    private AuthMngMapper authMngMapper;

    /**
     * <p>권한 목록</p>
     *
     * @param authMngVO
     * @return JSONArray
     * @throws Exception throws Exception
     */
    public JSONArray selectAuthMngList(AuthMngVO authMngVO) throws Exception {
        JSONArray authMngArray = new JSONArray();
        JSONObject authMngObject = new JSONObject();

        // 하위 권한 조회
        List<AuthMngVO> authMngList = authMngMapper.selectAuthMngList(authMngVO);
        for (AuthMngVO vo : authMngList) {
            authMngObject = new JSONObject();
            authMngObject.put("authNm", vo.getAuthNm());
            authMngObject.put("authCd", vo.getAuthCd());
            authMngObject.put("authDesc", vo.getAuthDesc());
            authMngObject.put("authLv", vo.getAuthLv());
            authMngObject.put("authOrd", vo.getAuthOrd());
            authMngObject.put("useYn", vo.getUseYn());
            authMngObject.put("leaf", "Y".equals(vo.getLastYn()));
            authMngObject.put("expanded", !"Y".equals(vo.getLastYn()));

            authMngArray.add(authMngObject);
        }

        return authMngArray;
    }

    /**
     * <p>상위 권한 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<AuthMngVO> selectUpperAuthMngList(AuthMngVO authMngVO)
        throws Exception {
        return (List<AuthMngVO>) authMngMapper.selectUpperAuthMngList(authMngVO);
    }

    /**
     * <p>권한 상세</p>
     *
     * @param authMngVO
     * @return AuthMngVO
     * @throws Exception throws Exception
     */
    public AuthMngVO selectAuthMngDetail(AuthMngVO authMngVO) throws Exception {
        return authMngMapper.selectAuthMngDetail(authMngVO);
    }

    /**
     * <p>권한 등록</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String insertAuthMngData(AuthMngVO authMngVO) throws Exception {
        return authMngMapper.insertAuthMngData(authMngVO) > 0 ? "S" : "N";
    }

    /**
     * <p>권한 수정</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateAuthMngData(AuthMngVO authMngVO) throws Exception {
        return authMngMapper.updateAuthMngData(authMngVO) > 0 ? "S" : "N";
    }

    /**
     * <p>권한 삭제</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String deleteAuthMngData(AuthMngVO authMngVO) throws Exception {
        String code = null;

        // 1. 권한별 사용자 조회
        int userCnt = authMngMapper.selectAuthUserListCnt(authMngVO);
        // 2. 권한별 메뉴 조회
        int menuCnt = authMngMapper.selectAuthMenuListCnt(authMngVO);

        if (userCnt > 0) {
            code = "U"; // 권한별 사용자가 존재할 경우
        } else if (menuCnt > 0) {
            code = "M"; // 권한별 메뉴가 존재할 경우
        } else {
            // 3. 권한 삭제
            code = authMngMapper.deleteAuthMngData(authMngVO) > 0 ? "S" : "N";
        }

        return code;
    }

    /**
     * <p>권한별 사용자 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<AuthMngVO> selectAuthUserMngList(AuthMngVO authMngVO)
        throws Exception {
        authMngVO.setAuthCdList(Arrays.asList(authMngVO.getAuthCd().split(",")));
        return authMngMapper.selectAuthUserMngList(authMngVO);
    }

    /**
     * <p>권한별 사용자 목록 수</p>
     *
     * @param authMngVO
     * @return int
     * @throws Exception throws Exception
     */
    public int selectAuthUserMngListCnt(AuthMngVO authMngVO) throws Exception {
        authMngVO.setAuthCdList(Arrays.asList(authMngVO.getAuthCd().split(",")));
        return authMngMapper.selectAuthUserMngListCnt(authMngVO);
    }

    /**
     * <p>사용자 설정 적용</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateAuthUserMngData(AuthMngVO authMngVO) throws Exception {
        int cnt = 0;
        String[] arrAuthCd = StringUtil.split(authMngVO.getAuthCd());
        String[] arrUserId = StringUtil.split(authMngVO.getUserId());
        String authYn = authMngVO.getAuthYn();

        for (String authCd : arrAuthCd) {
            authMngVO.setAuthCd(authCd);
            if (arrUserId.length > 0) {
                for (String userId : arrUserId) {
                    authMngVO.setUserId(userId);
                    if ("Y".equals(authYn)) {
                        cnt += authMngMapper.insertAuthUserMngData(authMngVO); // 권한 허용
                    }
                    if ("N".equals(authYn)) {
                        cnt += authMngMapper.deleteAuthUserMngData(authMngVO); // 권한 해제
                    }
                }
            }
        }

        return cnt > 0 ? "S" : "N";
    }

    /**
     * <p>권한별 메뉴 목록</p>
     *
     * @param authMngVO
     * @return JSONArray
     * @throws Exception throws Exception
     */
    public JSONArray selectAuthMenuMngList(AuthMngVO authMngVO)
        throws Exception {
        String authCd = authMngVO.getAuthCd();
        JSONArray menuArray = new JSONArray();
        JSONObject menuObject = new JSONObject();
        authMngVO.setAuthCdList(Arrays.asList(authCd.split(",")));

        // 하위 메뉴 조회
        List<AuthMngVO> authMenuMngList = authMngMapper.selectAuthMenuMngList(authMngVO);
        for (AuthMngVO menuVO : authMenuMngList) {
            menuObject = new JSONObject();
            menuObject.put("id", menuVO.getMenuCd());
            menuObject.put("text", menuVO.getMenuNm());
            menuObject.put("leaf", "Y".equals(menuVO.getLastYn()));
            menuObject.put("expanded", !"Y".equals(menuVO.getLastYn()));
            menuObject.put("checked", "Y".equals(menuVO.getAuthYn()));
            menuArray.add(menuObject);
        }

        return menuArray;
    }

    /**
     * <p>메뉴 설정 적용</p>
     *
     * @param authMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateAuthMenuMngData(AuthMngVO authMngVO) throws Exception {
        int cnt = 0;
        String[] arrAuthCd = StringUtil.split(authMngVO.getAuthCd());
        String[] arrMenuCd = StringUtil.split(authMngVO.getMenuCd());

        for (String authCd : arrAuthCd) {
            authMngVO.setAuthCd(authCd);

            // 1. 권한별 메뉴 삭제
            cnt += authMngMapper.deleteAuthMenuMngData(authMngVO);

            // 2. 권한별 메뉴 등록
            if (arrMenuCd.length > 0) {
                for (String menuCd : arrMenuCd) {
                    authMngVO.setMenuCd(menuCd);
                    cnt += authMngMapper.insertAuthMenuMngData(authMngVO);
                }
            }
        }

        return cnt > 0 ? "S" : "N";
    }

    /**
     * <p>권한 엑셀 목록</p>
     *
     * @param authMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<AuthMngVO> selectAuthMngExcelList(AuthMngVO authMngVO)
        throws Exception {
        return (List<AuthMngVO>) authMngMapper.selectAuthMngExcelList(authMngVO);
    }
}
