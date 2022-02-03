package com.jsplan.drp.cmmn.ctx;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @Class : UserSessionExpiredStrategy
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 세션 만료 시 처리
 */

public class UserSessionExpiredStrategy implements SessionInformationExpiredStrategy {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String expiredUrl;
    private final RedirectStrategy redirectStrategy;

    /**
     * <p>세션 만료 처리 생성자</p>
     *
     * @param expiredUrl (세션 만료 시 이동 URL)
     */
    public UserSessionExpiredStrategy(String expiredUrl) {
        this(expiredUrl, new DefaultRedirectStrategy());
    }

    /**
     * <p>세션 만료 처리 생성자</p>
     *
     * @param expiredUrl              (세션 만료 시 이동 URL)
     * @param defaultRedirectStrategy (기본 Redirect 설정 방식)
     */
    public UserSessionExpiredStrategy(String expiredUrl,
        DefaultRedirectStrategy defaultRedirectStrategy) {
        this.expiredUrl = expiredUrl;
        this.redirectStrategy = defaultRedirectStrategy;
    }

    /**
     * <p>세션 만료 시 처리</p>
     *
     * @param event (세션 만료 이벤트 정보)
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
        throws IOException, ServletException {
        try {
            HttpServletResponse response = event.getResponse();
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            String sb = "<script type='text/javascript'>"
                + "	alert('다른 곳에서 동일한 계정으로 로그인 되었습니다.');"
                + "	top.location.href='" + expiredUrl + "';"
                + "</script>";

            response.getWriter().print(sb);
            response.flushBuffer();

            logger.info("Redirecting to '" + expiredUrl + "'");
            logger.info("redirectStrategy : " + redirectStrategy.toString());
            // response 객체의 flush 후 redirect 가 불가능하여 javascript 로 임시 처리
//			redirectStrategy.sendRedirect(event.getRequest(), response, expiredUrl);
        } catch (Exception e) {
            logger.error("{}", e);
        }
    }
}
