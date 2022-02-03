package com.jsplan.drp.cmmn.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @Class : StringUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : String 문자 처리
 */
public class StringUtil {

    /**
     * <p>trim 적용</p>
     *
     * <pre>
     * StringUtil.trim(null)    = null
     * StringUtil.trim("  ")    = ""
     * StringUtil.trim(" abc ") = "abc"
     * </pre>
     *
     * @param object (문자형 객체)
     * @return String (공백 제거 후 문자)
     */
    public static String trim(Object object) {
        return object == null ? null : String.valueOf(object).trim();
    }

    /**
     * <p>
     * trim 적용 (공백 null 처리)
     * </p>
     *
     * <pre>
     * StringUtil.trimToNull(null)    = null
     * StringUtil.trimToNull("")      = null
     * StringUtil.trimToNull("  ")    = null
     * StringUtil.trimToNull(" abc ") = "abc"
     * </pre>
     *
     * @param object (문자형 객체)
     * @return String (공백 제거 후 문자)
     */
    public static String trimToNull(Object object) {
        return isBlank(object) ? null : trim(object);
    }

    /**
     * <p>공백 체크</p>
     *
     * <pre>
     * StringUtil.isEmpty(null)    = true
     * StringUtil.isEmpty("")      = true
     * StringUtil.isEmpty("  ")    = false
     * StringUtil.isEmpty(" bob ") = false
     * StringUtil.isEmpty(123)     = false
     * </pre>
     *
     * @param object (문자형, 숫자형 객체)
     * @return boolean (체크 결과)
     */
    public static boolean isEmpty(Object object) {
        return object == null || String.valueOf(object).length() == 0;
    }

    /**
     * <p>공백 체크 (trim 적용)</p>
     *
     * <pre>
     * StringUtil.isEmptyTrim(null)    = true
     * StringUtil.isEmptyTrim("")      = true
     * StringUtil.isEmptyTrim("  ")    = true
     * StringUtil.isEmptyTrim(" bob ") = false
     * StringUtil.isEmptyTrim(123)     = false
     * </pre>
     *
     * @param object (체크할 문자형, 숫자형 객체)
     * @return boolean (체크 결과)
     */
    public static boolean isEmptyTrim(Object object) {
        return isEmpty(trim(object));
    }

