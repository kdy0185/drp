package com.jsplan.drp.cmmn.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : SystemUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : System 처리
 */
public class SystemUtil {

    private static final Logger logger = LoggerFactory.getLogger(SystemUtil.class);

    /**
     * <p>WAS 종류 구하기</p>
     *
     * <pre>
     * SystemUtil.getWas() = "tomcat"
     * SystemUtil.getWas() = "jeus"
     * SystemUtil.getWas() = "weblogic"
     * SystemUtil.getWas() = "other"
     * </pre>
     *
     * @return String (WAS 종류)
     */
    public static String getWas() {
        if (System.getProperty("tomcat.home") != null
            || System.getProperty("catalina.home") != null) {
            return "tomcat";
        } else if (System.getProperty("jeus.home") != null) {
            return "jeus";
        } else if (System.getProperty("weblogic.Name") != null) {
            return "weblogic";
        } else {
            return "other";
        }
    }

    /**
     * <p>웹서버 IP 주소 구하기</p>
     *
     * <pre>
     * SystemUtil.getServerIp() = &quot;74.125.71.106&quot;
     * </pre>
     *
     * @return String (웹서버 IP)
     */
    public static String getServerIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(
                ExceptionUtil.addMessage(e, "SystemUtil.getServerIp 에서 UnknownHostException 발생"));
            return "";
        }
    }

    /**
     * <p>현재 Host IP 주소 구하기 (서버 이중화시 L4의 IP 주소 리턴)</p>
     *
     * <pre>
     * SystemUtil.getHostIp(request) = &quot;74.125.71.106&quot;
     * </pre>
     *
     * @param request (HttpServletRequest 객체)
     * @return String (현재 Host의 IP 주소)
     */
    public static String getHostIp(HttpServletRequest request) {
        return getHostIp(request.getServerName());
    }

    /**
     * <p>현재 Host IP 주소 구하기 (서버 이중화시 L4의 IP 주소 리턴)</p>
     *
     * <pre>
     * SystemUtil.getHostIp("www.redpeolpe.co.kr") = "119.206.195.246"
     * SystemUtil.getHostIp("www.google.co.kr")    = "74.125.71.106"
     * SystemUtil.getHostIp("www.naver1.com")      = ""
     * </pre>
     *
     * @param hostName (Host명)
     * @return String (해당 Host의 IP 주소)
     */
    public static String getHostIp(String hostName) {
        try {
            InetAddress inetAddress = InetAddress.getByName(hostName);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(
                ExceptionUtil.addMessage(e, "SystemUtil.getHostIp 에서 UnknownHostException 발생"));
            return "";
        }
    }
}
