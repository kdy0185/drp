package com.jsplan.drp.global.util;

import com.jsplan.drp.global.config.StaticConfig;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 * @Class : MessageUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 다국어 처리
 */
public class MessageUtil {

    /**
     * <p>properties key를 사용한 메시지 추출</p>
     *
     * @param request (HttpServletRequest 객체)
     * @param code    (message.properties 파일 내 key)
     * @return String (key에 해당하는 value)
     */
    public static String getMessage(HttpServletRequest request, String code) {
        Locale locale = StaticConfig.LOCALE_RESOLVER.resolveLocale(request);
        return StaticConfig.MESSAGE_SOURCE.getMessage(code, null, locale);
    }

    /**
     * <p>properties key와 parameter를 사용한 메시지 추출</p>
     *
     * @param request (HttpServletRequest 객체)
     * @param code    (message.properties 파일 내 key)
     * @param args    (message.properties 파일 내 parameter ("{0}, {1}, …"))
     * @return String (key에 해당하는 value)
     */
    public static String getMessage(HttpServletRequest request, String code, Object[] args) {
        Locale locale = StaticConfig.LOCALE_RESOLVER.resolveLocale(request);
        return StaticConfig.MESSAGE_SOURCE.getMessage(code, args, locale);
    }

    /**
     * <p>properties key를 사용한 메시지 추출</p>
     *
     * @param lang (사용 언어)
     * @param code (message.properties 파일 내 key)
     * @return String (key에 해당하는 value)
     */
    public static String getMessage(String lang, String code) {
        Locale locale = "ko".equals(lang) ? Locale.KOREAN : Locale.ENGLISH;
        return StaticConfig.MESSAGE_SOURCE.getMessage(code, null, locale);
    }

    /**
     * <p>properties key와 parameter를 사용한 메시지 추출</p>
     *
     * @param lang (사용 언어)
     * @param code (message.properties 파일 내 key)
     * @param args (message.properties 파일 내 parameter ("{0}, {1}, …"))
     * @return String (key에 해당하는 value)
     */
    public static String getMessage(String lang, String code, Object[] args) {
        Locale locale = "ko".equals(lang) ? Locale.KOREAN : Locale.ENGLISH;
        return StaticConfig.MESSAGE_SOURCE.getMessage(code, args, locale);
    }
}
