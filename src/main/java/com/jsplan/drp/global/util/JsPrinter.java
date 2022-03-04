package com.jsplan.drp.global.util;

import com.jsplan.drp.global.config.StaticConfig;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : JsPrinter
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : JavaScript 출력
 */

public class JsPrinter {

    private static final Logger logger = LoggerFactory.getLogger(JsPrinter.class);

    /**
     * <p>JavaScript 출력</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param function (출력할 자바스크립트)
     */
    public static void printScript(HttpServletResponse response, String function) {
        printScript(response, function, false);
    }

    /**
     * <p>JavaScript 출력</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param function (출력할 자바스크립트)
     * @param isClose  (스트림 close 여부)
     */
    public static void printScript(HttpServletResponse response,
        String function, boolean isClose) {
        try {
            response.setContentType("text/html; charset=" + StaticConfig.DEFAULT_ENCODING);
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("<!--");
            out.println(function);
            out.println("//-->");
            out.println("</script>");
            if (isClose) {
                out.close();
            }
        } catch (IOException e) {
            logger.error(ExceptionUtil.addMessage(e, "JavaScript 출력 에러"));
        }
    }

    /**
     * <p>alert 메시지 전송</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param message  (전송할 메시지)
     */
    public static void alert(HttpServletResponse response, String message) {
        printScript(response, "alert(\"" + message + "\");", false);
    }

    /**
     * <p>팝업창 닫기</p>
     *
     * @param response (HttpServletResponse 객체)
     */
    public static void close(HttpServletResponse response) {
        printScript(response, "self.close();", true);
    }

    /**
     * <p>alert 메시지 전송 + 팝업창 닫기</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param message  (전송할 메시지)
     */
    public static void close(HttpServletResponse response, String message) {
        String str = "alert(\"" + message + "\");\n"
            + "self.close();";
        printScript(response, str, true);
    }

    /**
     * <p>이전 페이지 이동</p>
     *
     * @param response (HttpServletResponse 객체)
     */
    public static void back(HttpServletResponse response) {
        printScript(response, "history.back();", true);
    }

    /**
     * <p>alert 메시지 전송 + 이전 페이지 이동</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param message  (전송할 메시지)
     */
    public static void back(HttpServletResponse response, String message) {
        String str = "alert(\"" + message + "\");\n"
            + "history.back();";
        printScript(response, str, true);
    }

    /**
     * <p>특정 페이지 이동 (location.href)</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param linkUrl  (이동할 페이지 URL)
     */
    public static void href(HttpServletResponse response, String linkUrl) {
        printScript(response, "location.href = \"" + linkUrl + "\";", true);
    }

    /**
     * <p>alert 메시지 전송 + 특정 페이지 이동 (location.href)</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param linkUrl  (이동할 페이지 URL)
     * @param message  (전송할 메시지)
     */
    public static void href(HttpServletResponse response, String linkUrl,
        String message) {
        String str = "alert(\"" + message + "\");\n"
            + "location.href = \"" + linkUrl + "\";";
        printScript(response, str, true);
    }

    /**
     * <p>특정 페이지 이동 (location.replace)</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param linkUrl  (이동할 페이지 URL)
     */
    public static void replace(HttpServletResponse response, String linkUrl) {
        printScript(response, "location.replace(\"" + linkUrl + "\");", true);
    }

    /**
     * <p>alert 메시지 전송 + 특정 페이지 이동 (location.replace)</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param linkUrl  (이동할 페이지 URL)
     * @param message  (전송할 메시지)
     */
    public static void replace(HttpServletResponse response, String linkUrl,
        String message) {
        String str = "alert(\"" + message + "\");\n"
            + "location.replace(\"" + linkUrl + "\");";
        printScript(response, str, true);
    }

    /**
     * <p>특정 페이지 이동 (meta 태그 적용)</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param linkUrl  (이동할 페이지 URL)
     */
    public static void metaRefresh(HttpServletResponse response, String linkUrl) {
        try {
            response.setContentType("text/html; charset=" + StaticConfig.DEFAULT_ENCODING);
            PrintWriter out = response.getWriter();
            out.println(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            out.println(
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ko\" lang=\"ko\">");
            out.println("<head>");
            out.println("	<title> 페이지 이동 </title>");
            out.println("	<meta http-equiv=\"refresh\" content=\"0;url=" + linkUrl + "\" />");
            out.println("</head>");
            out.println("<body>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException e) {
            logger.error(ExceptionUtil.addMessage(e, "메타태그를 이용한 페이지 이동 에러"));
        }
    }

    /**
     * <p>alert 메시지 전송 + 특정 페이지 이동 (meta 태그 적용)</p>
     *
     * @param response (HttpServletResponse 객체)
     * @param linkUrl  (이동할 페이지 URL)
     * @param message  (전송할 메시지)
     */
    public static void metaRefresh(HttpServletResponse response,
        String linkUrl, String message) {
        try {
            response.setContentType("text/html; charset=" + StaticConfig.DEFAULT_ENCODING);
            PrintWriter out = response.getWriter();
            out.println(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            out.println(
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ko\" lang=\"ko\">");
            out.println("<head>");
            out.println("	<title> 페이지 이동 </title>");
            out.println("	<meta http-equiv=\"refresh\" content=\"0;url=" + linkUrl + "\" />");
            out.println("	<script type=\"text/javascript\">");
            out.println("	<!--");
            out.println("		alert(\"" + message + "\");");
            out.println("	//-->");
            out.println("	</script>");
            out.println("</head>");
            out.println("<body>");
            out.println("</body>");
            out.println("</html>");
        } catch (IOException e) {
            logger.error(ExceptionUtil.addMessage(e, "메타태그를 이용한 페이지 이동 에러"));
        }
    }
}
