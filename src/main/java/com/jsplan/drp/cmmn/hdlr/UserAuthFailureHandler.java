package com.jsplan.drp.cmmn.hdlr;

import com.jsplan.drp.cmmn.util.MessageUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @Class : UserAuthFailureHandler
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 로그인 실패 시 Handler
 */

public class UserAuthFailureHandler implements AuthenticationFailureHandler {

    private String userId;
    private String userPw;
    private String loginUrl;
    private String exMsg;
    private String defaultFailureUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getExMsg() {
        return exMsg;
    }

    public void setExMsg(String exMsg) {
        this.exMsg = exMsg;
    }

    public String getDefaultFailureUrl() {
        return defaultFailureUrl;
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }

    /**
     * <p>로그인 실패 시 Handler</p>
     *
     * @param request   (HttpServletRequest 객체)
     * @param response  (HttpServletResponse 객체)
     * @param exception (AuthenticationException 객체)
     * @throws IOException throws IOException
     * @throws ServletException throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {

        // 이전 입력 값 유지
        request.setAttribute(userId, request.getParameter(userId));
        request.setAttribute(userPw, request.getParameter(userPw));
        request.setAttribute(loginUrl, request.getParameter(loginUrl));

        // Request 객체의 Attribute에 예외 메시지 저장
        String failMsg = exception.getMessage();

        // 데이터 조회 실패 시 메시지 설정
        if (exception instanceof InternalAuthenticationServiceException) {
            failMsg = MessageUtil.getMessage(request, "Authentication.failure.system");
        }

        // 로그인 실패 시 메시지 설정
        if (exception instanceof BadCredentialsException) {
            failMsg = MessageUtil.getMessage(request, "AbstractUserDetailsAuthenticationProvider.badCredentials");
        }

        // 로그인 차단 시 메시지 설정
        if (exception.getMessage().equalsIgnoreCase("blocked")) {
            failMsg = MessageUtil.getMessage(request, "Authentication.failure.blocked");
        }

        request.setAttribute(exMsg, failMsg);

        request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
    }

}