    /**
     * <p>null 및 공백 체크</p>
     *
     * <pre>
     * StringUtil.isBlank(null)    = true
     * StringUtil.isBlank("")      = true
     * StringUtil.isBlank("  ")    = true
     * StringUtil.isBlank(" bob ") = false
     * StringUtil.isBlank(123)     = false
     * </pre>
     *
     * @param object (체크할 문자형, 숫자형 객체)
     * @return boolean (체크 결과)
     */
    public static boolean isBlank(Object object) {
        if (object == null) {
            return true;
        }
        String str = String.valueOf(object);
        if (str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>문자의 좌측 추출 (=LPAD)</p>
     *
     * <pre>
     * StringUtil.left(null, *)  = ""
     * StringUtil.left(*, -ve)   = ""
     * StringUtil.left("", *)    = ""
     * StringUtil.left("abc", 0) = ""
     * StringUtil.left("abc", 2) = "ab"
     * StringUtil.left("abc", 4) = "abc"
     * StringUtil.left(12345, 3) = "123"
     * </pre>
     *
     * @param object (가져올 문자형, 숫자형 객체)
     * @param len    (가져올 길이)
     * @return String (추출한 결과 값)
     */
    public static String left(Object object, int len) {
        if (object == null) {
            return "";
        }
        if (len < 0) {
            return "";
        }
        String str = String.valueOf(object);
        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(0, len);
        }
    }

    /**
     * <p>문자의 우측 추출 (=RPAD)</p>
     *
     * <pre>
     * StringUtil.right(null, *)  = ""
     * StringUtil.right(*, -ve)   = ""
     * StringUtil.right("", *)    = ""
     * StringUtil.right("abc", 0) = ""
     * StringUtil.right("abc", 2) = "bc"
     * StringUtil.right("abc", 4) = "abc"
     * StringUtil.right(12345, 3) = "345"
     * </pre>
     *
     * @param object (가져올 문자형, 숫자형 객체)
     * @param len    (가져올 길이)
     * @return String (추출한 결과 값)
     */
    public static String right(Object object, int len) {
        if (object == null) {
            return "";
        }
        if (len < 0) {
            return "";
        }
        String str = String.valueOf(object);
        if (str.length() <= len) {
            return str;
        } else {
            return str.substring(str.length() - len);
        }
    }

    /**
     * <p>문자의 중간 추출 (=MID)</p>
     *
     * <pre>
     * StringUtil.mid(null, *, *)   = ""
     * StringUtil.mid(*, *, -ve)    = ""
     * StringUtil.mid("", 0, *)     = ""
     * StringUtil.mid("abc", 0, 2)  = "ab"
     * StringUtil.mid("abc", 0, 4)  = "abc"
     * StringUtil.mid("abc", 2, 4)  = "c"
     * StringUtil.mid("abc", 4, 2)  = ""
     * StringUtil.mid("abc", -2, 2) = "ab"
     * StringUtil.mid(12345, 2, 2)  = "34"
     * </pre>
     *
     * @param object     (가져올 문자형, 숫자형 객체)
     * @param startPoint (시작할 위치)
     * @param len        (가져올 길이)
     * @return String (추출한 결과 값)
     */
    public static String mid(Object object, int startPoint, int len) {
        if (object == null) {
            return "";
        }
        String str = String.valueOf(object);
        if (len < 0 || startPoint > str.length()) {
            return "";
        }
        if (startPoint < 0) {
            startPoint = 0;
        }
        if (str.length() <= (startPoint + len)) {
            return str.substring(startPoint);
        } else {
            return str.substring(startPoint, startPoint + len);
        }
    }

    /**
     * <p>문자의 뒷부분 추출</p>
     *
     * <pre>
     * StringUtils.substringAfterLast(null, *)      = null
     * StringUtils.substringAfterLast("", *)        = ""
     * StringUtils.substringAfterLast(*, "")        = ""
     * StringUtils.substringAfterLast(*, null)      = ""
     * StringUtils.substringAfterLast("abc", "a")   = "bc"
     * StringUtils.substringAfterLast("abcba", "b") = "a"
     * StringUtils.substringAfterLast("abc", "c")   = ""
     * StringUtils.substringAfterLast("a", "a")     = ""
     * StringUtils.substringAfterLast("a", "z")     = ""
     * </pre>
     *
     * @param str       (적용할 문자)
     * @param separator (구분자)
     * @return String (추출한 결과 값)
     */
    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return "";
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == (str.length() - separator.length())) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>null 및 공백 체크 + trim 적용</p>
     *
     * <pre>
     * StringUtil.clean(null)    = ""
     * StringUtil.clean("")      = ""
     * StringUtil.clean("  ")    = ""
     * StringUtil.clean(" abc ") = "abc"
     * StringUtil.clean(12345)   = "12345"
     * </pre>
     *
     * @param object (변경할 문자형, 숫자형 객체)
     * @return String (변경 후 문자)
     */
    public static String clean(Object object) {
        return object == null ? "" : trim(object);
    }

    /**
     * <p>문자 전체 변경</p>
     *
     * <pre>
     * StringUtil.replaceAll(null, "b", "ee")    = ""
     * StringUtil.replaceAll("", "b", "ee")      = ""
     * StringUtil.replaceAll("  ", "b", "ee")    = "  "
     * StringUtil.replaceAll(" abc ", "b", "ee") = " aeec "
     * StringUtil.replaceAll(117, "1", "33")     = "33337"
     * </pre>
     *
     * @param object (변경할 문자형, 숫자형 객체)
     * @param oldStr (이전 문자)
     * @param newStr (신규 문자)
     * @return String (변경 후 문자)
     */
    public static String replaceAll(Object object, String oldStr, String newStr) {
        if (object == null) {
            return "";
        }
        String str = String.valueOf(object);
        if (oldStr == null || newStr == null || oldStr.length() == 0) {
            return str;
        }
        int i = str.lastIndexOf(oldStr);
        if (i < 0) {
            return str;
        }
        StringBuilder sbuf = new StringBuilder(str);
        while (i >= 0) {
            sbuf.replace(i, (i + oldStr.length()), newStr);
            i = str.lastIndexOf(oldStr, i - 1);
        }
        return sbuf.toString();
    }

    /**
     * <p>특정 범위 문자 변경</p>
     *
     * <pre>
     * StringUtil.replaceRange(null, 1, 2, "ee")    = ""
     * StringUtil.replaceRange("", 1, 2, "ee")      = ""
     * StringUtil.replaceRange("  ", 1, 2, "ee")    = " ee"
     * StringUtil.replaceRange(" abc ", 0, 3, "ee") = "eec "
     * StringUtil.replaceRange("A1EK-05816", 3, 4, "E")     = "A1EE-05816"
     * </pre>
     *
     * @param object   (변경할 문자형, 숫자형 객체)
     * @param startNum (범위 시작 지점)
     * @param endNum   (범위 종료 지점)
     * @param newStr   (신규 문자)
     * @return String (변경 후 문자)
     */
    public static String replaceRange(Object object, int startNum, int endNum, String newStr) {
        if (object == null || object == "") {
            return "";
        }
        String str = String.valueOf(object);
        if (startNum < 0 || endNum <= 0 || startNum >= endNum) {
            return str;
        }
        StringBuilder sbuf = new StringBuilder(str);
        sbuf.replace(startNum, endNum, newStr);
        return sbuf.toString();
    }

