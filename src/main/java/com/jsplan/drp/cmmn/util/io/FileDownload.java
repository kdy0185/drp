package com.jsplan.drp.cmmn.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jsplan.drp.cmmn.util.EncodeUtil;
import com.jsplan.drp.cmmn.util.ExceptionUtil;
import com.jsplan.drp.cmmn.util.HeaderUtil;
import com.jsplan.drp.cmmn.util.JsPrinter;
import com.jsplan.drp.cmmn.util.StringUtil;
import com.jsplan.drp.cmmn.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Class : FileDownload
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 파일 다운로드
 */
public class FileDownload {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.http.encoding.charset}")
    private String defaultEncoding;

    /**
     * <p>브라우저별 Content-Disposition 지정</p>
     *
     * @param request      (HttpServletRequest 객체)
     * @param response     (HttpServletResponse 객체)
     * @param originalName (원본 파일명)
     */
    public void setDisposition(HttpServletRequest request,
        HttpServletResponse response, String originalName) {
        String browser = HeaderUtil.getBrowser(request);
        String encodeFileName = null;
        if (StringUtil.isContain("MSIE", browser, "|")) {
            encodeFileName = EncodeUtil.encode(originalName).replaceAll("\\+", "%20");
        } else if (StringUtil.isContain("Firefox|Opera|Safari", browser, "|")) {
            if ("jeus".equals(SystemUtil.getWas())) {
                encodeFileName = "\"" + originalName + "\"";
            } else {
                encodeFileName =
                    "\"" + EncodeUtil.getBytes(originalName, defaultEncoding, "8859_1") + "\"";
            }
        } else if (StringUtil.isContain("Chrome", browser, "|")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < originalName.length(); i++) {
                char c = originalName.charAt(i);
                if (c > '~') {
                    sb.append(EncodeUtil.encode("" + c));
                } else {
                    sb.append(c);
                }
            }
            encodeFileName = sb.toString();
        } else {
            encodeFileName =
                "\"" + EncodeUtil.getBytes(originalName, defaultEncoding, "8859_1") + "\"";
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + encodeFileName);
    }

    /**
     * <p>파일 다운로드 처리</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @param fileMap  (파일정보 Map class)
     */
    public void download(HttpServletRequest request,
        HttpServletResponse response, Map<String, Object> fileMap) {
        String savePath = StringUtil.clean(fileMap.get("savePath"));
        String saveName = StringUtil.clean(fileMap.get("saveName"));
        String originalName = StringUtil.clean(fileMap.get("originalName"));
        String contentType = StringUtil.clean(fileMap.get("contentType"));
        if (!StringUtil.isBlank(savePath) && !StringUtil.isBlank(saveName) && !StringUtil.isBlank(
            originalName) && !StringUtil.isBlank(contentType)) {
            download(request, response, savePath, saveName, originalName, contentType);
        } else if (!StringUtil.isBlank(savePath) && !StringUtil.isBlank(saveName)
            && !StringUtil.isBlank(originalName)) {
            download(request, response, savePath, saveName, originalName);
        } else if (!StringUtil.isBlank(savePath) && !StringUtil.isBlank(saveName)) {
            download(request, response, savePath, saveName);
        } else {
            JsPrinter.back(response, "다운로드 받을 파일정보가 없습니다.");
        }
    }

    /**
     * <p>파일 다운로드 처리</p>
     *
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @param savePath (파일 저장경로)
     * @param saveName (저장 파일명)
     */
    public void download(HttpServletRequest request,
        HttpServletResponse response, String savePath, String saveName) {
        download(request, response, savePath, saveName, saveName);
    }

    /**
     * <p>파일 다운로드 처리</p>
     *
     * @param request      (HttpServletRequest 객체)
     * @param response     (HttpServletResponse 객체)
     * @param savePath     (파일 저장경로)
     * @param saveName     (저장 파일명)
     * @param originalName (원본 파일명)
     */
    public void download(HttpServletRequest request,
        HttpServletResponse response, String savePath, String saveName,
        String originalName) {
        download(request, response, savePath, saveName, originalName, null);
    }

    /**
     * <p>파일 다운로드 처리</p>
     *
     * @param request      (HttpServletRequest 객체)
     * @param response     (HttpServletResponse 객체)
     * @param savePath     (파일 저장경로)
     * @param saveName     (저장 파일명)
     * @param originalName (원본 파일명)
     * @param contentType  (파일 콘텐츠타입)
     */
    public void download(HttpServletRequest request,
        HttpServletResponse response, String savePath, String saveName,
        String originalName, String contentType) {
        if (StringUtil.isBlank(savePath)) {
            JsPrinter.back(response, "다운로드 파일경로가 지정되지 않았습니다.");
        } else if (StringUtil.isContainKeywords(savePath, "./|../|.\\|..\\|\r|\n|\r\n", "|")) {
            JsPrinter.back(response, "잘못된 다운로드 파일경로가 지정 되었습니다.");
        } else if (StringUtil.isBlank(saveName)) {
            JsPrinter.back(response, "파일명이 지정되지 않았습니다.");
        } else if (StringUtil.isContainKeywords(saveName, "./|../|.\\|..\\|\r|\n|\r\n", "|")) {
            JsPrinter.back(response, "잘못된 파일명이 지정 되었습니다.");
        } else if (StringUtil.isBlank(originalName)) {
            JsPrinter.back(response, "다운로드 파일명이 지정되지 않았습니다.");
        } else if (StringUtil.isContainKeywords(originalName, "./|../|.\\|..\\|\r|\n|\r\n", "|")) {
            JsPrinter.back(response, "잘못된 다운로드 파일명이 지정 되었습니다.");
        } else if (StringUtil.isContainKeywords(contentType, "\r|\n|\r\n", "|")) {
            JsPrinter.back(response, "잘못된 파일타입이 지정 되었습니다.");
        } else {
            File downloadFile = new File(savePath, saveName);
            if (downloadFile.exists() && downloadFile.length() > 0) {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    response.reset();
                    response.setContentType(
                        StringUtil.isBlank(contentType) ? "application/octet-stream" : contentType);
                    response.setContentLength((int) downloadFile.length());
                    setDisposition(request, response, originalName);
                    bis = new BufferedInputStream(new FileInputStream(downloadFile));
                    bos = new BufferedOutputStream(response.getOutputStream());
                    int read = 0;
                    byte[] b = new byte[2048];
                    while ((read = bis.read(b)) != -1) {
                        bos.write(b, 0, read);
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtil.addMessage(e,
                        "첨부파일 다운로드 실패 - [파일경로 : " + downloadFile.toString() + "]"));
                    JsPrinter.back(response, "첨부파일 다운로드 실패");
                } finally {
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                logger.warn("다운로드 받을 파일이 존재하지 않습니다. [파일경로 : " + downloadFile.toString() + "]");
                JsPrinter.back(response, "다운로드 받을 파일이 존재하지 않습니다.");
            }
        }
    }
}
