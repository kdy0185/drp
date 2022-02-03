package com.jsplan.drp.cmmn.hdlr;

import com.jsplan.drp.cmmn.obj.UserVO;
import com.jsplan.drp.cmmn.util.StringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

/**
 * @Class : UserAuthSuccessHandler
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 로그인 성공 시 Handler
 */
public class UserAuthSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private String targetUrlParameter;
    private String defaultUrl;
    private boolean useReferer;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private static final String SAVE_ID_CHK = "saveIdChk";
    private static final String COOKIE_NAME = "cookieUserId";
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7; // 7일
    private static final int SESSION_MAX_TIME = 30 * 60; // 30분

    public UserAuthSuccessHandler() {
        targetUrlParameter = "";
        defaultUrl = "/";
        useReferer = false;
    }

    public String getTargetUrlParameter() {
        return targetUrlParameter;
    }

    public void setTargetUrlParameter(String targetUrlParameter) {
        this.targetUrlParameter = targetUrlParameter;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public boolean isUseReferer() {
        return useReferer;
    }

    public void setUseReferer(boolean useReferer) {
        this.useReferer = useReferer;
    }

    /**
     * <p>로그인 성공 시 Handler</p>
     *
     * @param request        (HttpServletRequest 객체)
     * @param response       (HttpServletResponse 객체)
     * @param authentication (Authentication 객체)
     * @throws IOException      throws IOException
     * @throws ServletException throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        resetAuthenticationAttributes(request);

        // ID 저장 설정
        String saveIdChk = request.getParameter(SAVE_ID_CHK);
        if (saveIdChk != null) {
            String userId = ((UserVO) authentication.getPrincipal()).getUserId();
            Cookie cookie = new Cookie(COOKIE_NAME, userId);
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie(COOKIE_NAME, "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        int intRedirectStrategy = decideRedirectStrategy(request, response);
        switch (intRedirectStrategy) {
            case 1:
                useTargetUrl(request, response);
                break;
            case 2:
                useSessionUrl(request, response);
                break;
            case 3:
                useRefererUrl(request, response);
                break;
            default:
                useDefaultUrl(request, response);
        }
    }

    /**
     * <p>인증 설정 초기화</p>
     *
     * @param request (HttpServletRequest 객체)
     */
    private void resetAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        // 세션 유지 시간 설정
        session.setMaxInactiveInterval(SESSION_MAX_TIME);
    }

    /**
     * <p>target url을 적용한 URL 이동</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @throws IOException throws IOException
     */
    private void useTargetUrl(HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            requestCache.removeRequest(request, response);
        }
        String targetUrl = request.getParameter(targetUrlParameter);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * <p>session url을 적용한 URL 이동</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @throws IOException throws IOException
     */
    private void useSessionUrl(HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = savedRequest.getRedirectUrl();
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * <p>referer url을 적용한 URL 이동</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @throws IOException throws IOException
     */
    private void useRefererUrl(HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        String targetUrl = request.getHeader("REFERER");
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * <p>default url을 적용한 URL 이동</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @throws IOException throws IOException
     */
    private void useDefaultUrl(HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        redirectStrategy.sendRedirect(request, response, defaultUrl);
    }

    /**
     * <p>
     * 인증 성공 후 어떤 URL로 redirect 할지를 결정한다. <br/>
     * 판단 기준은 targetUrlParameter 값을 읽은 URL이 존재할 경우 그것을 1순위 <br/>
     * 1순위 URL이 없을 경우 Spring Security가 세션에 저장한 URL을 2순위 <br/>
     * 2순위 URL이 없을 경우 Request의 REFERER를 사용하고 그 REFERER URL이 존재할 경우 그 URL을 3순위 <br/>
     * 3순위 URL이 없을 경우 Default URL을 4순위로 한다.
     * </p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @return <b>int</b> <br/>
     * 1 : targetUrlParameter 값을 읽은 url <br/>
     * 2 : Session에 저장되어 있는 url <br/>
     * 3 : referer 헤더에 있는 url <br/>
     * 0 : default url <br/>
     */
    private int decideRedirectStrategy(HttpServletRequest request,
        HttpServletResponse response) {
        int result = 0;

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (!"".equals(targetUrlParameter)) {
            String targetUrl = request.getParameter(targetUrlParameter);
            if (!StringUtil.isBlank(targetUrl)) {
                result = 1;
            } else {
                if (savedRequest != null) {
                    result = 2;
                } else {
                    String refererUrl = request.getHeader("REFERER");
                    if (useReferer && !StringUtil.isBlank(refererUrl)) {
                        result = 3;
                    } else {
                        result = 0;
                    }
                }
            }

            return result;
        }

        if (savedRequest != null) {
            result = 2;
            return result;
        }

        String refererUrl = request.getHeader("REFERER");
        if (useReferer && !StringUtil.isBlank(refererUrl)) {
            result = 3;
        } else {
            result = 0;
        }

        return result;
    }
}
