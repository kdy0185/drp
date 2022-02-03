package com.jsplan.drp.cmmn.bean;

import com.jsplan.drp.cmmn.obj.ComsService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;

/**
 * @Class : AvailableRoleHierarchy
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 계층화 권한 설정
 */
public class AvailableRoleHierarchy {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RoleHierarchyImpl roleHierarchy;
    private ComsService comsService;

    /**
     * <p>HierarchyStringsFactoryBean 클래스 Injection을 통한 roleHierarchy 저장</p>
     *
     * @param roleHierarchy (권한 계층 설정 클래스)
     */
    public AvailableRoleHierarchy(RoleHierarchyImpl roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    /**
     * <p>HierarchyStringsFactoryBean 클래스 Injection을 통한 comsService 저장</p>
     *
     * @param comsService (comsService 클래스)
     */
    public void setComsService(ComsService comsService) {
        this.comsService = comsService;
    }

    /**
     * <p>하위 계층 권한 포함 조회</p>
     *
     * @param authorities (조회할 권한 목록)
     * @return List (하위 계층 권한 포함한 목록)
     */
    public List<GrantedAuthority> getReachableAuthorities(List<GrantedAuthority> authorities)
        throws Exception {
        return (List<GrantedAuthority>) roleHierarchy.getReachableGrantedAuthorities(authorities);
    }


    /**
     * <p>권한 정보 변경 시 Reload</p>
     */
    public void reload() throws Exception {
        String reloadHierachyStrings = comsService.selectHierarchicalAuthList();
        roleHierarchy.setHierarchy(reloadHierachyStrings);

        if (logger.isInfoEnabled()) {
            logger.info("\nRole's Hierarchy Reloaded.\n");
        }
    }

}
