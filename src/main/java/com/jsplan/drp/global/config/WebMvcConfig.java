package com.jsplan.drp.global.config;

import com.jsplan.drp.global.hdlr.UserInterceptor;
import java.util.Locale;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @Class : WebMvcConfig
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : Web 환경 설정
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private UserInterceptor userInterceptor;

    @Value("${spring.web.locale}")
    private Locale locale;

    @Value("${spring.messages.basename}")
    private String basename;

    @Value("${spring.messages.encoding}")
    private String defaultEncoding;

    @Value("${spring.messages.cache-duration}")
    private int cacheDuration;

    /**
     * <p>LocaleChangeInterceptor 설정</p>
     *
     * @return LocaleChangeInterceptor (LocaleChangeInterceptor 객체)
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * <p>SessionLocaleResolver 설정</p>
     *
     * @return LocaleResolver (SessionLocaleResolver 객체)
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(locale);
        return localeResolver;
    }

    /**
     * <p>MessageSource 설정</p>
     *
     * @return ReloadableResourceBundleMessageSource (ReloadableResourceBundleMessageSource 객체)
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding(defaultEncoding);
        messageSource.setCacheSeconds(cacheDuration);
        return messageSource;
    }

    /**
     * <p>Interceptor 등록</p>
     *
     * @param registry (InterceptorRegistry 객체)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/**/*.do")
            .excludePathPatterns("/main/login/login*.do");
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**/*.do");
    }

    /**
     * <p>URL 직접 매핑 설정</p>
     *
     * @param registry (ViewControllerRegistry 객체)
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index"); // index.jsp
    }
}
