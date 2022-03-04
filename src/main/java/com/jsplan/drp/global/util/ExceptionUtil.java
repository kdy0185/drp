package com.jsplan.drp.global.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : ExceptionUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 예외 처리
 */
public class ExceptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    /**
     * <p>Error 정보의 Console 출력</p>
     *
     * @param e       (Exception 객체)
     * @param clazz   (에러가 발생한 Class 객체)
     * @param message (추가할 Exception 메시지)
     */
    public static void printConsole(Exception e, Class<?> clazz, String message) {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(DateUtil.getNowAll()).append("] [ERROR] ").append(clazz.getName())
            .append("\n\n");
        sb.append("(").append(e.toString()).append(") - ").append(StringUtil.clean(message));
        System.out.println(
            "+------------------------------------------------------------------------------------------------------------------------+");
        System.out.println(
            "+------------------------------------------------------------------------------------------------------------------------+");
        System.out.println(sb.toString());
        System.out.println(
            "+------------------------------------------------------------------------------------------------------------------------+");
        e.printStackTrace();
        System.out.println(
            "+------------------------------------------------------------------------------------------------------------------------+");
        System.out.println(
            "+------------------------------------------------------------------------------------------------------------------------+");
        sb = null;
    }

    /**
     * <p>Exception StackTrace 구하기</p>
     *
     * @param e (Exception 객체)
     * @return String (Exception StackTrace)
     */
    public static String getStackTrace(Exception e) {
        StringBuffer sb = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sb = sw.getBuffer();
            return sb.toString();
        } catch (Exception e2) {
            logger.warn("Exception StackTrace를 가져올 수 없습니다.");
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * <p>사용자 메시지를 추가한 Exception 처리</p>
     *
     * @param e       (Exception 객체)
     * @param message (추가할 Exception 메시지)
     * @return String (Exception 메시지)
     */
    public static String addMessage(Exception e, String message) {
        String str = e.toString() + "\n"
            + "Stacktrace: " + getStackTrace(e)
            + "Caused by: " + message + "\n";
        return str;
    }
}
