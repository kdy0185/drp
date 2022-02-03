package com.jsplan.drp.cmmn.bean;

import com.jsplan.drp.cmmn.obj.ComsService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.FactoryBean;

/**
 * @Class : HierarchyStringsFactoryBean
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 계층화 권한 Bean 연동
 */

public class HierarchyStringsFactoryBean implements FactoryBean<String> {

    private ComsService comsService;
    private String hierarchyStrings;

    /**
     * <p>comsService Setter 메소드</p>
     *
     * @param comsService (comsService 클래스)
     */
    public void setComsService(ComsService comsService) {
        this.comsService = comsService;
    }

    /**
     * <p>해당 클래스를 &lt;bean&gt; 태그로 설정 후 구동 시 실행되는 메소드</p>
     */
    @PostConstruct
    public void init() throws Exception {
        // 계층화된 권한 목록을 String 형태로 저장
        hierarchyStrings = comsService.selectHierarchicalAuthList();
    }

    /**
     * <p>해당 클래스가 다른 &lt;bean&gt; 태그에서 참조된 후 구동 시 실행되는 메소드</p>
     *
     * @return String (계층화 권한 목록의 String 형태)
     */
    @Override
    public String getObject() throws Exception {
        // 계층화된 권한 목록을 String 형태로 저장
        if (hierarchyStrings == null) {
            hierarchyStrings = comsService.selectHierarchicalAuthList();
        }
        return hierarchyStrings;
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
