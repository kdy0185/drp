package com.jsplan.drp.global.obj.service;

import com.jsplan.drp.global.obj.entity.ComsMenuVO;
import com.jsplan.drp.global.obj.entity.ComsVO;
import com.jsplan.drp.global.obj.mapper.ComsMapper;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Class : ComsService
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 공통 Service
 */

@Service("ComsService")
public class ComsService {

    @Resource
    private ComsMapper ComsMapper;

    /**
     * <p>메뉴 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    public List<ComsMenuVO> selectComsMenuList() throws Exception {
        return ComsMapper.selectComsMenuList();
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd
     * @return ComsMenuVO
     * @throws Exception throws Exception
     */
    public ComsMenuVO selectComsMenuDetail(String menuCd) throws Exception {
        return ComsMapper.selectComsMenuDetail(menuCd);
    }

    /**
     * <p>URL별 권한 목록</p>
     *
     * @return LinkedHashMap<RequestMatcher, List < ConfigAttribute>>
     * @throws Exception throws Exception
     */
    @Transactional
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> selectComsUrlAuthList()
        throws Exception {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> urlAuthMap = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();
        List<ComsMenuVO> list = ComsMapper.selectComsUrlAuthList();
        Iterator<ComsMenuVO> itr = list.iterator();
        ComsMenuVO vo;
        String preMenuUrl = null;
        String menuUrl, authCd;
        Object antUrl;

        // 조회된 URL별 권한 목록을 LinkedHashMap 형식으로 변환
        while (itr.hasNext()) {
            vo = itr.next();
            menuUrl = vo.getMenuUrl();
            authCd = vo.getAuthCd();
            antUrl = new AntPathRequestMatcher(menuUrl); // RequestKey 형식으로 URL 변환
            List<ConfigAttribute> configList = new LinkedList<ConfigAttribute>();

            // 해당 URL에 대한 권한이 여러 개 매핑되어 있는 경우
            if (preMenuUrl != null && menuUrl.equals(preMenuUrl)) {
                // 해당 URL에서 조회된 이전 Row의 권한을 모두 configList에 추가
                List<ConfigAttribute> preAuthList = urlAuthMap.get(antUrl);
                Iterator<ConfigAttribute> preAuthItr = preAuthList.iterator();
                while (preAuthItr.hasNext()) {
                    SecurityConfig tempConfig = (SecurityConfig) preAuthItr.next();
                    configList.add(tempConfig);
                }
            }

            // 해당 URL에서 조회된 현재 Row의 권한을 configList에 추가
            configList.add(new SecurityConfig(authCd));
            // 같은 URL - RequestKey일 경우 새로 추출한 configList로 덮어쓰기
            urlAuthMap.put((RequestMatcher) antUrl, configList);
            preMenuUrl = menuUrl;
        }

        return urlAuthMap;
    }

    /**
     * <p>계층화 권한 목록</p>
     *
     * @return String
     * @throws Exception throws Exception
     */
    public String selectHierarchicalAuthList() throws Exception {
        List<ComsMenuVO> authList = ComsMapper.selectHierarchicalAuthList();
        StringBuilder hierarchyStrings = new StringBuilder();
        for (ComsMenuVO vo : authList) {
            hierarchyStrings.append(vo.getUpperAuthCd());
            hierarchyStrings.append(" > ");
            hierarchyStrings.append(vo.getAuthCd());
            hierarchyStrings.append("\n");
        }
        return hierarchyStrings.toString();
    }

    /**
     * <p>공통 코드 목록</p>
     *
     * @param comCd
     * @return List
     * @throws Exception throws Exception
     */
    public List<ComsVO> selectComsCodeList(String comCd) throws Exception {
        return ComsMapper.selectComsCodeList(comCd);
    }

    /**
     * <p>그룹 목록</p>
     *
     * @return List
     * @throws Exception throws Exception
     */
    public List<ComsVO> selectComsGrpList() throws Exception {
        return ComsMapper.selectComsGrpList();
    }

    /**
     * <p>담당자 목록</p>
     *
     * @param comsVO
     * @return List
     * @throws Exception throws Exception
     */
    public List<ComsVO> selectComsUserList(ComsVO comsVO) throws Exception {
        return ComsMapper.selectComsUserList(comsVO);
    }
}