    /**
     * <p>null 및 공백 변경 (String 형)</p>
     *
     * <pre>
     * StringUtil.replaceBlank(null, "xyz")    = "xyz"
     * StringUtil.replaceBlank("", "xyz")      = "xyz"
     * StringUtil.replaceBlank("  ", "xyz")    = "xyz"
     * StringUtil.replaceBlank(" abc ", "xyz") = "abc"
     * StringUtil.replaceBlank(123, "xyz")     = "123"
     * </pre>
     *
     * @param object (적용할 문자형, 숫자형 객체)
     * @param newStr (새로운 문자)
     * @return String (적용 후 문자)
     */
    public static String replaceBlank(Object object, String newStr) {
        return isBlank(object) ? newStr : trim(object);
    }

    /**
     * <p>null 및 공백 변경 (int 형)</p>
     *
     * <pre>
     * StringUtil.replaceBlank(null, 567)    = 567
     * StringUtil.replaceBlank("", 567)      = 567
     * StringUtil.replaceBlank("  ", 567)    = 567
     * StringUtil.replaceBlank(" 123 ", 567) = 123
     * StringUtil.replaceBlank(123, 567)     = 123
     * </pre>
     *
     * @param object (적용할 문자형, 숫자형 객체)
     * @param newInt (새로운 숫자)
     * @return int (적용 후 숫자)
     */
    public static int replaceBlank(Object object, int newInt) {
        return isBlank(object) ? newInt : Integer.parseInt(trim(object));
    }

    /**
     * <p>null 및 공백 변경 (long 형)</p>
     *
     * <pre>
     * StringUtil.replaceBlank(null, 567)    = 567
     * StringUtil.replaceBlank("", 567)      = 567
     * StringUtil.replaceBlank("  ", 567)    = 567
     * StringUtil.replaceBlank(" 123 ", 567) = 123
     * StringUtil.replaceBlank(123, 567)     = 123
     * </pre>
     *
     * @param object  (적용할 문자형, 숫자형 객체)
     * @param newLong (새로운 숫자)
     * @return long (적용 후 숫자)
     */
    public static long replaceBlank(Object object, long newLong) {
        return isBlank(object) ? newLong : Long.parseLong(trim(object));
    }

    /**
     * <p>null 및 공백 변경 (float 형)</p>
     *
     * <pre>
     * StringUtil.replaceBlank(null, 567)    = 567
     * StringUtil.replaceBlank("", 567)      = 567
     * StringUtil.replaceBlank("  ", 567)    = 567
     * StringUtil.replaceBlank(" 123 ", 567) = 123
     * StringUtil.replaceBlank(123, 567)     = 123
     * </pre>
     *
     * @param object   (적용할 문자형, 숫자형 객체)
     * @param newFloat (새로운 숫자)
     * @return float (적용 후 숫자)
     */
    public static float replaceBlank(Object object, float newFloat) {
        return isBlank(object) ? newFloat : Float.parseFloat(trim(object));
    }

