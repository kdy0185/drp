package com.jsplan.drp.global.config;

import com.jsplan.drp.global.bean.AvailableRoleHierarchy;
import com.jsplan.drp.global.bean.HierarchyStringsFactoryBean;
import com.jsplan.drp.global.bean.ReloadableFilterInvocationSecurityMetadataSource;
import com.jsplan.drp.global.bean.UrlAuthMapFactoryBean;
import com.jsplan.drp.global.ctx.UserSessionExpiredStrategy;
import com.jsplan.drp.global.filter.AjaxSessionTimeoutFilter;
import com.jsplan.drp.global.filter.RememberMeConcurrentSessionFilter;
import com.jsplan.drp.global.hdlr.UserAccessDeniedHandler;
import com.jsplan.drp.global.hdlr.UserAuthFailureHandler;
import com.jsplan.drp.global.hdlr.UserAuthSuccessHandler;
import com.jsplan.drp.global.obj.service.ComsService;
import com.jsplan.drp.global.obj.service.UserService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @Class : SecurityConfig
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : Security ??????
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * <p>WebSecurity ??????</p>
     *
     * @param web (WebSecurity ??????)
     * @throws Exception throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // web.ignoring().antMatchers("/static/**"); // Spring Security ?????? URL
    }

    /**
     * <p>HttpSecurity ??????</p>
     *
     * @param http (HttpSecurity ??????)
     * @throws Exception throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().disable() // HTTP Response Headers ?????? ??????
            .csrf() // CSRF ??????
            .ignoringAntMatchers("/ex/ex/exTestList.do") // ?????? ????????? ??????
            .and()
            .authorizeRequests()
            .antMatchers("/coms/**").authenticated() // ?????? ???????????? ?????? ?????? ??????
            .and()
            .anonymous().authorities("ANONYMOUS") // ?????? ?????? (ANONYMOUS) ??????
            .and()
            .exceptionHandling().accessDeniedHandler(userAccessDeniedHandler()) // ?????? ?????? handler ??????
            .and()
            .sessionManagement()
            .sessionAuthenticationStrategy(sessionAuthenticationStrategy()) // ?????? ?????? ?????? ?????? ??????
            .and()
            .httpBasic().authenticationEntryPoint(authenticationEntryPoint()) // ?????? Entry Point ??????
            .and()
            .addFilterAt(concurrentSessionFilter(),
                ConcurrentSessionFilter.class) // ????????? ?????? filter ?????? : 3. concurrentSessionFilter
            .addFilterAt(logoutFilter(), LogoutFilter.class) // ????????? ?????? filter ?????? : 6. logoutFilter
            .addFilterAt(usernamePasswordAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class) // ????????? ?????? filter ?????? : 10. usernamePasswordAuthenticationFilter
            .addFilterAt(rememberMeAuthenticationFilter(),
                RememberMeAuthenticationFilter.class) // ????????? ?????? filter ?????? : 14. rememberMeAuthenticationFilter
            .addFilterBefore(exceptionTranslationFilter(),
                ExceptionTranslationFilter.class) // ????????? ?????? filter ?????? : 17. exceptionTranslationFilter
            .addFilterAfter(ajaxSessionTimeoutFilter(),
                ExceptionTranslationFilter.class) // ????????? ?????? filter ?????? : 17. ajaxSessionTimeoutFilter
            .addFilterBefore(filterSecurityInterceptor(),
                FilterSecurityInterceptor.class) // ????????? ?????? filter ?????? : 18. filterSecurityInterceptor
        ;
        if ("local".equals(profiles)) {
            http.csrf().disable(); // ?????? ???????????? CSRF ?????? ??????
        }
        if ("dev".equals(profiles)) {
            http.csrf().disable(); // ?????? ???????????? CSRF ?????? ??????
        }
        if ("real".equals(profiles)) {
            http.requiresChannel().antMatchers("/**")
                .requiresSecure(); // ?????? ???????????? SSL ?????? (https + ?????? ??????)
        }
    }

    /**
     * <p>AuthenticationManagerBuilder ?????? (?????? ??????)</p>
     *
     * @param auth (AuthenticationManagerBuilder ??????)
     * @throws Exception throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(rememberMeAuthenticationProvider()).eraseCredentials(false)
            .authenticationEventPublisher(defaultAuthenticationEventPublisher())
            .userDetailsService(userService())
            .passwordEncoder(passwordEncoder());
    }

    /**
     * <p>?????? event publisher ??????</p>
     *
     * @return defaultAuthenticationEventPublisher (DefaultAuthenticationEventPublisher ??????)
     */
    @Bean
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    /**
     * <p>?????? ?????? handler ??????</p>
     *
     * @return UserAccessDeniedHandler (UserAccessDeniedHandler ??????)
     */
    @Bean
    public UserAccessDeniedHandler userAccessDeniedHandler() {
        return new UserAccessDeniedHandler();
    }

    /**
     * <p>???????????? ?????? ?????? bean ??????</p>
     *
     * @return SessionRegistryImpl (SessionRegistryImpl ??????)
     */
    @Bean
    public SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * <p>?????? ?????? ?????? ?????? - ?????? ?????? ??????</p>
     *
     * @return ConcurrentSessionControlAuthenticationStrategy
     * (ConcurrentSessionControlAuthenticationStrategy ??????)
     */
    @Bean
    public ConcurrentSessionControlAuthenticationStrategy concurentSessionStrategy() {
        ConcurrentSessionControlAuthenticationStrategy concurentSessionStrategy =
            new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        concurentSessionStrategy.setMaximumSessions(1); // ?????? ?????? ?????? ???
        concurentSessionStrategy.setExceptionIfMaximumExceeded(
            false); // false : ?????? ????????? ??? ????????? ?????? / true : ????????? ????????? ??? ????????? ??????
        return concurentSessionStrategy;
    }

    /**
     * <p>?????? ?????? ?????? ?????? - ?????? ??? ?????? ?????? ?????? ??????</p>
     *
     * @return SessionFixationProtectionStrategy (SessionFixationProtectionStrategy ??????)
     */
    @Bean
    public SessionFixationProtectionStrategy sessionFixationProtectionStrategy() {
        SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();
        sessionFixationProtectionStrategy.setMigrateSessionAttributes(false);
        return sessionFixationProtectionStrategy;
    }

    /**
     * <p>?????? ?????? ?????? ?????? - sessionRegistry ?????? ??????</p>
     *
     * @return RegisterSessionAuthenticationStrategy (RegisterSessionAuthenticationStrategy ??????)
     */
    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    /**
     * <p>?????? ?????? ?????? ?????? ??????</p>
     *
     * @return CompositeSessionAuthenticationStrategy (CompositeSessionAuthenticationStrategy ??????)
     */
    @Bean
    public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {
        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
        delegateStrategies.add(concurentSessionStrategy()); // ?????? ?????? ??????
        delegateStrategies.add(sessionFixationProtectionStrategy()); // ?????? ??? ?????? ?????? ?????? ??????
        delegateStrategies.add(registerSessionAuthenticationStrategy()); // sessionRegistry ?????? ??????
        return new CompositeSessionAuthenticationStrategy(delegateStrategies);
    }

    /**
     * <p>?????? Entry Point ??????</p>
     *
     * @return LoginUrlAuthenticationEntryPoint (LoginUrlAuthenticationEntryPoint ??????)
     */
    @Bean
    public LoginUrlAuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/main/login/login.do"); // ????????? URL ??????
    }

    /**
     * <p>comsService bean ??????</p>
     *
     * @return ComsService (ComsService ??????)
     */
    @Bean
    public ComsService comsService() {
        return new ComsService();
    }

    /**
     * <p>userService bean ??????</p>
     *
     * @return UserService (UserService ??????)
     */
    @Bean
    public UserService userService() {
        return new UserService();
    }

    /**
     * <p>?????? ????????? - ?????? ?????? ?????? bean ??????</p>
     *
     * @return HierarchyStringsFactoryBean (HierarchyStringsFactoryBean ??????)
     */
    @Bean
    public HierarchyStringsFactoryBean hierarchyStrings() {
        HierarchyStringsFactoryBean hierarchyStrings = new HierarchyStringsFactoryBean();
        hierarchyStrings.setComsService(comsService());
        return hierarchyStrings;
    }

    /**
     * <p>?????? ????????? - ?????? ?????? ??????</p>
     *
     * @return RoleHierarchyImpl (RoleHierarchyImpl ??????)
     * @throws Exception throws Exception
     */
    @Bean
    public RoleHierarchyImpl roleHierarchy() throws Exception {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(hierarchyStrings().getObject());
        return roleHierarchy;
    }

    /**
     * <p>?????? ????????? - ?????? ??? ??????</p>
     *
     * @return AvailableRoleHierarchy (AvailableRoleHierarchy ??????)
     * @throws Exception throws Exception
     */
    @Bean
    public AvailableRoleHierarchy availableRoleHierarchy() throws Exception {
        AvailableRoleHierarchy availableRoleHierarchy = new AvailableRoleHierarchy(roleHierarchy());
        availableRoleHierarchy.setComsService(comsService());
        return availableRoleHierarchy;
    }

    /* ============================ 3. concurrentSessionFilter : S ============================ */

    /**
     * <p>?????? ?????? ?????? Filter ??????</p>
     *
     * @return ConcurrentSessionFilter (ConcurrentSessionFilter ??????)
     */
    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter() {
        // logout handler ??????
        LogoutHandler[] handlers = new LogoutHandler[3];
        handlers[0] = rememberMeServices();
        handlers[1] = securityContextLogoutHandler();
        handlers[2] = cookieClearingLogoutHandler();

        // ?????? ?????? ?????? Filter ??????
        ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(
            sessionRegistry(), sessionInformationExpiredStrategy());
        concurrentSessionFilter.setLogoutHandlers(handlers);
        return concurrentSessionFilter;
    }

    /**
     * <p>?????? ?????? ??? ?????? ?????? ??????</p>
     *
     * @return UserSessionExpiredStrategy (UserSessionExpiredStrategy ??????)
     */
    @Bean
    public UserSessionExpiredStrategy sessionInformationExpiredStrategy() {
        return new UserSessionExpiredStrategy("/main/login/login.do"); // ?????? ?????? ??? ?????? URL
    }
    /* ============================ 3. concurrentSessionFilter : E ============================ */

    /* ============================ 6. logoutFilter : S ============================ */

    /**
     * <p>???????????? Filter ??????</p>
     *
     * @return LogoutFilter (LogoutFilter ??????)
     */
    @Bean
    public LogoutFilter logoutFilter() {
        // logout handler ??????
        LogoutHandler[] handlers = new LogoutHandler[3];
        handlers[0] = rememberMeServices();
        handlers[1] = securityContextLogoutHandler();
        handlers[2] = cookieClearingLogoutHandler();

        // ???????????? Filter ??????
        LogoutFilter logoutFilter = new LogoutFilter(logoutSuccessHandler(), handlers);
        logoutFilter.setFilterProcessesUrl("/main/login/logout.do");
        return logoutFilter;
    }

    /**
     * <p>logoutFilter : ???????????? ?????? Handler ??????</p>
     *
     * @return SimpleUrlLogoutSuccessHandler (SimpleUrlLogoutSuccessHandler ??????)
     */
    @Bean
    public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl("/main/login/login.do"); // ???????????? ??? ????????? URL
        return logoutSuccessHandler;
    }

    /**
     * <p>logoutFilter : ???????????? ??? ?????? ??????</p>
     *
     * @return SecurityContextLogoutHandler (SecurityContextLogoutHandler ??????)
     */
    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.setClearAuthentication(true);
        securityContextLogoutHandler.setInvalidateHttpSession(true);
        return securityContextLogoutHandler;
    }

    /**
     * <p>logoutFilter : ?????? ?????? ??????</p>
     *
     * @return CookieClearingLogoutHandler (CookieClearingLogoutHandler ??????)
     */
    @Bean
    public CookieClearingLogoutHandler cookieClearingLogoutHandler() {
        String[] cookiesToClear = new String[2];
        cookiesToClear[0] = "JSESSIONID";
        cookiesToClear[1] = "rememberMeCookie";

        return new CookieClearingLogoutHandler(cookiesToClear);
    }
    /* ============================ 6. logoutFilter : E ============================ */

    /* ============================ 10. usernamePasswordAuthenticationFilter : S ============================ */

    /**
     * <p>?????? Filter ??????</p>
     *
     * @return UsernamePasswordAuthenticationFilter (UsernamePasswordAuthenticationFilter ??????)
     * @throws Exception throws Exception
     */
    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter()
        throws Exception {
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setUsernameParameter("userId");
        usernamePasswordAuthenticationFilter.setPasswordParameter("userPw");
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/main/login/loginProc.do");
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(
            userAuthSuccessHandler());
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(
            userAuthFailureHandler());
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        usernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(
            sessionAuthenticationStrategy());
        usernamePasswordAuthenticationFilter.setRememberMeServices(rememberMeServices());
        return usernamePasswordAuthenticationFilter;
    }

    /**
     * <p>????????? ?????? Handler ??????</p>
     *
     * @return UserAuthSuccessHandler (UserAuthSuccessHandler ??????)
     */
    @Bean
    public UserAuthSuccessHandler userAuthSuccessHandler() {
        UserAuthSuccessHandler userAuthSuccessHandler = new UserAuthSuccessHandler();
        userAuthSuccessHandler.setTargetUrlParameter("loginUrl");
        userAuthSuccessHandler.setUseReferer(false);
        userAuthSuccessHandler.setDefaultUrl("/main/main/main.do"); // ????????? ?????? ??? ????????? ?????? URL
        return userAuthSuccessHandler;
    }

    /**
     * <p>????????? ?????? Handler ??????</p>
     *
     * @return UserAuthFailureHandler (UserAuthFailureHandler ??????)
     */
    @Bean
    public UserAuthFailureHandler userAuthFailureHandler() {
        UserAuthFailureHandler userAuthFailureHandler = new UserAuthFailureHandler();
        userAuthFailureHandler.setUserId("userId");
        userAuthFailureHandler.setUserPw("userPw");
        userAuthFailureHandler.setLoginUrl("loginUrl");
        userAuthFailureHandler.setExMsg("secExMsg");
        userAuthFailureHandler.setDefaultFailureUrl(
            "/main/login/login.do?failure=true"); // ????????? ?????? ??? ????????? ?????? URL
        return userAuthFailureHandler;
    }

    /**
     * <p>?????? ?????? bean ??????</p>
     *
     * @return AuthenticationManager (AuthenticationManager ??????)
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * <p>BCrypt ????????? ???????????? ??????</p>
     *
     * @return PasswordEncoder (PasswordEncoder ??????)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    /* ============================ 10. usernamePasswordAuthenticationFilter : E ============================ */

    /* ============================ 14. rememberMeAuthenticationFilter : S ============================ */

    /**
     * <p>?????? ????????? Filter ?????? (?????? ?????? ?????? ????????? ?????? ?????? Filter ??????)</p>
     *
     * @return RememberMeConcurrentSessionFilter (RememberMeConcurrentSessionFilter ??????)
     * @throws Exception throws Exception
     */
    @Bean
    public RememberMeConcurrentSessionFilter rememberMeAuthenticationFilter() throws Exception {
        RememberMeConcurrentSessionFilter rememberMeAuthenticationFilter = new RememberMeConcurrentSessionFilter(
            authenticationManager(), rememberMeServices());
        rememberMeAuthenticationFilter.setAuthenticationSuccessHandler(userAuthSuccessHandler());
        rememberMeAuthenticationFilter.setSessionAuthenticationStrategy(
            sessionAuthenticationStrategy());
        return rememberMeAuthenticationFilter;
    }

    /**
     * <p>?????? ????????? ?????? ??????</p>
     *
     * @return TokenBasedRememberMeServices (TokenBasedRememberMeServices ??????)
     */
    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
            "autoLoginKey", userService());
        rememberMeServices.setCookieName("rememberMeCookie");
        rememberMeServices.setParameter("autoLogin");
        rememberMeServices.setTokenValiditySeconds(604800); // ?????? ?????? : 7??? (604800=60*60*24*7)
        return rememberMeServices;
    }

    /**
     * <p>?????? ?????? ??? ?????? ????????? ??????</p>
     *
     * @return RememberMeAuthenticationProvider (RememberMeAuthenticationProvider ??????)
     */
    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider("autoLoginKey");
    }
    /* ============================ 14. rememberMeAuthenticationFilter : E ============================ */

    /* ============================ 17. exceptionTranslationFilter : S ============================ */

    /**
     * <p>?????? ?????? Filter ??????</p>
     *
     * @return ExceptionTranslationFilter (ExceptionTranslationFilter ??????)
     */
    @Bean
    public ExceptionTranslationFilter exceptionTranslationFilter() {
        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(
            authenticationEntryPoint());
        exceptionTranslationFilter.setAccessDeniedHandler(userAccessDeniedHandler());
        return exceptionTranslationFilter;
    }
    /* ============================ 17. exceptionTranslationFilter : E ============================ */

    /* ============================ 17. ajaxSessionTimeoutFilter : S ============================ */

    /**
     * <p>ajax ?????? ??? ????????? ?????? Filter ??????</p>
     *
     * @return AjaxSessionTimeoutFilter (AjaxSessionTimeoutFilter ??????)
     */
    @Bean
    public AjaxSessionTimeoutFilter ajaxSessionTimeoutFilter() {
        AjaxSessionTimeoutFilter ajaxSessionTimeoutFilter = new AjaxSessionTimeoutFilter();
        ajaxSessionTimeoutFilter.setAjaxHeader("AJAX");
        return ajaxSessionTimeoutFilter;
    }
    /* ============================ 17. ajaxSessionTimeoutFilter : E ============================ */

    /* ============================ 18. filterSecurityInterceptor : S ============================ */

    /**
     * <p>Security Interceptor Filter ??????</p>
     *
     * @return FilterSecurityInterceptor (FilterSecurityInterceptor ??????)
     * @throws Exception throws Exception
     */
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setSecurityMetadataSource(
            reloadableFilterInvocationSecurityMetadataSource());
        return filterSecurityInterceptor;
    }

    /**
     * <p>?????? ?????? ?????? ??????</p>
     *
     * @return RoleVoter (RoleVoter ??????)
     */
    @Bean
    public RoleVoter roleVoter() {
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix(""); // ?????? ?????? ????????? ?????? ??????
        return roleVoter;
    }

    /**
     * <p>filterSecurityInterceptor : ?????? ?????? ??????</p>
     *
     * @return AffirmativeBased (AffirmativeBased ??????)
     */
    @Bean
    public AffirmativeBased accessDecisionManager() {
        // ?????? ?????? ?????? ??????
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(roleVoter());

        // ?????? ?????? ??????
        AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
        accessDecisionManager.setAllowIfAllAbstainDecisions(false);
        return accessDecisionManager;
    }

    /**
     * <p>URL??? ?????? ?????? bean ??????</p>
     *
     * @return UrlAuthMapFactoryBean (UrlAuthMapFactoryBean ??????)
     */
    @Bean
    public UrlAuthMapFactoryBean urlAuthMapFactoryBean() {
        UrlAuthMapFactoryBean urlAuthMapFactoryBean = new UrlAuthMapFactoryBean();
        urlAuthMapFactoryBean.setComsService(comsService());
        return urlAuthMapFactoryBean;
    }

    /**
     * <p>filterSecurityInterceptor : URL??? ?????? ?????? ??????</p>
     *
     * @return ReloadableFilterInvocationSecurityMetadataSource
     * (ReloadableFilterInvocationSecurityMetadataSource ??????)
     * @throws Exception throws Exception
     */
    @Bean
    public ReloadableFilterInvocationSecurityMetadataSource reloadableFilterInvocationSecurityMetadataSource()
        throws Exception {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = urlAuthMapFactoryBean().getObject();
        ReloadableFilterInvocationSecurityMetadataSource reloadableFilterInvocationSecurityMetadataSource =
            new ReloadableFilterInvocationSecurityMetadataSource(requestMap);
        reloadableFilterInvocationSecurityMetadataSource.setComsService(comsService());
        return reloadableFilterInvocationSecurityMetadataSource;
    }
    /* ============================ 18. filterSecurityInterceptor : E ============================ */

}
