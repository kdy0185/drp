package com.jsplan.drp.global.util;

/**
 * @Class : FilterUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : XSS 및 CSRF 방지 처리
 */
public class FilterUtil {

    /**
     * <p>Script 관련 태그 필터링</p>
     *
     * <pre>
     * FilterUtil.filterScript(null)             = ""
     * FilterUtil.filterScript("")               = ""
     * FilterUtil.filterScript("  ")             = ""
     * FilterUtil.filterScript("&lt;script&gt;") = "&amp;lt;script&gt;"
     * FilterUtil.filterScript("&lt;object&gt;") = "&amp;lt;object&gt;"
     * FilterUtil.filterScript("&lt;applet&gt;") = "&amp;lt;applet&gt;"
     * FilterUtil.filterScript("&lt;embed&gt;")  = "&amp;lt;embed&gt;"
     * FilterUtil.filterScript("&lt;form&gt;")   = "&amp;lt;form&gt;"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String filterScript(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("<([Ss])([Cc])([Rr])([Ii])([Pp])([Tt])", "&lt;script");
        str = str.replaceAll("</([Ss])([Cc])([Rr])([Ii])([Pp])([Tt])", "&lt;/script");
        str = str.replaceAll("<([Oo])([Bb])([Jj])([Ee])([Cc])([Tt])", "&lt;object");
        str = str.replaceAll("</([Oo])([Bb])([Jj])([Ee])([Cc])([Tt])", "&lt;/object");
        str = str.replaceAll("<([Aa])([Pp])([Pp])([Ll])([Ee])([Tt])", "&lt;applet");
        str = str.replaceAll("</([Aa])([Pp])([Pp])([Ll])([Ee])([Tt])", "&lt;/applet");
        str = str.replaceAll("<([Ee])([Mm])([Bb])([Ee])([Dd])", "&lt;embed");
        str = str.replaceAll("</([Ee])([Mm])([Bb])([Ee])([Dd])", "&lt;embed");
        str = str.replaceAll("<([Ff])([Oo])([Rr])([Mm])", "&lt;form");
        str = str.replaceAll("</([Ff])([Oo])([Rr])([Mm])", "&lt;form");
        return str;
    }

    /**
     * <p>XSS 취약 문자 필터링</p>
     *
     * <pre>
     * FilterUtil.filterXss(null)        = ""
     * FilterUtil.filterXss("")          = ""
     * FilterUtil.filterXss("  ")        = ""
     * FilterUtil.filterXss("&amp;|"")   = "&amp;amp;|&amp;quot;"
     * FilterUtil.filterXss("&lt;|&gt;") = "&amp;lt;|&amp;gt;"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String filterXss(Object object) {
        return filterXss(object, false);
    }

    /**
     * <p>XSS 취약 문자 필터링 (개행문자 처리)</p>
     *
     * <pre>
     * FilterUtil.filterXss(null)        = ""
     * FilterUtil.filterXss("")          = ""
     * FilterUtil.filterXss("  ")        = ""
     * FilterUtil.filterXss("&amp;|"")   = "&amp;amp;|&amp;quot;"
     * FilterUtil.filterXss("&lt;|&gt;") = "&amp;lt;|&amp;gt;"
     * </pre>
     *
     * @param object       (변경할 문자형 객체)
     * @param isEncodeLine (개행문자 &lt;br&gt;로 변경여부)
     * @return String (변경 후 문자)
     */
    public static String filterXss(Object object, boolean isEncodeLine) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("'", "&apos;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        if (isEncodeLine) {
            str = HtmlUtil.encodeLine(str);
        }
        return StringUtil.trim(str);
    }

    /**
     * <p>XSS 취약 문자 역필터링</p>
     *
     * <pre>
     * FilterUtil.filterXssReverse(null)        = ""
     * FilterUtil.filterXssReverse("")          = ""
     * FilterUtil.filterXssReverse("  ")        = ""
     * FilterUtil.filterXssReverse("&amp;amp;|&amp;quot;")   = "&amp;|""
     * FilterUtil.filterXssReverse("&amp;lt;|&amp;gt;") = "&lt;|&gt;"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String filterXssReverse(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&nbsp;", " ");
        str = str.replaceAll("&apos;", "'");
        str = str.replaceAll("&quot;", "\"");
        return StringUtil.trim(str);
    }

    /**
     * <p>XSS 취약 문자 ASCII 필터링</p>
     *
     * <pre>
     * FilterUtil.filterXssToAscii(null)        = ""
     * FilterUtil.filterXssToAscii("")          = ""
     * FilterUtil.filterXssToAscii("  ")        = ""
     * FilterUtil.filterXssToAscii("&amp;|'|"") = "&amp;#38;|&amp;#39;|&amp;#34;"
     * FilterUtil.filterXssToAscii("&lt;|&gt;") = "&amp;#60;|&amp;#62;"
     * FilterUtil.filterXssToAscii("(|)")       = "&amp;#40;|&amp;#41;"
     * FilterUtil.filterXssToAscii("%|+")       = "&amp;#37;|&amp;#43;"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String filterXssToAscii(Object object) {
        return filterXssToAscii(object, false);
    }

    /**
     * <p>XSS 취약 문자 ASCII 필터링 (개행문자 처리)</p>
     *
     * <pre>
     * FilterUtil.filterXssToAscii(null)        = ""
     * FilterUtil.filterXssToAscii("")          = ""
     * FilterUtil.filterXssToAscii("  ")        = ""
     * FilterUtil.filterXssToAscii("&amp;|'|"") = "&amp;#38;|&amp;#39;|&amp;#34;"
     * FilterUtil.filterXssToAscii("&lt;|&gt;") = "&amp;#60;|&amp;#62;"
     * FilterUtil.filterXssToAscii("(|)")       = "&amp;#40;|&amp;#41;"
     * FilterUtil.filterXssToAscii("%|+")       = "&amp;#37;|&amp;#43;"
     * </pre>
     *
     * @param object       (변경할 문자형 객체)
     * @param isEncodeLine (개행문자 &lt;br&gt;로 변경여부)
     * @return String (변경 후 문자)
     */
    public static String filterXssToAscii(Object object, boolean isEncodeLine) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("&", "&#38;");
        str = str.replaceAll("'", "&#39;");
        str = str.replaceAll("\"", "&#34;");
        str = str.replaceAll("<", "&#60;");
        str = str.replaceAll(">", "&#62;");
        str = str.replaceAll("\\(", "&#40;");
        str = str.replaceAll("\\)", "&#41;");
        str = str.replaceAll("%", "&#37;");
        str = str.replaceAll("\\+", "&#43;");
        // str = str.replaceAll(";", "&#59;");
        if (isEncodeLine) {
            str = HtmlUtil.encodeLine(str);
        }
        return StringUtil.trim(str);
    }

    /**
     * <p>XSS 취약 문자 제거</p>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String filterXssToEmpty(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = StringUtil.remove(str, "'");
        str = StringUtil.remove(str, "`");
        str = StringUtil.remove(str, "\"");
        str = StringUtil.remove(str, "%");
        str = StringUtil.remove(str, "<");
        str = StringUtil.remove(str, ">");
        str = StringUtil.remove(str, "(");
        str = StringUtil.remove(str, ")");
        str = StringUtil.remove(str, "#");
        str = StringUtil.remove(str, "&");
        str = StringUtil.remove(str, ";");
        str = StringUtil.replaceAll(str, "\\'", "''");
        str = StringUtil.replaceAll(str, "\t'", "' '");
        str = StringUtil.remove(str, "=");
        str = StringUtil.remove(str, "--");
        return StringUtil.trim(str);
    }
}
