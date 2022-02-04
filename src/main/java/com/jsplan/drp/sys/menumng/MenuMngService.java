package com.jsplan.drp.sys.menumng;

import com.jsplan.drp.cmmn.util.StringUtil;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : MenuMngService
 * @Author : KDW
 * @Date : 2022-01-26
 * @Description : 메뉴 관리 Service
 */
@Service("MenuMngService")
public class MenuMngService {

    @Resource
    private MenuMngMapper menuMngMapper;

    /**
     * <p>메뉴 목록</p>
     *
     * @param menuMngVO
     * @return JSONArray
     * @throws Exception throws Exception
     */
    public JSONArray selectMenuMngList(MenuMngVO menuMngVO) throws Exception {
        JSONArray menuMngArray = new JSONArray();
        JSONObject menuMngObject = new JSONObject();

        // 하위 메뉴 조회
        List<MenuMngVO> menuMngList = menuMngMapper.selectMenuMngList(menuMngVO);
        for (MenuMngVO vo : menuMngList) {
            menuMngObject = new JSONObject();
            menuMngObject.put("menuNm", vo.getMenuNm());
            menuMngObject.put("menuCd", vo.getMenuCd());
            menuMngObject.put("upperMenuCd", vo.getUpperMenuCd());
            menuMngObject.put("menuUrl", vo.getMenuUrl());
            menuMngObject.put("menuLv", vo.getMenuLv());
            menuMngObject.put("menuOrd", vo.getMenuOrd());
            menuMngObject.put("useYn", vo.getUseYn());
            menuMngObject.put("leaf", "Y".equals(vo.getLastYn()));
            menuMngObject.put("expanded", true);

            menuMngArray.add(menuMngObject);
        }

        return menuMngArray;
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuMngVO
     * @return MenuMngVO
     * @throws Exception throws Exception
     */
    public MenuMngVO selectMenuMngDetail(MenuMngVO menuMngVO) throws Exception {
        return menuMngMapper.selectMenuMngDetail(menuMngVO);
    }

    /**
     * <p>메뉴별 권한 목록</p>
     *
     * @param menuMngVO
     * @return JSONArray
     * @throws Exception throws Exception
     */
    public JSONArray selectMenuAuthMngList(MenuMngVO menuMngVO) throws Exception {
        String menuCd = menuMngVO.getMenuCd();
        JSONArray authArray = new JSONArray();
        JSONObject authObject = new JSONObject();
        menuMngVO.setMenuCdList(Arrays.asList(menuCd.split(",")));

        // 하위 권한 조회
        List<MenuMngVO> menuAuthMngList = menuMngMapper.selectMenuAuthMngList(menuMngVO);
        for (MenuMngVO authVO : menuAuthMngList) {
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
     * <p>메뉴 등록</p>
     *
     * @param menuMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String insertMenuMngData(MenuMngVO menuMngVO) throws Exception {
        String code = null;

        // 1. 메뉴 등록
        code = menuMngMapper.insertMenuMngData(menuMngVO) > 0 ? "S" : "N";

        // 2. 메뉴별 권한 등록
        if (!StringUtil.isEmpty(menuMngVO.getAuthCd())) {
            String[] arrAuthCd = StringUtil.split(menuMngVO.getAuthCd());
            for (String authCd : arrAuthCd) {
                menuMngVO.setAuthCd(authCd);
                menuMngMapper.insertMenuAuthMngData(menuMngVO);
            }
        }

        return code;
    }

    /**
     * <p>메뉴 수정</p>
     *
     * @param menuMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateMenuMngData(MenuMngVO menuMngVO) throws Exception {
        String code = null;

        // 1. 메뉴 수정
        code = menuMngMapper.updateMenuMngData(menuMngVO) > 0 ? "S" : "N";

        // 2. 메뉴별 권한 삭제
        menuMngMapper.deleteMenuAuthMngData(menuMngVO);

        // 3. 메뉴별 권한 등록
        if (!StringUtil.isEmpty(menuMngVO.getAuthCd())) {
            String[] arrAuthCd = StringUtil.split(menuMngVO.getAuthCd());
            for (String authCd : arrAuthCd) {
                menuMngVO.setAuthCd(authCd);
                menuMngMapper.insertMenuAuthMngData(menuMngVO);
            }
        }

        return code;
    }

    /**
     * <p>메뉴 삭제</p>
     *
     * @param menuMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String deleteMenuMngData(MenuMngVO menuMngVO) throws Exception {
        String code = null;

        // 1. 메뉴별 권한 삭제
        menuMngMapper.deleteMenuAuthMngData(menuMngVO);

        // 2. 메뉴 삭제
        code = menuMngMapper.deleteMenuMngData(menuMngVO) > 0 ? "S" : "N";

        return code;
    }

    /**
     * <p>권한 설정 적용</p>
     *
     * @param menuMngVO
     * @return String
     * @throws Exception throws Exception
     */
    @Transactional
    public String updateMenuAuthMngData(MenuMngVO menuMngVO) throws Exception {
        int cnt = 0;
        String[] arrMenuCd = StringUtil.split(menuMngVO.getMenuCd());
        String[] arrAuthCd = StringUtil.split(menuMngVO.getAuthCd());

        for (String menuCd : arrMenuCd) {
            menuMngVO.setMenuCd(menuCd);

            // 1. 메뉴별 권한 삭제
            cnt += menuMngMapper.deleteMenuAuthMngData(menuMngVO);

            // 2. 메뉴별 권한 등록
            if (arrAuthCd.length > 0) {
                for (String authCd : arrAuthCd) {
                    menuMngVO.setAuthCd(authCd);
                    cnt += menuMngMapper.insertMenuAuthMngData(menuMngVO);
                }
            }

            // 3. 기본 권한 등록
            if ("A0000".equals(menuCd) || "A0100".equals(menuCd)) { // 메인 메뉴는 반드시 인증 권한 추가 필요
                menuMngVO.setAuthCd("IS_AUTHENTICATED_FULLY");
                cnt += menuMngMapper.insertMenuAuthMngData(menuMngVO);
            }
            if ("A0200".equals(menuCd)) { // 로그인 메뉴는 반드시 익명 권한 추가 필요
                menuMngVO.setAuthCd("ANONYMOUS");
                cnt += menuMngMapper.insertMenuAuthMngData(menuMngVO);
            }
        }

        return cnt > 0 ? "S" : "N";
    }

    /**
     * <p>메뉴 엑셀 목록</p>
     *
     * @param menuMngVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<MenuMngVO> selectMenuMngExcelList(MenuMngVO menuMngVO) throws Exception {
        return (List<MenuMngVO>) menuMngMapper.selectMenuMngExcelList(menuMngVO);
    }
}
