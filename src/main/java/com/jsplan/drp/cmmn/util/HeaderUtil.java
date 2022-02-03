package com.jsplan.drp.cmmn.util;

import com.jsplan.drp.cmmn.config.StaticConfig;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : HeaderUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : HTTP Header 처리
 */
public class HeaderUtil {

    private static final Logger logger = LoggerFactory.getLogger(HeaderUtil.class);

    /**
     * <p>브라우저 종류 구하기</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (사용자 브라우저 종류)
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "MSIE";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        }
        return "Firefox";
    }

    /**
     * <p>모바일 브라우저 여부 확인</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return boolean (모바일 브라우저 여부)
     */
    public static boolean isMobileBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        Pattern p1 = Pattern.compile(
            "(?i)(iPhone|iPad|iPod|Android|Windows CE|Windows Phone|BlackBerry|Symbian|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson)");
        Matcher m1 = p1.matcher(userAgent);
        Pattern p2 = Pattern.compile("(LG|SAMSUNG|Samsung)");
        Matcher m2 = p2.matcher(userAgent);
        return m1.find() || m2.find();
    }

    /**
     * <p>Host 동일 여부 확인</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return boolean (확인 결과)
     */
    public static boolean isEqualsHost(HttpServletRequest request) {
        return getRequestHost(request).equals(getRefererHost(request));
    }

    /**
     * <p>Host 변동 시 기본 페이지 이동</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @return boolean (확인 결과)
     */
    public static boolean checkEqualsHost(HttpServletRequest request,
        HttpServletResponse response) {
        return checkEqualsHost(request, response, "/");
    }

    /**
     * <p>Host 변동 시 기본 페이지 이동</p>
     *
     * @param request     (HttpServletRequest 객체)
     * @param response    (HttpServletResponse 객체)
     * @param redirectUrl (이동할 주소)
     * @return boolean (확인 결과)
     */
    public static boolean checkEqualsHost(HttpServletRequest request,
        HttpServletResponse response, String redirectUrl) {
        try {
            if (!isEqualsHost(request)) {
                response.sendRedirect(redirectUrl);
                return false;
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.addMessage(e, "현재 와 이전 페이지 Host 체크 후 페이지 이동 에러"));
            return false;
        }
        return true;
    }

    /**
     * <p>현재 페이지의 Host 구하기 (Scheme 제외)</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (현재 페이지 Host)
     */
    public static String getRequestHost(HttpServletRequest request) {
        return request.getHeader("host");
    }

    /**
     * <p>현재 페이지의 Host 구하기 (Scheme 포함)</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (현재 페이지 Host)
     */
    public static String getFullRequestHost(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getHeader("host");
    }

    /**
     * <p>현재 페이지의 URI 구하기</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (현재 페이지 URI)
     */
    public static String getRequestUri(HttpServletRequest request) {
        String requestUri = (String) request.getAttribute("javax.servlet.forward.request_uri");
        if (StringUtil.isBlank(requestUri)) {
            requestUri = request.getRequestURI();
        }
        return requestUri;
    }

    /**
     * <p>현재 페이지의 URL 구하기</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (현재 페이지 URL)
     */
    public static String getRequestUrl(HttpServletRequest request) {
        return getFullRequestHost(request) + getRequestUri(request);
    }

    /**
     * <p>현재 페이지의 전체 경로 구하기</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (현재 페이지 전체 경로)
     */
    public static String getFullRequestUrl(HttpServletRequest request) {
        return getFullRequestUrl(request, false);
    }

    /**
     * <p>현재 페이지의 전체 경로 구하기</p>
     *
     * @param request     (HttpServletRequest 객체)
     * @param isEncodeUrl (URL 인코딩 여부)
     * @return String (현재 페이지 전체 경로)
     */
    public static String getFullRequestUrl(HttpServletRequest request,
        boolean isEncodeUrl) {
        String httpUrl = getRequestUrl(request);
        if (request.getQueryString() != null) {
            httpUrl += "?" + EncodeUtil.getBytes(request.getQueryString());
        }
        if (isEncodeUrl) {
            httpUrl = EncodeUtil.encode(httpUrl);
        }
        return httpUrl;
    }

    /**
     * <p>이전 페이지의 Host 구하기 (Scheme 포함)</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (이전 페이지 Host)
     */
    public static String getFullRefererHost(HttpServletRequest request) {
        String refererUrl = request.getHeader("referer");
        if (!StringUtil.isBlank(refererUrl)) {
            int chkNum = refererUrl.contains("://") ? refererUrl.indexOf("://") + 3 : 0;
            int endNum = refererUrl.indexOf('/', chkNum);
            if (endNum >= 0) {
                return refererUrl.substring(0, endNum);
            } else {
                return refererUrl.substring(0);
            }
        }
        return "";
    }

    /**
     * <p>이전 페이지의 Host 구하기 (Scheme 제외)</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (이전 페이지 Host)
     */
    public static String getRefererHost(HttpServletRequest request) {
        String allRefererHost = getFullRefererHost(request);
        if (!StringUtil.isBlank(allRefererHost)) {
            int startNum =
                allRefererHost.contains("://") ? allRefererHost.indexOf("://") + 3 : 0;
            return allRefererHost.substring(startNum);
        }
        return "";
    }

    /**
     * <p>URI 경로에서 해당 위치 폴더명 구하기</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param location (해당 위치)
     * @return String (폴더명)
     */
    public static String getFolderName(HttpServletRequest request, int location) {
        return getFolderName(getRequestUri(request), location);
    }

    /**
     * <p>URI 경로에서 해당 위치 폴더명 구하기</p>
     *
     * @param strUri   (URI 문자열)
     * @param location (해당 위치)
     * @return String (폴더명)
     */
    public static String getFolderName(String strUri, int location) {
        String result = "";
        if (StringUtil.countMatches(strUri, "/") > location) {
            String[] splitUri = strUri.substring(1).split("/");
            result = splitUri[location - 1];
        }
        return result;
    }

    /**
     * <p>URI 경로에서 현재 파일명 구하기</p>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (파일명)
     */
    public static String getFileName(HttpServletRequest request) {
        return getFileName(getRequestUri(request));
    }

    /**
     * <p>URI 경로에서 현재 파일명 구하기</p>
     *
     * @param strUrl (URL 문자열)
     * @return String (파일명)
     */
    public static String getFileName(String strUrl) {
        return strUrl.substring(strUrl.lastIndexOf("/") + 1);
    }

    /**
     * <p>URL 경로에 http://[host] 추가</p>
     *
     * <pre>
     * HeaderUtil.addHttpHost(request, null)        = ""
     * HeaderUtil.addHttpHost(request, "  ")        = ""
     * HeaderUtil.addHttpHost(request, "http://*")  = "http://*"
     * HeaderUtil.addHttpHost(request, "https://*") = "http://*"
     * HeaderUtil.addHttpHost(request, "index.jsp") = "http://host/index.jsp"
     * </pre>
     *
     * @param request (HttpServletRequest 객체)
     * @param strUrl  (URL 문자열)
     * @return String (http://[host]를 추가한 URL)
     */
    public static String addHttpHost(HttpServletRequest request, String strUrl) {
        if (StringUtil.isBlank(strUrl)) {
            return "";
        }
        if (strUrl.indexOf("http://") == 0) {
            return strUrl;
        }
        if (strUrl.indexOf("https://") == 0) {
            return strUrl.replace("https://", "http://");
        }
        if (strUrl.indexOf("/") != 0) {
            strUrl = "/" + strUrl;
        }
        return "http://" + getRequestHost(request) + strUrl;
    }

    /**
     * <p>URL 경로에 https://[host] 추가</p>
     *
     * <pre>
     * HeaderUtil.addHttpsHost(request, null)        = ""
     * HeaderUtil.addHttpsHost(request, "  ")        = ""
     * HeaderUtil.addHttpsHost(request, "http://*")  = "https://*"
     * HeaderUtil.addHttpsHost(request, "https://*") = "https://*"
     * HeaderUtil.addHttpsHost(request, "index.jsp") = "https://host/index.jsp"
     * </pre>
     *
     * @param request (HttpServletRequest 객체)
     * @param strUrl  (URL 문자열)
     * @return String (https://[host]를 추가한 URL)
     */
    public static String addHttpsHost(HttpServletRequest request, String strUrl) {
        if (StringUtil.isBlank(strUrl)) {
            return "";
        }
        if (strUrl.indexOf("http://") == 0) {
            return strUrl.replace("http://", "https://");
        }
        if (strUrl.indexOf("https://") == 0) {
            return strUrl;
        }
        if (strUrl.indexOf("/") != 0) {
            strUrl = "/" + strUrl;
        }
        return "https://" + getRequestHost(request) + strUrl;
    }

    /**
     * <p>URL 경로에 http:// Scheme 추가</p>
     *
     * <pre>
     * HeaderUtil.addHttpScheme(null)              = ""
     * HeaderUtil.addHttpScheme("  ")              = ""
     * HeaderUtil.addHttpScheme("http://*")        = "http://*"
     * HeaderUtil.addHttpScheme("https://*")       = "https://*"
     * HeaderUtil.addHttpScheme("index.jsp")       = "http://index.jsp"
     * HeaderUtil.addHttpScheme("redpeople.co.kr") = "http://redpeople.co.kr"
     * </pre>
     *
     * @param object (URL 문자형 객체)
     * @return String (http:// Scheme를 추가한 URL)
     */
    public static String addHttpScheme(Object object) {
        if (StringUtil.isBlank(object)) {
            return "";
        }
        String str = StringUtil.clean(object);
        if (str.indexOf("http://") == 0) {
            return str;
        }
        if (str.indexOf("https://") == 0) {
            return str;
        }
        return "http://" + str;
    }

    /**
     * <p>Header 정보 출력</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     */
    public static void printHeader(HttpServletRequest request,
        HttpServletResponse response) {
        printHeader(request, response, false);
    }

    /**
     * <p>Header 정보 출력</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @param isClose  (스트림 close 여부)
     */
    public static void printHeader(HttpServletRequest request,
        HttpServletResponse response, boolean isClose) {
        try {
            response.setContentType("text/html; charset=" + StaticConfig.DEFAULT_ENCODING);
            PrintWriter out = response.getWriter();
            Enumeration<?> enumer = request.getHeaderNames();
            String name, value = "";
            StringBuilder sb = new StringBuilder();
            sb.append(
                "<table width=\"700px\" border=\"1px\" cellspacing=\"0px\" cellpadding=\"1px\" borderColorDark=\"white\" borderColorLight=\"silver\">\n");
            sb.append("	<caption><strong>HTTP 요청 헤더 정보</strong></caption>\n");
            sb.append("	<colgroup>\n");
            sb.append("	<col width=\"200px\" />\n");
            sb.append("	<col width=\"*\" />\n");
            sb.append("	</colgroup>\n");
            sb.append("	<thead style=\"height:30px;background:silver;\">\n");
            sb.append("		<tr>\n");
            sb.append("			<th scope=\"col\">Header Name</th>\n");
            sb.append("			<th scope=\"col\">Header Value</th>\n");
            sb.append("		</tr>\n");
            sb.append("	</thead>\n");
            sb.append("	<tbody>\n");
            sb.append("		<tr>\n");
            sb.append("			<td>Request URL</td>\n");
            sb.append("			<td>").append(getRequestUrl(request)).append("</td>\n");
            sb.append("		</tr>\n");
            sb.append("		<tr>\n");
            sb.append("			<td>Request URI</td>\n");
            sb.append("			<td>").append(getRequestUri(request)).append("</td>\n");
            sb.append("		</tr>\n");
            sb.append("		<tr>\n");
            sb.append("			<td>Request QueryString</td>\n");
            sb.append("			<td>").append(request.getQueryString()).append("</td>\n");
            sb.append("		</tr>\n");
            sb.append("		<tr>\n");
            sb.append("			<td>Request Method</td>\n");
            sb.append("			<td>").append(request.getMethod()).append("</td>\n");
            sb.append("		</tr>\n");
            while (enumer.hasMoreElements()) {
                name = (String) enumer.nextElement();
                value = request.getHeader(name);
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
            logger.error(ExceptionUtil.addMessage(e, "HTTP 요청 헤더 정보 출력 에러"));
        }
    }
}
