package com.jsplan.drp.global.util;

import java.text.NumberFormat;

/**
 * @Class : NumberUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 숫자 처리
 */
public class NumberUtil {

    /**
     * <p>범위 내 랜덤 숫자 생성</p>
     *
     * @param startNum (숫자 시작 범위)
     * @param endNum   (숫자 끝 범위)
     * @return int (생성된 랜덤 숫자)
     */
    public static int getRandomInt(int startNum, int endNum) {
        int randomNum;
        if (endNum == 0) {
            randomNum = 0;
        } else {
            randomNum = (int) ((endNum - startNum + 1) * Math.random() + startNum);
        }
        return randomNum;
    }

    /**
     * <p>범위 내 랜덤 숫자 생성 + 자릿수 지정</p>
     *
     * <pre>
     * NumberUtil.getRandomString(1, 100)    = "099"
     * NumberUtil.getRandomString(1, 100)    = "100"
     * NumberUtil.getRandomString(1, 1000)   = "0007"
     * NumberUtil.getRandomString(100, 1000) = "0111"
     * NumberUtil.getRandomString(100, 2000) = "1234"
     * </pre>
     *
     * @param startNum (숫자 시작 범위)
     * @param endNum   (숫자 끝 범위)
     * @return String (생성된 랜덤한 숫자)
     */
    public static String getRandomString(int startNum, int endNum) {
        return StringUtil.addZero(getRandomInt(startNum, endNum), String.valueOf(endNum).length());
    }

    /**
     * <p>모든 요소의 숫자 0 여부 체크</p>
     *
     * <pre>
     * NumberUtil.isAllZero(null)   = true
     * NumberUtil.isAllZero("000")  = true
     * NumberUtil.isAllZero("ac0")  = false
     * </pre>
     *
     * @param str (체크할 문자)
     * @return boolean (체크 결과)
     */
    public static boolean isAllZero(String str) {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }
        return str.length() > 0;
    }

    /**
     * <p>숫자 여부 체크</p>
     *
     * <pre>
     * NumberUtil.isDigits(null)     = false
     * NumberUtil.isDigits("")       = false
     * NumberUtil.isDigits("     ")  = false
     * NumberUtil.isDigits("111.1")  = false
     * NumberUtil.isDigits("12345")  = true
     * </pre>
     *
     * @param str (체크할 문자)
     * @return boolean (체크 결과)
     */
    public static boolean isDigits(String str) {
        if ((str == null) || (str.length() == 0)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>3자리 마다 콤마 삽입</p>
     *
     * <pre>
     * NumberUtil.toNumberFormat(12345) = 12,345
     * </pre>
     *
     * @param intVal (적용할 숫자)
     * @return String (적용 후 숫자)
     */
    public static String toNumberFormat(int intVal) {
        return NumberFormat.getInstance().format(intVal);
    }
}
