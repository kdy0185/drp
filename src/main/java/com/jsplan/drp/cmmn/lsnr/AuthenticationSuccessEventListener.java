package com.jsplan.drp.cmmn.lsnr;

import com.jsplan.drp.cmmn.obj.LoginAttemptService;
import javax.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * @Class : AuthenticationSuccessEventListener
 * @Author : KDW
 * @Date : 2022-01-22
 * @Description : 인증 성공 Listener
 */
@Component
public class AuthenticationSuccessEventListener implements
    ApplicationListener<AuthenticationSuccessEvent> {

    @Resource
    private LoginAttemptService loginAttemptService;

    /**
     * <p>인증 성공 시 event 설정</p>
     *
     * @param e (인증 성공 Event 객체)
     */
    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails) e.getAuthentication()
            .getDetails();
        loginAttemptService.loginSucceeded(auth.getRemoteAddress());
    }
}
