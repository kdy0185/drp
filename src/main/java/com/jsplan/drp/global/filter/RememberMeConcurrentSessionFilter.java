package com.jsplan.drp.global.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * @Class : RememberMeConcurrentSessionFilter
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 자동 로그인 설정 시 동시 세션 제어
 */
public class RememberMeConcurrentSessionFilter extends RememberMeAuthenticationFilter {

    private SessionAuthenticationStrategy sessionAuthenticationStrategy = new NullAuthenticatedSessionStrategy();

    /**
     * <p>자동 로그인 설정 시 동시 세션 제어 Filter 생성자</p>
     *
     * @param authenticationManager (인증 관리 객체)
     * @param rememberMeServices    (자동 로그인 옵션 객체)
     */
    public RememberMeConcurrentSessionFilter(
        AuthenticationManager authenticationManager,
        RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }

    /**
     * <p>RememberMeAuthenticationFilter 클래스 Injection을 통한 sessionAuthenticationStrategy 저장</p>
     *
     * @param sessionAuthenticationStrategy (sessionAuthenticationStrategy 클래스)
     */
    public void setSessionAuthenticationStrategy(
        SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }

    /**
     * <p>Filter 처리</p>
     *
     * @param req   (ServletRequest 객체)
     * @param res   (ServletResponse 객체)
     * @param chain (FilterChain 객체)
     * @throws IOException      throws IOException
     * @throws ServletException throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
        FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }

    /**
     * <p>인증 성공 시 설정한 동시 세션 처리 전략 적용</p>
     *
     * @param request    (HttpServletRequest 객체)
     * @param response   (HttpServletResponse 객체)
     * @param authResult (Authentication 객체)
     */
    @Override
    public void onSuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, Authentication authResult) {
        sessionAuthenticationStrategy.onAuthentication(authResult, request, response);
    }
}
