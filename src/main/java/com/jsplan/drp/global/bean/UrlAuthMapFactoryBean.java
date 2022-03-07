package com.jsplan.drp.global.bean;

import com.jsplan.drp.global.obj.service.ComsService;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @Class : UrlAuthMapFactoryBean
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : URL별 권한 Bean 연동
 */
public class UrlAuthMapFactoryBean implements
    FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private ComsService comsService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

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
     *
     * @throws Exception throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        // URL별 권한 목록을 LinkedHashMap<RequestMatcher, List<ConfigAttribute>> 형태로 저장
        requestMap = comsService.selectComsUrlAuthList();
    }

    /**
     * <p>해당 클래스가 다른 &lt;bean&gt; 태그에서 참조된 후 구동 시 실행되는 메소드</p>
     *
     * @return LinkedHashMap (URL별 권한 목록의 LinkedHashMap 형태)
     * @throws Exception throws Exception
     */
    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject()
        throws Exception {
        // URL별 권한 목록을 LinkedHashMap<RequestMatcher, List<ConfigAttribute>> 형태로 저장
        if (requestMap == null) {
            requestMap = comsService.selectComsUrlAuthList();
        }
        return requestMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
