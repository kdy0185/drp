package com.jsplan.drp.global.obj.service;

import com.jsplan.drp.global.obj.dto.ComsDTO;
import com.jsplan.drp.global.obj.dto.ComsMenuDTO;
import com.jsplan.drp.global.obj.repository.ComsRepository;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

/**
 * @Class : ComsService
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 공통 Service
 */

@Service("ComsService")
public class ComsService {

    @Autowired
    private ComsRepository comsRepository;

    /**
     * <p>메뉴 목록</p>
     *
     * @return List (메뉴 목록)
     */
    public List<ComsMenuDTO> selectComsMenuList() {
        return comsRepository.selectComsMenuList();
    }

    /**
     * <p>메뉴 상세</p>
     *
     * @param menuCd (메뉴 코드)
     * @return ComsMenuDTO (메뉴 정보)
     */
    public ComsMenuDTO selectComsMenuDetail(String menuCd) {
        return comsRepository.selectComsMenuDetail(menuCd);
    }

    /**
     * <p>URL별 권한 목록</p>
     *
     * @return LinkedHashMap (URL별 권한 목록)
     */
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> selectComsUrlAuthList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> urlAuthMap = new LinkedHashMap<>();
        List<ComsMenuDTO> list = comsRepository.selectComsUrlAuthList();
        Iterator<ComsMenuDTO> itr = list.iterator();
        ComsMenuDTO vo;
        String preMenuUrl = null;
        String menuUrl, authCd;
        RequestMatcher antUrl;

        // 조회된 URL별 권한 목록을 LinkedHashMap 형식으로 변환
        while (itr.hasNext()) {
            vo = itr.next();
            menuUrl = vo.getMenuUrl();
            authCd = vo.getAuthCd();
            antUrl = new AntPathRequestMatcher(menuUrl); // RequestKey 형식으로 URL 변환
            List<ConfigAttribute> configList = new LinkedList<>();

            // 해당 URL에 대한 권한이 여러 개 매핑되어 있는 경우
            if (preMenuUrl != null && menuUrl.equals(preMenuUrl)) {
                // 해당 URL에서 조회된 이전 Row의 권한을 모두 configList에 추가
                for (ConfigAttribute configAttribute : urlAuthMap.get(antUrl)) {
                    SecurityConfig tempConfig = (SecurityConfig) configAttribute;
                    configList.add(tempConfig);
                }
            }

            // 해당 URL에서 조회된 현재 Row의 권한을 configList에 추가
            configList.add(new SecurityConfig(authCd));
            // 같은 URL - RequestKey일 경우 새로 추출한 configList로 덮어쓰기
            urlAuthMap.put(antUrl, configList);
            preMenuUrl = menuUrl;
        }

        return urlAuthMap;
    }

    /**
     * <p>계층화 권한 목록</p>
     *
     * @return String (계층화 권한 목록)
     */
    public String selectHierarchicalAuthList() {
        List<ComsMenuDTO> authList = comsRepository.selectHierarchicalAuthList();
        StringBuilder hierarchyStrings = new StringBuilder();
        for (ComsMenuDTO vo : authList) {
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
     * @param grpCd (그룹 코드)
     * @return List (공통 코드 목록)
     */
    public List<ComsDTO> selectComsCodeList(String grpCd) {
        return comsRepository.selectComsCodeList(grpCd);
    }

    /**
     * <p>그룹 목록</p>
     *
     * @return List (그룹 목록)
     */
    public List<ComsDTO> selectComsGrpList() {
        return comsRepository.selectComsGrpList();
    }

    /**
     * <p>담당자 목록</p>
     *
     * @param comsDTO (담당자 정보)
     * @return List (담당자 목록)
     */
    public List<ComsDTO> selectComsUserList(ComsDTO comsDTO) {
        return comsRepository.selectComsUserList(comsDTO.getGrpCd(), comsDTO.getSearchCd(),
            comsDTO.getSearchWord());
    }
}
