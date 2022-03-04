package com.jsplan.drp.global.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

/**
 * @Class : AjaxSessionTimeoutFilter
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : ajax 사용 시 로그인 연동
 */
public class AjaxSessionTimeoutFilter implements Filter {

    private String ajaxHeader;

    /**
     * <p>ajaxHeader Setter 메소드</p>
     *
     * @param ajaxHeader (요청 헤더 변수)
     */
    public void setAjaxHeader(String ajaxHeader) {
        this.ajaxHeader = ajaxHeader;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * <p>Filter 처리</p>
     *
     * @param request  (ServletRequest 객체)
     * @param response (ServletResponse 객체)
     * @param chain    (FilterChain 객체)
     * @throws IOException      throws IOException
     * @throws ServletException throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (isAjaxRequest(req)) {
            try {
                chain.doFilter(req, res);
            } catch (AccessDeniedException e) { // 403 : 접근 거부
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
            } catch (AuthenticationException e) { // 401 : 인증 실패
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        return req.getHeader(ajaxHeader) != null && req.getHeader(ajaxHeader)
            .equals(Boolean.TRUE.toString());
    }

    @Override
    public void destroy() {
    }
}
