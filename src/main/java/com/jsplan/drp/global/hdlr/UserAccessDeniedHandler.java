package com.jsplan.drp.global.hdlr;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

/**
 * @Class : UserAccessDeniedHandler
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 접근 거부 시 Handler
 */
public class UserAccessDeniedHandler extends AccessDeniedHandlerImpl {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * <p>접근 거부(403) 에러 발생 시 로그인 페이지로 redirect</p>
     *
     * @param request               (HttpServletRequest 객체)
     * @param response              (HttpServletResponse 객체)
     * @param accessDeniedException (AccessDeniedException 객체)
     * @throws IOException      throws IOException
     * @throws ServletException throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (accessDeniedException instanceof MissingCsrfTokenException) {
            requestCache.saveRequest(request, response);
            response.sendRedirect(request.getContextPath() + "/main/login/login.do");
        }

        super.handle(request, response, accessDeniedException);
    }
}
