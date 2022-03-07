package com.jsplan.drp.global.util;

import com.jsplan.drp.global.config.StaticConfig;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : EncodeUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : Encode, Decode 처리
 */
public class EncodeUtil {

    private static final Logger logger = LoggerFactory.getLogger(EncodeUtil.class);

    /**
     * <p>Application 기본 인코딩</p>
     *
     * @param object (인코딩할 문자형, 숫자형 객체)
     * @return String (인코딩 후 문자)
     */
    public static String encode(Object object) {
        return encode(object, StaticConfig.DEFAULT_ENCODING);
    }

    /**
     * <p>인코딩 타입 적용 후 인코딩</p>
     *
     * @param object   (인코딩할 문자형, 숫자형 객체)
     * @param encoding (인코딩 타입)
     * @return String (인코딩 후 문자)
     */
    public static String encode(Object object, String encoding) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        try {
            return URLEncoder.encode(StringUtil.trim(object), encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error(
                ExceptionUtil.addMessage(e, "URLEncoder.encode 에서 해당 문자의 인코딩을 지원하지 않습니다."));
            return "";
        }
    }

    /**
     * <p>Application 기본 디코딩</p>
     *
     * @param object (디코딩할 문자형, 숫자형 객체)
     * @return String (디코딩 후 문자)
     */
    public static String decode(Object object) {
        return decode(object, StaticConfig.DEFAULT_ENCODING);
    }

    /**
     * <p>디코딩 타입 적용 후 디코딩</p>
     *
     * @param object (디코딩할 문자형, 숫자형 객체)
     * @param decode (디코딩 타입)
     * @return String (디코딩 후 문자)
     */
    public static String decode(Object object, String decode) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        try {
            return URLDecoder.decode(StringUtil.trim(object), decode);
        } catch (UnsupportedEncodingException e) {
            logger.error(
                ExceptionUtil.addMessage(e, "URLEncoder.decode 에서 해당 문자의 디코딩을 지원하지 않습니다."));
            return "";
        }
    }

    /**
     * <p>인코딩 타입 변경</p>
     *
     * @param object    (인코딩할 문자형, 숫자형 객체)
     * @param encoding1 (현재의 인코딩 타입)
     * @param encoding2 (변경할 인코딩 타입)
     * @return String (인코딩 후 문자)
     */
    public static String getBytes(Object object, String encoding1,
        String encoding2) {
        if (object == null) {
            return "";
        }
        String str = String.valueOf(object);
        try {
            return new String(str.getBytes(encoding1), encoding2);
        } catch (UnsupportedEncodingException e) {
            logger.error(ExceptionUtil.addMessage(e, "해당 문자의 인코딩을 지원하지 않습니다."));
            return str;
        }
    }

    /**
     * <p>인코딩 타입 변경 (EUC-KR → 8859_1)</p>
     *
     * @param object (인코딩할 문자형, 숫자형 객체)
     * @return String (인코딩 후 문자)
     */
    public static String toKorean(Object object) {
        return getBytes(object, "EUC-KR", "8859_1");
    }

    /**
     * <p>인코딩 타입 변경 (8859_1 → 기본 인코딩 타입)</p>
     *
     * @param object (인코딩할 문자형, 숫자형 객체)
     * @return String (인코딩 후 문자)
     */
    public static String getBytes(Object object) {
        return getBytes(object, "8859_1", StaticConfig.DEFAULT_ENCODING);
    }
}
