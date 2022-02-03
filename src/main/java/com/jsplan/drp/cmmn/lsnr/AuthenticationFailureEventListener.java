package com.jsplan.drp.cmmn.lsnr;

import com.jsplan.drp.cmmn.obj.LoginAttemptService;
import javax.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * @Class : AuthenticationFailureEventListener
 * @Author : KDW
 * @Date : 2022-01-22
 * @Description : 인증 실패 Listener
 */
@Component
public class AuthenticationFailureEventListener implements
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Resource
    private LoginAttemptService loginAttemptService;

    /**
     * <p>인증 실패 시 event 설정</p>
     *
     * @param e (인증 실패 Event 객체)
     */
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails) e.getAuthentication()
            .getDetails();
        loginAttemptService.loginFailed(auth.getRemoteAddress());
    }
}