    /**
     * <p>문자 일치 여부 체크 (대소문자 무시)</p>
     *
     * <pre>
     * StringUtil.isEqualsIgnoreCase(null, null)   = true
     * StringUtil.isEqualsIgnoreCase(null, "abc")  = false
     * StringUtil.isEqualsIgnoreCase("abc", null)  = false
     * StringUtil.isEqualsIgnoreCase("abc", "abc") = true
     * StringUtil.isEqualsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @param object1 (체크할 문자형 객체1)
     * @param object2 (체크할 문자형 객체2)
     * @return boolean (체크 결과)
     */
    public static boolean isEqualsIgnoreCase(Object object1, Object object2) {
        String str1 = (String) object1;
        String str2 = (String) object2;
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    /**
     * <p>검색어 포함 여부 체크</p>
     *
     * <pre>
     * StringUtil.isContain(null, *)    = false
     * StringUtil.isContain(*, null)    = false
     * StringUtil.isContain("", "")     = false
     * StringUtil.isContain("abc", "")  = false
     * StringUtil.isContain(12345, "a") = false
     * StringUtil.isContain("abc", "a") = true
     * StringUtil.isContain(12345, "3") = true
     * </pre>
     *
     * @param object  (체크할 문자형, 숫자형 객체)
     * @param keyword (검색어)
     * @return boolean (검색어 포함 여부)
     */
    public static boolean isContain(Object object, String keyword) {
        if (isEmpty(object) || isEmpty(keyword)) {
            return false;
        }
        return String.valueOf(object).contains(keyword);
    }

    /**
     * <p>검색어 포함 여부 체크 (구분자 적용)</p>
     *
     * <pre>
     * StringUtil.isContain(null, *, *)       = false
     * StringUtil.isContain(*, null, *)       = false
     * StringUtil.isContain("", "", *)        = false
     * StringUtil.isContain("abc", "", *)     = false
     * StringUtil.isContain("ab#c", "a", "#") = false
     * StringUtil.isContain("ab#c#", "", "#") = true
     * StringUtil.isContain("a#bc", "a", "#") = true
     * </pre>
     *
     * @param object    (체크할 문자형 객체)
     * @param keyword   (검색어)
     * @param separator (구분자)
     * @return boolean (검색어 포함 여부)
     */
    public static boolean isContain(Object object, String keyword, String separator) {
        if (object == null || keyword == null) {
            return false;
        }
        if (isEmpty(object) && isEmpty(keyword)) {
            return false;
        }
        StringTokenizer strToken = new StringTokenizer(String.valueOf(object), separator);
        while (strToken.hasMoreTokens()) {
            if (strToken.nextToken().equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>검색어 포함 여부 체크 (구분자 적용 + 1개 이상 포함)</p>
     *
     * <pre>
     * StringUtil.isContainKeywords(null, *, *)         = false
     * StringUtil.isContainKeywords(*, null, *)         = false
     * StringUtil.isContainKeywords("", "", *)          = false
     * StringUtil.isContainKeywords("", "abc", *)       = false
     * StringUtil.isContainKeywords("abc", "d#e", "#")  = false
     * StringUtil.isContainKeywords(12345, "d#54", "#") = false
     * StringUtil.isContainKeywords("abc", "b#d", "#")  = true
     * StringUtil.isContainKeywords(12345, "d#45", "#") = true
     * </pre>
     *
     * @param object    (체크할 문자형, 숫자형 객체)
     * @param keywords  (구분자로 되어있는 검색어)
     * @param separator (구분자)
     * @return boolean (검색어 포함 여부)
     */
    public static boolean isContainKeywords(Object object, String keywords, String separator) {
        if (object == null || keywords == null) {
            return false;
        }
        if (isEmpty(object) && isEmpty(keywords)) {
            return false;
        }
        StringTokenizer strToken = new StringTokenizer(keywords, separator);
        while (strToken.hasMoreTokens()) {
            if (isContain(String.valueOf(object), strToken.nextToken())) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>특정 문자의 구간별 추가 삽입</p>
     *
     * <pre>
     * StringUtil.insertion(null, *, *, *)    = ""
     * StringUtil.insertion("", *, *, *)      = ""
     * StringUtil.insertion("  ", *, *, *)    = "  "
     * StringUtil.insertion("abc", *, 0, *)   = "abc"
     * StringUtil.insertion("abc", *, 1, 4)   = "abc"
     * StringUtil.insertion(12345, *, 1, 5)   = "12345"
     * StringUtil.insertion("abc", "|", 1, 0) = "a|b|c"
     * StringUtil.insertion("abc", "|", 2, 2) = "ab|c"
     * StringUtil.insertion(12345, "|", 2, 4) = "12|34|5"
     * </pre>
     *
     * @param object      (변경할 문자형, 숫자형 객체)
     * @param insertStr   (삽입할 문자)
     * @param insertPoint (삽입할 위치)
     * @param limitLen    (제한된 길이)
     * @return String (변경 후 문자)
     */
    public static String insertion(Object object, String insertStr, int insertPoint, int limitLen) {
        if (object == null) {
            return "";
        }
        String str = String.valueOf(object);
        if (isBlank(str)) {
            return str;
        }
        if (insertPoint < 1) {
            return str;
        }
        if (str.length() <= limitLen) {
            return str;
        }
        int index = 0;
        StringBuilder sBuf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (insertPoint == index) {
                sBuf.append(insertStr);
                index = 0;
            }
            sBuf.append(str.charAt(i));
            ++index;
        }
        return sBuf.toString();
    }

    /**
     * <p>소문자 변경</p>
     *
     * <pre>
     * StringUtil.toLowerCase(null)  = ""
     * StringUtil.toLowerCase("")    = ""
     * StringUtil.toLowerCase("  ")  = "  "
     * StringUtil.toLowerCase("aBC") = "abc"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String toLowerCase(Object object) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object).toLowerCase();
    }

    /**
     * <p>대문자 변경</p>
     *
     * <pre>
     * StringUtil.toUpperCase(null)  = ""
     * StringUtil.toUpperCase("")    = ""
     * StringUtil.toUpperCase("  ")  = "  "
     * StringUtil.toUpperCase("aBc") = "ABC"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String toUpperCase(Object object) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object).toUpperCase();
    }

    /**
     * <p>camel case 방식 문자 변경</p>
     *
     * <pre>
     * StringUtil.toCamelCase(null)      = ""
     * StringUtil.toCamelCase("")        = ""
     * StringUtil.toCamelCase("  ")      = ""
     * StringUtil.toCamelCase("abc")     = "abc"
     * StringUtil.toCamelCase("aBc")     = "aBc"
     * StringUtil.toCamelCase("AbC")     = "abc"
     * StringUtil.toCamelCase("abc_def") = "abcDef"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String toCamelCase(Object object) {
        if (isBlank(object)) {
            return "";
        }
        String str = trim(object);
        if (str.indexOf('_') < 0 && Character.isLowerCase(str.charAt(0))) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (currentChar == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(currentChar));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(currentChar));
                }
            }
        }
        return result.toString();
    }

    /**
     * <p>쿼리용 표준형 문자 변경</p>
     *
     * <pre>
     * StringUtil.toQueryString(null)   = ""
     * StringUtil.toQueryString("")     = ""
     * StringUtil.toQueryString("  ")   = " "
     * StringUtil.toQueryString("abc")  = "abc"
     * StringUtil.toQueryString("ab'c") = "ab''c"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String toQueryString(Object object) {
        if (object == null) {
            return "";
        }
        String str = String.valueOf(object);
        if (str.length() == 0) {
            return str;
        }
        return str.replaceAll("'", "''");
    }

    /**
     * <p>홑따옴표 → ASCII 변경</p>
     *
     * <pre>
     * StringUtil.encodeSingleMark(null)   = ""
     * StringUtil.encodeSingleMark("")     = ""
     * StringUtil.encodeSingleMark("  ")   = ""
     * StringUtil.encodeSingleMark("abc")  = "abc"
     * StringUtil.encodeSingleMark("ab'c") = "ab&amp;#39;c"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String encodeSingleMark(Object object) {
        if (isBlank(object)) {
            return "";
        }
        return String.valueOf(object).replaceAll("\'", "&#39;");
    }

    /**
     * <p>쌍따옴표 → ASCII 변경</p>
     *
     * <pre>
     * StringUtil.encodeDoubleMark(null)   = ""
     * StringUtil.encodeDoubleMark("")     = ""
     * StringUtil.encodeDoubleMark("  ")   = ""
     * StringUtil.encodeDoubleMark("abc")  = "abc"
     * StringUtil.encodeDoubleMark("ab"c") = "ab&amp;#34;c"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String encodeDoubleMark(Object object) {
        if (isBlank(object)) {
            return "";
        }
        return String.valueOf(object).replaceAll("\"", "&#34;");
    }

    /**
     * <p>홑따옴표, 쌍따옴표 → ASCII 변경</p>
     *
     * <pre>
     * StringUtil.encodeMark(null)   = ""
     * StringUtil.encodeMark("")     = ""
     * StringUtil.encodeMark("  ")   = ""
     * StringUtil.encodeMark("abc")  = "abc"
     * StringUtil.encodeMark("ab'c") = "ab&amp;#39;c"
     * StringUtil.encodeMark("ab"c") = "ab&amp;#34;c"
     * </pre>
     *
     * @param object (변경할 문자형 객체)
     * @return String (변경 후 문자)
     */
    public static String encodeMark(Object object) {
        if (isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        str = encodeSingleMark(str);
        str = encodeDoubleMark(str);
        return str;
    }

    /**
     * <p>문자 자르기</p>
     *
     * <pre>
     * StringUtil.cutString(null, *)  = ""
     * StringUtil.cutString("", *)    = ""
     * StringUtil.cutString("  ", *)  = ""
     * StringUtil.cutString("abc", 3) = "abc"
     * StringUtil.cutString("abc", 2) = "ab"
     * </pre>
     *
     * @param object (변경할 문자형, 숫자형 객체)
     * @param len    (가져올 문자 길이)
     * @return String (변경 후 문자)
     */
    public static String cutString(Object object, int len) {
        if (isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        if (str.length() > len) {
            str = str.substring(0, len);
        }
        return str;
    }

    /**
     * <p>문자 자르기 + 접미사 처리</p>
     *
     * <pre>
     * StringUtil.cutString(null, *, *)      = ""
     * StringUtil.cutString("", *, *)        = ""
     * StringUtil.cutString("  ", *, *)      = ""
     * StringUtil.cutString("abc", 3, *)     = "abc"
     * StringUtil.cutString("abc", 2, "...") = "ab..."
     * </pre>
     *
     * @param object (변경할 문자형, 숫자형 객체)
     * @param len    (가져올 문자 길이)
     * @param suffix (접미사)
     * @return String (변경 후 문자)
     */
    public static String cutString(Object object, int len, String suffix) {
        if (isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        if (str.length() > len) {
            str = str.substring(0, len) + suffix;
        }
        return str;
    }

    /**
     * <p>문자 자르기 + "..." 처리</p>
     *
     * <pre>
     * StringUtil.cutTitle(null, *)  = ""
     * StringUtil.cutTitle("", *)    = ""
     * StringUtil.cutTitle("  ", *)  = ""
     * StringUtil.cutTitle("abc", 3) = "abc"
     * StringUtil.cutTitle("abc", 2) = "ab..."
     * </pre>
     *
     * @param object (변경할 문자형, 숫자형 객체)
     * @param len    (가져올 문자 길이)
     * @return String (변경 후 문자)
     */
    public static String cutTitle(Object object, int len) {
        return cutString(object, len, "...");
    }

    /**
     * <p>특정 문자 카운팅</p>
     *
     * <pre>
     * StringUtil.countMatches(null, *)    = 0
     * StringUtil.countMatches("", *)      = 0
     * StringUtil.countMatches(*, null)    = 0
     * StringUtil.countMatches(*, "")      = 0
     * StringUtil.countMatches("abb", "b") = 2
     * </pre>
     *
     * @param object  (검색할 문자형, 숫자형 객체)
     * @param keyword (검색어)
     * @return int (메칭되는 수)
     */
    public static int countMatches(Object object, String keyword) {
        if (isEmpty(object) || isEmpty(keyword)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = String.valueOf(object).indexOf(keyword, idx)) != -1) {
            count++;
            idx += keyword.length();
        }
        return count;
    }

    /**
     * <p>문자 앞 0 추가</p>
     *
     * <pre>
     * StringUtil.addZero(null, *)  = ""
     * StringUtil.addZero("", *)    = ""
     * StringUtil.addZero("  ", *)  = ""
     * StringUtil.addZero("123", 2) = "123"
     * StringUtil.addZero("123", 3) = "123"
     * StringUtil.addZero("123", 5) = "00123"
     * </pre>
     *
     * @param object (변경할 문자형, 숫자형 객체)
     * @param len    (가져올 길이)
     * @return String (변경 후 값)
     */
    public static String addZero(Object object, int len) {
        if (isBlank(object)) {
            return "";
        }
        String str = String.valueOf(object);
        StringBuilder zeroNum = new StringBuilder();
        if (str.length() < len) {
            for (int i = 0; i < (len - str.length()); i++) {
                zeroNum.append("0");
            }
        }
        return zeroNum + str;
    }

    /**
     * <p>특정 문자 제거</p>
     *
     * <pre>
     * StringUtil.addZero(null, *)  = ""
     * StringUtil.addZero("", *)    = ""
     * StringUtil.addZero("  ", *)  = ""
     * StringUtil.addZero("123", 2) = "123"
     * StringUtil.addZero("123", 3) = "123"
     * StringUtil.addZero("123", 5) = "00123"
     * </pre>
     *
     * @param oldStr    (이전 문자)
     * @param removeStr (제거할 문자)
     * @return String (변경 후 문자)
     */
    public static String remove(String oldStr, String removeStr) {
        return replaceAll(oldStr, removeStr, "");
    }

    /**
     * <p>문자 split 처리 (comma 기준)</p>
     *
     * <pre>
     * StringUtil.split("") = {}
     * StringUtil.split("A,B,C,D") = {A}, {B}, {C}, {D}
     * StringUtil.split("A,B,C,D,") = {A}, {B}, {C}, {D}
     * StringUtil.split(",A,") = {}, {A}
     * StringUtil.split("A,B,C,,")  = {A}, {B}, {C}, {}, {}
     * StringUtil.split("A,B,,C, ,")  = {A}, {B}, {}, {C}, { }
     * </pre>
     *
     * @param src (처리할 문자)
     * @return String[] (처리 후 배열)
     */
    public static String[] split(String src) {
        return split(src, ",");
    }

    /**
     * <p>문자 split 처리</p>
     *
     * <pre>
     * StringUtil.split("", "") = {}
     * StringUtil.split("*", "") = {*}
     * StringUtil.split("A", "A") = {}
     * StringUtil.split("AB", "AB") = {A}, {B}
     * StringUtil.split("ABC", "CDD") = {AB}, {C}
     * StringUtil.split("ABBC", "B") = {A}, {}, {C}
     * StringUtil.split("ABCD", "B") = {A}, {CD}
     * </pre>
     *
     * @param src  (처리할 문자)
     * @param mark (구분자)
     * @return String[] (처리 후 배열)
     */
    public static String[] split(String src, String mark) {
        return split(src, mark, false);
    }

    /**
     * <p>문자 split 처리 + trim 적용</p>
     *
     * <pre>
     * StringUtil.split("A B C D", "B", false) = {A }, { C D}
     * StringUtil.split("A B C D", "B", true) = {A}, {C D}
     * </pre>
     *
     * @param src   (처리할 문자)
     * @param mark  (구분자)
     * @param btrim (trim 여부)
     * @return String[] (처리 후 배열)
     */
    public static String[] split(String src, String mark, boolean btrim) {
        String[] arrRet = null;
        ArrayList<String> al = null;
        try {
            StringTokenizer st = new StringTokenizer(src, mark, true);
            al = new ArrayList<String>();
            String prevToken = "";
            int cnt = st.countTokens();

            for (int i = 0, j = 0; i < cnt; i++) {
                String strToken = st.nextToken();
                if (i == 0) {
                    if (mark.equals(strToken)) {
                        prevToken = strToken;
                        strToken = "";
                    }
                } else {
                    if (mark.equals(strToken)) {
                        if (mark.equals(prevToken)) {
                            if (i == (cnt - 1)) {
                                al.add(j, "");
                                j++;
                            }
                            prevToken = strToken;
                            strToken = "";
                        } else {
                            prevToken = strToken;
                            continue;
                        }
                    } else {
                        prevToken = strToken;
                    }
                }
                strToken = btrim ? strToken.trim() : strToken;
                al.add(j, strToken);
                j++;
            }
            arrRet = new String[al.size()];
            arrRet = (String[]) al.toArray(arrRet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrRet;
    }

    /**
     * <p>split 처리 후 특정 인덱스 요소 구하기</p>
     *
     * <pre>
     * StringUtil.split("", "", 0) = ""
     * StringUtil.split(ABCD, "", 0) = "ABCD"
     * StringUtil.split(ABCD, "A", 0) = ""
     * StringUtil.split("ABCD", "B", 1) = "CD"
     * </pre>
     *
     * @param src  (처리할 문자)
     * @param mark (구분자)
     * @param num  (배열 인덱스)
     * @return String (인덱스 해당 값)
     */
    public static String split(String src, String mark, int num) {
        String resultValue = "";
        if (src != null && !src.equals("")) {
            String[] strArr = split(src, mark);
            if (strArr.length >= (num + 1)) {
                resultValue = strArr[num];
            }
        }
        return resultValue;
    }

    /**
     * <p>대문자 여부 체크</p>
     *
     * <pre>
     * StringUtil.upperCheck("ABC")  = true
     * StringUtil.upperCheck("AB1C")  = false
     * StringUtil.upperCheck("abC")  = false
     * StringUtil.upperCheck("AB C")  = false
     * StringUtil.upperCheck("AB,C")  = false
     * </pre>
     *
     * @param str (검사할 문자열)
     * @return boolean (대문자 여부)
     */
    public static boolean upperCheck(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String regex1 = "[A-Z]*";
        return str.matches(regex1);
    }

    /**
     * <p>소문자 여부 체크</p>
     *
     * <pre>
     * StringUtil.lowerCheck("abc")  = true
     * StringUtil.lowerCheck("ab1c")  = false
     * StringUtil.lowerCheck("ABc")  = false
     * StringUtil.lowerCheck("ab c")  = false
     * StringUtil.lowerCheck("ab,c")  = false
     * </pre>
     *
     * @param str (검사할 문자열)
     * @return boolean (소문자 여부)
     */
    public static boolean lowerCheck(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String regex1 = "[a-z]*";
        return str.matches(regex1);
    }

    /**
     * <p>숫자 여부 체크</p>
     *
     * <pre>
     * StringUtil.numCheck("123")  = true
     * StringUtil.numCheck("123ab")  = false
     * StringUtil.numCheck("12.3")  = false
     * StringUtil.numCheck("12 3")  = false
     * StringUtil.numCheck("100000")  = true
     * </pre>
     *
     * @param str (검사할 문자열)
     * @return boolean (숫자 여부)
     */
    public static boolean numCheck(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String regex1 = "[0-9]*";
        return str.matches(regex1);
    }

    /**
     * <p>실수 여부 체크</p>
     *
     * <pre>
     * StringUtil.doubleCheck("10")  = true
     * StringUtil.doubleCheck("0.12")  = true
     * StringUtil.doubleCheck("1.000")  = true
     * StringUtil.doubleCheck("-1.01")  = false
     * StringUtil.doubleCheck("0001.2")  = false
     * </pre>
     *
     * @param str (검사할 문자열)
     * @return boolean (실수 여부)
     */
    public static boolean doubleCheck(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String regex1 = "^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$";
        return str.matches(regex1);
    }

    /**
     * <p>대문자 + 숫자 여부 체크</p>
     *
     * <pre>
     * StringUtil.upperNumCheck("AB1")  = true
     * StringUtil.upperNumCheck("AB 1")  = false
     * StringUtil.upperNumCheck("ABc12")  = false
     * StringUtil.upperNumCheck("CHECK000")  = true
     * StringUtil.upperNumCheck("STR001,")  = false
     * </pre>
     *
     * @param str (검사할 문자열)
     * @return boolean (대문자 + 숫자 여부)
     */
    public static boolean upperNumCheck(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String regex1 = "[A-Z0-9]*";
        return str.matches(regex1);
    }

    /**
     * <p>소문자 + 숫자 여부 체크</p>
     *
     * <pre>
     * StringUtil.lowerNumCheck("ab1")  = true
     * StringUtil.lowerNumCheck("ab 1")  = false
     * StringUtil.lowerNumCheck("abC12")  = false
     * StringUtil.lowerNumCheck("check000")  = true
     * StringUtil.lowerNumCheck("str001,")  = false
     * </pre>
     *
     * @param str (검사할 문자열)
     * @return boolean (소문자 + 숫자 여부)
     */
    public static boolean lowerNumCheck(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String regex1 = "[a-z0-9]*";
        return str.matches(regex1);
    }

    /**
     * <p>배열 요소 간 중복 여부 체크</p>
     *
     * <pre>
     * StringUtil.findDuplicateValue("A,B,C,A")  = true
     * StringUtil.findDuplicateValue("A,B,C,D")  = false
     * StringUtil.findDuplicateValue("A ,B,C,A ")  = true
     * StringUtil.findDuplicateValue("A,B,C,")  = false
     * StringUtil.findDuplicateValue("A,B,,C, ,")  = false
     * </pre>
     *
     * @param str (구분자로 연결된 문자열)
     * @return boolean (중복 여부)
     */
    public static boolean findDuplicateValue(String str) {
        boolean flag = false;
        String[] strArr = split(str, ",");
        for (int i = 0; i < strArr.length; i++) {
            for (int j = 0; j < strArr.length; j++) {
                if (i >= j) {
                    continue;
                }
                if (strArr[i].equals(strArr[j])) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * <p>문자 제거 (공백 포함)</p>
     *
     * <pre>
     * StringUtils.strip(null, *)          = null
     * StringUtils.strip("", *)            = ""
     * StringUtils.strip("abc", null)      = "abc"
     * StringUtils.strip("  abc", null)    = "abc"
     * StringUtils.strip("abc  ", null)    = "abc"
     * StringUtils.strip(" abc ", null)    = "abc"
     * StringUtils.strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str        (처리할 문자)
     * @param stripChars (제거할 문자)
     * @return String (변경 후 문자)
     */
    public static String strip(String str, String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }

    /**
     * <p>앞 부분 문자 제거 (공백 포함)</p>
     *
     * <pre>
     * StringUtils.stripStart(null, *)          = null
     * StringUtils.stripStart("", *)            = ""
     * StringUtils.stripStart("abc", "")        = "abc"
     * StringUtils.stripStart("abc", null)      = "abc"
     * StringUtils.stripStart("  abc", null)    = "abc"
     * StringUtils.stripStart("abc  ", null)    = "abc  "
     * StringUtils.stripStart(" abc ", null)    = "abc "
     * StringUtils.stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str        (처리할 문자)
     * @param stripChars (제거할 문자)
     * @return String (변경 후 문자)
     */
    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }
        return str.substring(start);
    }

    /**
     * <p>뒷 부분 문자 제거 (공백 포함)</p>
     *
     * <pre>
     * StringUtils.stripEnd(null, *)          = null
     * StringUtils.stripEnd("", *)            = ""
     * StringUtils.stripEnd("abc", "")        = "abc"
     * StringUtils.stripEnd("abc", null)      = "abc"
     * StringUtils.stripEnd("  abc", null)    = "  abc"
     * StringUtils.stripEnd("abc  ", null)    = "abc"
     * StringUtils.stripEnd(" abc ", null)    = " abc"
     * StringUtils.stripEnd("  abcyx", "xyz") = "  abc"
     * StringUtils.stripEnd("120.00", ".0")   = "12"
     * </pre>
     *
     * @param str        (처리할 문자)
     * @param stripChars (제거할 문자)
     * @return String (변경 후 문자)
     */
    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }
        return str.substring(0, end);
    }
}
