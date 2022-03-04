package com.jsplan.drp.global.bean;

import com.jsplan.drp.global.obj.ComsService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @Class : ReloadableFilterInvocationSecurityMetadataSource
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : URL별 권한 설정
 */
public class ReloadableFilterInvocationSecurityMetadataSource implements
    FilterInvocationSecurityMetadataSource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<RequestMatcher, List<ConfigAttribute>> requestMap;
    private ComsService comsService;

    /**
     * <p>UrlAuthMapFactoryBean 클래스 Injection을 통한 requestMap 저장</p>
     *
     * @param requestMap (URL별 권한 목록의 LinkedHashMap 형태)
     */
    public ReloadableFilterInvocationSecurityMetadataSource(
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap) {
        this.requestMap = requestMap;
    }

    /**
     * <p>UrlAuthMapFactoryBean 클래스 Injection을 통한 comsService 저장</p>
     *
     * @param comsService (comsService 클래스)
     */
    public void setComsService(ComsService comsService) {
        this.comsService = comsService;
    }

    /**
     * <p>접근할 URL의 Ant 패턴에 맞는 권한 목록 조회</p>
     *
     * @param object (접근 URL)
     * @return Collection (권한 목록)
     * @throws IllegalArgumentException throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
        throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        Collection<ConfigAttribute> result = null;
        for (Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

    /**
     * <p>모든 Ant 패턴에 대한 권한 목록 조회</p>
     *
     * @return Collection (권한 목록)
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        for (Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * <p>URL별 권한 데이터 변경 시 Reload</p>
     *
     * @throws Exception throws Exception
     */
    public void reload() throws Exception {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadedMap = comsService.selectComsUrlAuthList();
        Iterator<Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadedMap.entrySet()
            .iterator();

        // 이전 데이터 삭제
        requestMap.clear();

        while (iterator.hasNext()) {
            Entry<RequestMatcher, List<ConfigAttribute>> entry = iterator.next();
            requestMap.put(entry.getKey(), entry.getValue());
        }

        if (logger.isInfoEnabled()) {
            logger.info("\nSecured URL & Auth Mappings Reloaded.\n");
        }
    }

}
