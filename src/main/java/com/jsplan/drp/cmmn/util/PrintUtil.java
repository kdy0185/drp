package com.jsplan.drp.cmmn.util;

import com.jsplan.drp.cmmn.config.StaticConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : PrintUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : Print 처리
 */
public class PrintUtil {

    private static final Logger logger = LoggerFactory.getLogger(PrintUtil.class);

    /**
     * <p>데이터 화면 출력</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     */
    public static void printRequest(HttpServletRequest request,
        HttpServletResponse response) {
        printRequest(request, response, false);
    }

    /**
     * <p>데이터 화면 출력</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @param isClose  (스트림 close 여부)
     */
    public static void printRequest(HttpServletRequest request,
        HttpServletResponse response, boolean isClose) {
        try {
            response.setContentType("text/html; charset=" + StaticConfig.DEFAULT_ENCODING);
            PrintWriter out = response.getWriter();
            Enumeration<?> enumer = request.getParameterNames();
            String name, value = "";
            StringBuilder sb = new StringBuilder();
            sb.append("<table width=\"700px\" border=\"1px\" cellspacing=\"0px\" cellpadding=\"1px\" borderColorDark=\"white\" borderColorLight=\"silver\">\n");
            sb.append("	<caption><strong>Request QueryString</strong></caption>\n");
            sb.append("	<colgroup>\n");
            sb.append("	<col width=\"200px\" />\n");
            sb.append("	<col width=\"*\" />\n");
            sb.append("	</colgroup>\n");
            sb.append("	<thead style=\"height:30px;background:silver;\">\n");
            sb.append("		<tr>\n");
            sb.append("			<th scope=\"col\">Parameter Name</th>\n");
            sb.append("			<th scope=\"col\">Parameter Value</th>\n");
            sb.append("		</tr>\n");
            sb.append("	</thead>\n");
            sb.append("	<tbody>\n");
            while (enumer.hasMoreElements()) {
                name = (String) enumer.nextElement();
                value = request.getParameter(name);
                sb.append("		<tr>\n");
                sb.append("			<td>").append(name).append("</td>\n");
                sb.append("			<td>").append(value).append("</td>\n");
                sb.append("		</tr>\n");
            }
            sb.append("	</tbody>\n");
            sb.append("</table>\n");
            out.println(sb.toString());
            if (isClose) {
                out.close();
            }
        } catch (IOException e) {
            logger.error(ExceptionUtil.addMessage(e, "GET/POST 방식으로 전달된 데이터 화면에 출력 에러"));
        }
    }

    /**
     * <p>Console 출력</p>
     *
     * @param str (출력할 문자)
     */
    public static void printConsole(String str) {
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println(str);
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
    }

    /**
     * <p>Console 출력 (Map 데이터)</p>
     *
     * @param dataMap (출력할 Map 데이터)
     */
    public static void printConsole(Map<String, String> dataMap) {
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
        for (Iterator<String> iter = dataMap.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next();
            String value = dataMap.get(key);
            System.out.println(key + " = " + value);
            if (iter.hasNext()) {
                System.out.println("--------------------------------------------");
            }
        }
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
        System.out.println("+---------------------------------------------------------------------------------------------------------------------------------+");
    }

}
