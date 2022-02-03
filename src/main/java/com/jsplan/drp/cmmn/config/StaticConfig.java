package com.jsplan.drp.cmmn.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @Class : StaticConfig
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : static 변수 설정
 */
@Component
public class StaticConfig {

    public static String DEFAULT_ENCODING; // 기본 인코딩
    public static String UPLOAD_PATH; // 파일 업로드 경로
    public static MessageSource MESSAGE_SOURCE; // 다국어 처리
    public static LocaleResolver LOCALE_RESOLVER; // locale 처리

    @Resource
    private MessageSource messageSource;
    @Resource
    private LocaleResolver localeResolver;

    @Value("${server.servlet.encoding.charset}")
    private void setDefaultEncoding(String defaultEncoding) { DEFAULT_ENCODING = defaultEncoding; }

    @Value("${project.path.upload}")
    private void setUploadPath(String uploadPath) {
        UPLOAD_PATH = uploadPath;
    }

    @PostConstruct
    private void setMessageSource() {
        MESSAGE_SOURCE = this.messageSource;
    }

    @PostConstruct
    private void setLocaleResolver() {
        LOCALE_RESOLVER = this.localeResolver;
    }
}
