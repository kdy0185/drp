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
 * @Description : Security 설정
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.profiles.active}")
	private String profiles;

	/**
	 * <p>WebSecurity 설정</p>
	 * 
	 * @param web (WebSecurity 객체)
	 * @throws Exception throws Exception
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		// web.ignoring().antMatchers("/static/**"); // Spring Security 제외 URL
	}

	/**
	 * <p>HttpSecurity 설정</p>
	 * 
	 * @param http (HttpSecurity 객체)
	 * @throws Exception throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.headers().disable() // HTTP Response Headers 기능 제거
			.csrf() // CSRF 적용
				.ignoringAntMatchers("/ex/ex/exTestList.do") // 특정 페이지 제외
			.and()
			.authorizeRequests()
				.antMatchers("/coms/**").authenticated() // 공통 프로그램 접근 권한 설정
			.and()
			.anonymous().authorities("ANONYMOUS") // 익명 권한 (ANONYMOUS) 설정
			.and()
			.exceptionHandling().accessDeniedHandler(userAccessDeniedHandler()) // 접근 거부 handler 설정
			.and()
			.sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy()) // 동시 세션 처리 전략 설정
			.and()
			.httpBasic().authenticationEntryPoint(authenticationEntryPoint()) // 인증 Entry Point 지정
			.and()
			.addFilterAt(concurrentSessionFilter(), ConcurrentSessionFilter.class) // 사용자 정의 filter 추가 : 3. concurrentSessionFilter
			.addFilterAt(logoutFilter(), LogoutFilter.class) // 사용자 정의 filter 추가 : 6. logoutFilter
			.addFilterAt(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 사용자 정의 filter 추가 : 10. usernamePasswordAuthenticationFilter
			.addFilterAt(rememberMeAuthenticationFilter(), RememberMeAuthenticationFilter.class) // 사용자 정의 filter 추가 : 14. rememberMeAuthenticationFilter
			.addFilterBefore(exceptionTranslationFilter(), ExceptionTranslationFilter.class) // 사용자 정의 filter 추가 : 17. exceptionTranslationFilter
			.addFilterAfter(ajaxSessionTimeoutFilter(), ExceptionTranslationFilter.class) // 사용자 정의 filter 추가 : 17. ajaxSessionTimeoutFilter
			.addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class) // 사용자 정의 filter 추가 : 18. filterSecurityInterceptor
		;
		if ("local".equals(profiles)) {
			http.csrf().disable(); // 로컬 환경에서 CSRF 기능 제거
		}
		if ("dev".equals(profiles)) {
			http.csrf().disable(); // 개발 환경에서 CSRF 기능 제거
		}
		if ("real".equals(profiles)) {
			http.requiresChannel().antMatchers("/**").requiresSecure(); // 운영 환경에서 SSL 적용 (https + 포트 전환)
		}
	}

	/**
	 * <p>AuthenticationManagerBuilder 설정 (인증 관리)</p>
	 * 
	 * @param auth (AuthenticationManagerBuilder 객체)
	 * @throws Exception throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(rememberMeAuthenticationProvider()).eraseCredentials(false)
				.authenticationEventPublisher(defaultAuthenticationEventPublisher()).userDetailsService(userService())
				.passwordEncoder(passwordEncoder());
	}

	/**
	 * <p>기본 event publisher 설정</p>
	 * 
	 * @return defaultAuthenticationEventPublisher (DefaultAuthenticationEventPublisher 객체)
	 */
	@Bean
	DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
		return new DefaultAuthenticationEventPublisher();
	}

	/**
	 * <p>접근 거부 handler 설정</p>
	 * 
	 * @return UserAccessDeniedHandler (UserAccessDeniedHandler 객체)
	 */
	@Bean
	public UserAccessDeniedHandler userAccessDeniedHandler() {
		return new UserAccessDeniedHandler();
	}

	/**
	 * <p>사용자별 세션 관리 bean 설정</p>
	 * 
	 * @return SessionRegistryImpl (SessionRegistryImpl 객체)
	 */
	@Bean
	public SessionRegistryImpl sessionRegistry() {
		return new SessionRegistryImpl();
	}

	/**
	 * <p>동시 세션 처리 전략 - 중복 세션 설정</p>
	 * 
	 * @return ConcurrentSessionControlAuthenticationStrategy (ConcurrentSessionControlAuthenticationStrategy 객체)
	 */
	@Bean
	public ConcurrentSessionControlAuthenticationStrategy concurentSessionStrategy() {
		ConcurrentSessionControlAuthenticationStrategy concurentSessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
		concurentSessionStrategy.setMaximumSessions(1); // 최대 허용 세션 수
		concurentSessionStrategy.setExceptionIfMaximumExceeded(false); // false : 먼저 로그인 한 사용자 차단 / true : 나중에 로그인 한 사용자 차단
		return concurentSessionStrategy;
	}

	/**
	 * <p>동시 세션 처리 전략 - 인증 후 세션 갱신 여부 설정</p>
	 * 
	 * @return SessionFixationProtectionStrategy (SessionFixationProtectionStrategy 객체)
	 */
	@Bean
	public SessionFixationProtectionStrategy sessionFixationProtectionStrategy() {
		SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();
		sessionFixationProtectionStrategy.setMigrateSessionAttributes(false);
		return sessionFixationProtectionStrategy;
	}

	/**
	 * <p>동시 세션 처리 전략 - sessionRegistry 연동 설정</p>
	 * 
	 * @return RegisterSessionAuthenticationStrategy (RegisterSessionAuthenticationStrategy 객체)
	 */
	@Bean
	public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}

	/**
	 * <p>동시 세션 처리 전략 설정</p>
	 * 
	 * @return CompositeSessionAuthenticationStrategy (CompositeSessionAuthenticationStrategy 객체)
	 */
	@Bean
	public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {
		List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
		delegateStrategies.add(concurentSessionStrategy()); // 중복 세션 설정
		delegateStrategies.add(sessionFixationProtectionStrategy()); // 인증 후 세션 갱신 여부 설정
		delegateStrategies.add(registerSessionAuthenticationStrategy()); // sessionRegistry 연동 설정
		return new CompositeSessionAuthenticationStrategy(delegateStrategies);
	}

	/**
	 * <p>인증 Entry Point 설정</p>
	 * 
	 * @return LoginUrlAuthenticationEntryPoint (LoginUrlAuthenticationEntryPoint 객체)
	 */
	@Bean
	public LoginUrlAuthenticationEntryPoint authenticationEntryPoint() {
		return new LoginUrlAuthenticationEntryPoint("/main/login/login.do"); // 로그인 URL 설정
	}

	/**
	 * <p>comsService bean 설정</p>
	 * 
	 * @return ComsService (ComsService 객체)
	 */
	@Bean
	public ComsService comsService() {
		return new ComsService();
	}

	/**
	 * <p>userService bean 설정</p>
	 * 
	 * @return UserService (UserService 객체)
	 */
	@Bean
	public UserService userService() {
		return new UserService();
	}

	/**
	 * <p>권한 계층화 - 권한 목록 조회 bean 설정</p>
	 * 
	 * @return HierarchyStringsFactoryBean (HierarchyStringsFactoryBean 객체)
	 */
	@Bean
	public HierarchyStringsFactoryBean hierarchyStrings() {
		HierarchyStringsFactoryBean hierarchyStrings = new HierarchyStringsFactoryBean();
		hierarchyStrings.setComsService(comsService());
		return hierarchyStrings;
	}

	/**
	 * <p>권한 계층화 - 계층 권한 정의</p>
	 * 
	 * @return RoleHierarchyImpl (RoleHierarchyImpl 객체)
	 * @throws Exception throws Exception 
	 */
	@Bean
	public RoleHierarchyImpl roleHierarchy() throws Exception {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy(hierarchyStrings().getObject());
		return roleHierarchy;
	}

	/**
	 * <p>권한 계층화 - 설정 및 적용</p>
	 * 
	 * @return AvailableRoleHierarchy (AvailableRoleHierarchy 객체)
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
	 * <p>동시 세션 제어 Filter 설정</p>
	 * 
	 * @return ConcurrentSessionFilter (ConcurrentSessionFilter 객체)
	 */
	@Bean
	public ConcurrentSessionFilter concurrentSessionFilter() {
		// logout handler 설정
		LogoutHandler[] handlers = new LogoutHandler[3];
		handlers[0] = rememberMeServices();
		handlers[1] = securityContextLogoutHandler();
		handlers[2] = cookieClearingLogoutHandler();

		// 동시 세션 제어 Filter 설정
		ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry(), sessionInformationExpiredStrategy());
		concurrentSessionFilter.setLogoutHandlers(handlers);
		return concurrentSessionFilter;
	}

	/**
	 * <p>세션 만료 시 처리 옵션 설정</p>
	 * 
	 * @return UserSessionExpiredStrategy (UserSessionExpiredStrategy 객체)
	 */
	@Bean
	public UserSessionExpiredStrategy sessionInformationExpiredStrategy() {
		return new UserSessionExpiredStrategy("/main/login/login.do"); // 세션 만료 시 이동 URL
	}
	/* ============================ 3. concurrentSessionFilter : E ============================ */

	/* ============================ 6. logoutFilter : S ============================ */
	/**
	 * <p>로그아웃 Filter 설정</p>
	 * 
	 * @return LogoutFilter (LogoutFilter 객체)
	 */
	@Bean
	public LogoutFilter logoutFilter() {
		// logout handler 설정
		LogoutHandler[] handlers = new LogoutHandler[3];
		handlers[0] = rememberMeServices();
		handlers[1] = securityContextLogoutHandler();
		handlers[2] = cookieClearingLogoutHandler();

		// 로그아웃 Filter 설정
		LogoutFilter logoutFilter = new LogoutFilter(logoutSuccessHandler(), handlers);
		logoutFilter.setFilterProcessesUrl("/main/login/logout.do");
		return logoutFilter;
	}

	/**
	 * <p>logoutFilter : 로그아웃 성공 Handler 설정</p>
	 * 
	 * @return SimpleUrlLogoutSuccessHandler (SimpleUrlLogoutSuccessHandler 객체)
	 */
	@Bean
	public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
		SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
		logoutSuccessHandler.setDefaultTargetUrl("/main/login/login.do"); // 로그아웃 후 이동할 URL
		return logoutSuccessHandler;
	}

	/**
	 * <p>logoutFilter : 로그아웃 시 옵션 설정</p>
	 * 
	 * @return SecurityContextLogoutHandler (SecurityContextLogoutHandler 객체)
	 */
	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
		SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		securityContextLogoutHandler.setClearAuthentication(true);
		securityContextLogoutHandler.setInvalidateHttpSession(true);
		return securityContextLogoutHandler;
	}

	/**
	 * <p>logoutFilter : 쿠키 제거 설정</p>
	 * 
	 * @return CookieClearingLogoutHandler (CookieClearingLogoutHandler 객체)
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
	 * <p>인증 Filter 설정</p>
	 * 
	 * @return UsernamePasswordAuthenticationFilter (UsernamePasswordAuthenticationFilter 객체)
	 * @throws Exception throws Exception 
	 */
	@Bean
	public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
		UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
		usernamePasswordAuthenticationFilter.setUsernameParameter("userId");
		usernamePasswordAuthenticationFilter.setPasswordParameter("userPw");
		usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/main/login/loginProc.do");
		usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(userAuthSuccessHandler());
		usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(userAuthFailureHandler());
		usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
		usernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
		usernamePasswordAuthenticationFilter.setRememberMeServices(rememberMeServices());
		return usernamePasswordAuthenticationFilter;
	}

	/**
	 * <p>로그인 성공 Handler 설정</p>
	 * 
	 * @return UserAuthSuccessHandler (UserAuthSuccessHandler 객체)
	 */
	@Bean
	public UserAuthSuccessHandler userAuthSuccessHandler() {
		UserAuthSuccessHandler userAuthSuccessHandler = new UserAuthSuccessHandler();
		userAuthSuccessHandler.setTargetUrlParameter("loginUrl");
		userAuthSuccessHandler.setUseReferer(false);
		userAuthSuccessHandler.setDefaultUrl("/main/main/main.do"); // 로그인 성공 후 이동할 기본 URL
		return userAuthSuccessHandler;
	}

	/**
	 * <p>로그인 실패 Handler 설정</p>
	 * 
	 * @return UserAuthFailureHandler (UserAuthFailureHandler 객체)
	 */
	@Bean
	public UserAuthFailureHandler userAuthFailureHandler() {
		UserAuthFailureHandler userAuthFailureHandler = new UserAuthFailureHandler();
		userAuthFailureHandler.setUserId("userId");
		userAuthFailureHandler.setUserPw("userPw");
		userAuthFailureHandler.setLoginUrl("loginUrl");
		userAuthFailureHandler.setExMsg("secExMsg");
		userAuthFailureHandler.setDefaultFailureUrl("/main/login/login.do?failure=true"); // 로그인 실패 후 이동할 기본 URL
		return userAuthFailureHandler;
	}

	/**
	 * <p>인증 관리 bean 설정</p>
	 * 
	 * @return AuthenticationManager (AuthenticationManager 객체)
	 */
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	/**
	 * <p>BCrypt 암호화 알고리즘 적용</p>
	 * 
	 * @return PasswordEncoder (PasswordEncoder 객체)
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	/* ============================ 10. usernamePasswordAuthenticationFilter : E ============================ */

	/* ============================ 14. rememberMeAuthenticationFilter : S ============================ */
	/**
	 * <p>자동 로그인 Filter 설정 (동시 세션 제어 설정을 위해 직접 Filter 구현)</p>
	 * 
	 * @return RememberMeConcurrentSessionFilter (RememberMeConcurrentSessionFilter 객체)
	 * @throws Exception throws Exception 
	 */
	@Bean
	public RememberMeConcurrentSessionFilter rememberMeAuthenticationFilter() throws Exception {
		RememberMeConcurrentSessionFilter rememberMeAuthenticationFilter = new RememberMeConcurrentSessionFilter(authenticationManager(), rememberMeServices());
		rememberMeAuthenticationFilter.setAuthenticationSuccessHandler(userAuthSuccessHandler());
		rememberMeAuthenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
		return rememberMeAuthenticationFilter;
	}

	/**
	 * <p>자동 로그인 옵션 설정</p>
	 * 
	 * @return TokenBasedRememberMeServices (TokenBasedRememberMeServices 객체)
	 */
	@Bean
	public TokenBasedRememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("autoLoginKey", userService());
		rememberMeServices.setCookieName("rememberMeCookie");
		rememberMeServices.setParameter("autoLogin");
		rememberMeServices.setTokenValiditySeconds(604800); // 유효 기간 : 7일 (604800=60*60*24*7)
		return rememberMeServices;
	}

	/**
	 * <p>인증 완료 후 자동 로그인 설정</p>
	 * 
	 * @return RememberMeAuthenticationProvider (RememberMeAuthenticationProvider 객체)
	 */
	@Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
		return new RememberMeAuthenticationProvider("autoLoginKey");
	}
	/* ============================ 14. rememberMeAuthenticationFilter : E ============================ */

	/* ============================ 17. exceptionTranslationFilter : S ============================ */
	/**
	 * <p>예외 처리 Filter 설정</p>
	 * 
	 * @return ExceptionTranslationFilter (ExceptionTranslationFilter 객체)
	 */
	@Bean
	public ExceptionTranslationFilter exceptionTranslationFilter() {
		ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(authenticationEntryPoint());
		exceptionTranslationFilter.setAccessDeniedHandler(userAccessDeniedHandler());
		return exceptionTranslationFilter;
	}
	/* ============================ 17. exceptionTranslationFilter : E ============================ */

	/* ============================ 17. ajaxSessionTimeoutFilter : S ============================ */
	/**
	 * <p>ajax 사용 시 로그인 연동 Filter 설정</p>
	 * 
	 * @return AjaxSessionTimeoutFilter (AjaxSessionTimeoutFilter 객체)
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
	 * <p>Security Interceptor Filter 설정</p>
	 * 
	 * @return FilterSecurityInterceptor (FilterSecurityInterceptor 객체)
	 * @throws Exception throws Exception 
	 */
	@Bean
	public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
		FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
		filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
		filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
		filterSecurityInterceptor.setSecurityMetadataSource(reloadableFilterInvocationSecurityMetadataSource());
		return filterSecurityInterceptor;
	}

	/**
	 * <p>권한 부여 기준 설정</p>
	 * 
	 * @return RoleVoter (RoleVoter 객체)
	 */
	@Bean
	public RoleVoter roleVoter() {
		RoleVoter roleVoter = new RoleVoter();
		roleVoter.setRolePrefix(""); // 권한 코드 접두어 설정 제거
		return roleVoter;
	}

	/**
	 * <p>filterSecurityInterceptor : 판단 주체 설정</p>
	 * 
	 * @return AffirmativeBased (AffirmativeBased 객체)
	 */
	@Bean
	public AffirmativeBased accessDecisionManager() {
		// 권한 부여 기준 설정
		List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
		decisionVoters.add(roleVoter());

		// 판단 주체 설정
		AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
		accessDecisionManager.setAllowIfAllAbstainDecisions(false);
		return accessDecisionManager;
	}

	/**
	 * <p>URL별 권한 조회 bean 설정</p>
	 * 
	 * @return UrlAuthMapFactoryBean (UrlAuthMapFactoryBean 객체)
	 */
	@Bean
	public UrlAuthMapFactoryBean urlAuthMapFactoryBean() {
		UrlAuthMapFactoryBean urlAuthMapFactoryBean = new UrlAuthMapFactoryBean();
		urlAuthMapFactoryBean.setComsService(comsService());
		return urlAuthMapFactoryBean;
	}

	/**
	 * <p>filterSecurityInterceptor : URL별 접근 권한 설정</p>
	 * 
	 * @return ReloadableFilterInvocationSecurityMetadataSource (ReloadableFilterInvocationSecurityMetadataSource 객체)
	 * @throws Exception throws Exception 
	 */
	@Bean
	public ReloadableFilterInvocationSecurityMetadataSource reloadableFilterInvocationSecurityMetadataSource() throws Exception {
		LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = urlAuthMapFactoryBean().getObject();
		ReloadableFilterInvocationSecurityMetadataSource reloadableFilterInvocationSecurityMetadataSource = new ReloadableFilterInvocationSecurityMetadataSource(requestMap);
		reloadableFilterInvocationSecurityMetadataSource.setComsService(comsService());
		return reloadableFilterInvocationSecurityMetadataSource;
	}
	/* ============================ 18. filterSecurityInterceptor : E ============================ */

}
