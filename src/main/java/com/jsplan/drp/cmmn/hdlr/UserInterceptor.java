package com.jsplan.drp.cmmn.hdlr;

import com.jsplan.drp.cmmn.obj.UserVO;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @Class : UserInterceptor
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 인터셉터 설정
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * <p>Controller 동작 전 실행되는 메소드</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @param handler  (Handler 객체)
     * @return boolean (작동 성공 여부)
     * @throws Exception throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        Locale locale = null;

        // 기본 언어 설정 (한국어)
        Locale sessionLocale = sessionLocaleResolver.resolveLocale(request);
        locale = sessionLocale == null ? Locale.KOREA : sessionLocale;

        // 인증된 사용자의 정보 조회
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        String userId = "";

        if (principal != null && principal instanceof UserVO) {
            userId = ((UserVO) principal).getUserId();
        }

        // sessionLocaleResolver 에 언어 설정
        sessionLocaleResolver.setLocale(request, response, locale);

        logger.info(
            "\n\n\n================================          Log Interceptor : S          ================================");
        logger.info("Request URI \t:  " + request.getRequestURI());
        logger.info("Request IP \t:  " + request.getRemoteAddr());
        logger.info("Request ID \t:  " + userId);
        logger.info("Request Locale \t:  " + locale.getLanguage());
        return true;
    }

    /**
     * <p>Controller 동작 후 실행되는 메소드</p>
     *
     * @param request      (HttpServletRequest 객체)
     * @param response     (HttpServletResponse 객체)
     * @param handler      (Handler 객체)
     * @param modelAndView (ModelAndView 객체)
     * @throws Exception throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        logger.info(
            "\n================================          Log Interceptor : E          ================================\n\n");
    }
}
