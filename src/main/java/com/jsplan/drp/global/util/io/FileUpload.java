package com.jsplan.drp.global.util.io;

import com.jsplan.drp.global.util.DateUtil;
import com.jsplan.drp.global.util.ExceptionUtil;
import com.jsplan.drp.global.util.NumberUtil;
import com.jsplan.drp.global.util.StringUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Class : FileUpload
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 파일 업로드
 */
public class FileUpload {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Calendar calendar = Calendar.getInstance();

    @Value("${project.path.upload}")
    private String uploadPath;

    /**
     * <p>단일 파일 업로드 (확장자 제외)</p>
     *
     * @param attachFile (MultipartFile 객체)
     * @return Map<String, Object> (저장한 파일 정보)
     */
    public Map<String, Object> upload(MultipartFile attachFile) {
        return upload(attachFile, null);
    }

    /**
     * <p>단일 파일 업로드 (확장자 제외)</p>
     *
     * @param attachFile  (MultipartFile 객체)
     * @param fileSummary (첨부파일 설명)
     * @return Map<String, Object> (저장한 파일 정보)
     */
    public Map<String, Object> upload(MultipartFile attachFile,
        String fileSummary) {
        return upload(attachFile, fileSummary, false);
    }

    /**
     * <p>단일 파일 업로드</p>
     *
     * @param attachFile    (MultipartFile 객체)
     * @param fileSummary   (첨부파일 설명)
     * @param isSaveWithExt (파일 저장 시 파일 확장자 포함 여부)
     * @return Map<String, Object> (저장한 파일 정보)
     */
    public Map<String, Object> upload(MultipartFile attachFile,
        String fileSummary, boolean isSaveWithExt) {
        Map<String, Object> fileMap = null;
        if (attachFile != null && !attachFile.isEmpty()) {
            fileMap = upload(attachFile, null, null, isSaveWithExt);
            if (fileMap != null && !fileMap.isEmpty()) {
                fileMap.put("fileSummary", StringUtil.clean(fileSummary));
            }
        }
        return fileMap;
    }

    /**
     * <p>다중 파일 업로드 (확장자 제외)</p>
     *
     * @param attachFile (MultipartFile 객체 리스트)
     * @return List<Map> (저장한 파일 정보 리스트)
     */
    public List<Map<String, Object>> upload(List<MultipartFile> attachFile) {
        return upload(attachFile, null);
    }

    /**
     * <p>다중 파일 업로드 (확장자 제외)</p>
     *
     * @param attachFile  (MultipartFile 객체 리스트)
     * @param fileSummary (첨부파일 설명 리스트)
     * @return List<Map> (저장한 파일 정보 리스트)
     */
    public List<Map<String, Object>> upload(List<MultipartFile> attachFile,
        String[] fileSummary) {
        return upload(attachFile, fileSummary, false);
    }

    /**
     * <p>다중 파일 업로드</p>
     *
     * @param attachFile    (MultipartFile 객체 리스트)
     * @param fileSummary   (첨부파일 설명 리스트)
     * @param isSaveWithExt (파일 저장 시 파일 확장자 포함 여부)
     * @return List<Map> (저장한 파일 정보 리스트)
     */
    public List<Map<String, Object>> upload(List<MultipartFile> attachFile,
        String[] fileSummary, boolean isSaveWithExt) {
        List<Map<String, Object>> attachFileList = null;
        if (attachFile != null && attachFile.size() > 0) {
            attachFileList = new ArrayList<Map<String, Object>>();
            MultipartFile file = null;
            Map<String, Object> fileMap = null;
            for (int i = 0; i < attachFile.size(); i++) {
                file = attachFile.get(i);
                if (file != null && !file.isEmpty()) {
                    fileMap = upload(file, null, null, isSaveWithExt);
                    if (fileMap != null && !fileMap.isEmpty()) {
                        if (fileSummary != null && fileSummary.length > i) {
                            fileMap.put("fileSummary", StringUtil.clean(fileSummary[i]));
                        } else {
                            fileMap.put("fileSummary", "");
                        }
                        attachFileList.add(fileMap);
                    }
                }
            }
        }
        return attachFileList;
    }

    /**
     * <p>파일 업로드 처리</p>
     *
     * @param file          (MultipartFile 객체)
     * @param savePath      (저장할 파일 경로)
     * @param saveName      (저장할 파일 이름)
     * @param isSaveWithExt (파일 저장 시 파일 확장자 포함 여부)
     * @return Map<String, Object> (저장한 파일 정보)
     */
    public Map<String, Object> upload(MultipartFile file, String savePath,
        String saveName, boolean isSaveWithExt) {
        Map<String, Object> fileMap = null;
        File saveFolder = null;
        try {
            if (file != null && !file.isEmpty()) {
                if (StringUtil.isBlank(savePath)) {
                    savePath = calendar.get(Calendar.YEAR) + File.separator + StringUtil.addZero(
                        calendar.get(Calendar.MONTH) + 1, 2) + File.separator;
                    saveFolder = new File(uploadPath + savePath);
                } else {
                    saveFolder = new File(savePath);
                }
                saveFolder.mkdirs();
                if (StringUtil.isBlank(saveName)) {
                    saveName = DateUtil.getTimeStamp() + NumberUtil.getRandomString(1, 999);
                }
                if (isSaveWithExt) {
                    saveName += "." + FileUtil.getFileExt(file.getOriginalFilename());
                }

                // 첨부파일 저장
                file.transferTo(new File(saveFolder, saveName));
                // 첨부파일 정보 생성
                fileMap = new HashMap<String, Object>();
                fileMap.put("gubun", file.getName());
                fileMap.put("savePath", savePath);
                fileMap.put("saveName", saveName);
                fileMap.put("originalName", file.getOriginalFilename());
                fileMap.put("fileExt", FileUtil.getFileExt(file.getOriginalFilename()));
                fileMap.put("fileSize", file.getSize());
                fileMap.put("contentType", file.getContentType());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.addMessage(e, "첨부파일 업로드 실패"));
        } finally {
            saveFolder = null;
        }
        return fileMap;
    }
}
