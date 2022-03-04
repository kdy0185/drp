package com.jsplan.drp.global.util;

/**
 * @Class : HtmlUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : HTML 처리
 */
public class HtmlUtil {

    /**
     * <p>HTML 태그 제거</p>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String deleteHtml(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        str = str.replaceAll("\r\n", "");
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\t", "");
        return str;
    }

    /**
     * <p>개행문자 → &lt;br&gt; 태그 변경</p>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String encodeLine(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("\r\n", "<br />");
        str = str.replaceAll("\r", "<br />");
        str = str.replaceAll("\n", "<br />");
        return str;
    }

    /**
     * <p>&lt;br&gt; → 개행문자 변경</p>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String decodeLine(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("<br>", "\r\n");
        str = str.replaceAll("<br />", "\r\n");
        return str;
    }

    /**
     * <p>HTML 태그 인코딩</p>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String encodeHtml(Object object) {
        return encodeHtml(object, false);
    }

    /**
     * <p>HTML 태그 인코딩 (개행문자 처리)</p>
     *
     * @param object       (변경할 문자형 객체)
     * @param isEncodeLine (개행문자 &lt;br&gt;로 변경여부)
     * @return String (변경 후 문자)
     */
    public static String encodeHtml(Object object, boolean isEncodeLine) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case '&':
                    sb.append("&amp;");
                    break;
                // case '\'' :
                // sb.append("&apos;");
                // break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(str.charAt(i));
            }
        }
        return isEncodeLine ? encodeLine(sb.toString()) : sb.toString();
    }

    /**
     * <p>HTML 태그 디코딩</p>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String decodeHtml(Object object) {
        return decodeHtml(object, false);
    }

    /**
     * <p>HTML 태그 디코딩 (개행문자 처리)</p>
     *
     * @param object       (변경할 문자형 객체)
     * @param isDecodeLine (개행문자 &lt;br&gt;로 변경여부)
     * @return String (변경 후 문자)
     */
    public static String decodeHtml(Object object, boolean isDecodeLine) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = str.replaceAll("&gt;", ">");
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&quot;", "\"");
        str = str.replaceAll("&apos;", "'");
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&nbsp;", " ");
        return isDecodeLine ? decodeLine(str) : str;
    }

    /**
     * <p>같은 요소에 대한 selected 속성 적용</p>
     *
     * <pre>
     * HtmlUtil.getSelected(null, null)   = " selected="selected""
     * HtmlUtil.getSelected(null, "")     = " selected="selected""
     * HtmlUtil.getSelected("", null)     = " selected="selected""
     * HtmlUtil.getSelected("", "")       = " selected="selected""
     * HtmlUtil.getSelected(null, *)      = ""
     * HtmlUtil.getSelected(*, null)      = ""
     * HtmlUtil.getSelected("  ", "")     = ""
     * HtmlUtil.getSelected(" a ", "a")   = ""
     * HtmlUtil.getSelected("abc", "abc") = " selected="selected""
     * </pre>
     *
     * @param object1 (체크할 문자형, 숫자형 객체 1)
     * @param object2 (체크할 문자형, 숫자형 객체 2)
     * @return String ("" 또는 selected 구문)
     */
    public static String getSelected(Object object1, Object object2) {
        if (StringUtil.isEmpty(object1) && StringUtil.isEmpty(object2)) {
            return " selected=\"selected\"";
        }
        if (object1 == null) {
            return "";
        }
        if (object2 == null) {
            return "";
        }
        String str1 = String.valueOf(object1);
        String str2 = String.valueOf(object2);
        if (str1.equals(str2)) {
            return " selected=\"selected\"";
        }
        return "";
    }

    /**
     * <p>같은 요소에 대한 checked 속성 적용</p>
     *
     * <pre>
     * HtmlUtil.getChecked(null, null)   = " checked="checked""
     * HtmlUtil.getChecked(null, "")     = " checked="checked""
     * HtmlUtil.getChecked("", null)     = " checked="checked""
     * HtmlUtil.getChecked("", "")       = " checked="checked""
     * HtmlUtil.getChecked(null, *)      = ""
     * HtmlUtil.getChecked(*, null)      = ""
     * HtmlUtil.getChecked("  ", "")     = ""
     * HtmlUtil.getChecked(" a ", "a")   = ""
     * HtmlUtil.getChecked("abc", "abc") = " checked="checked""
     * </pre>
     *
     * @param object1 (체크할 문자형, 숫자형 객체 1)
     * @param object2 (체크할 문자형, 숫자형 객체 2)
     * @return String ("" 또는 checked 구문)
     */
    public static String getChecked(Object object1, Object object2) {
        if (StringUtil.isEmpty(object1) && StringUtil.isEmpty(object2)) {
            return " checked=\"checked\"";
        }
        if (object1 == null) {
            return "";
        }
        if (object2 == null) {
            return "";
        }
        String str1 = String.valueOf(object1);
        String str2 = String.valueOf(object2);
        if (str1.equals(str2)) {
            return " checked=\"checked\"";
        }
        return "";
    }

    /**
     * <p>HTML 태그 제거 (허용 태그 제외)</p>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String deleteHtmlTag(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        String pattern = "<(/?)(?!/####)([^<|>]+)?>";
        String[] allowTags = "span, br, u".split(","); // 줄바꿈(br), 밑줄(u) 태그 제외하고 모든 태그 삭제

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < allowTags.length; i++) {
            buffer.append("|").append(allowTags[i].trim()).append("(?!\\w)");
        }

        pattern = pattern.replace("####", buffer.toString());
        return str.replaceAll(pattern, "");
    }
}
