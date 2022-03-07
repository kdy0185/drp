package com.jsplan.drp.global.util.io;

import com.jsplan.drp.global.config.StaticConfig;
import com.jsplan.drp.global.util.ExceptionUtil;
import com.jsplan.drp.global.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : FileUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 파일 처리
 */

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * <p>해당 경로의 파일 삭제</p>
     *
     * @param filePath (삭제할 파일의 경로)
     * @param fileName (삭제할 파일의 이름)
     * @return boolean (파일 삭제 성공 여부)
     */
    public static boolean delete(String filePath, String fileName) {
        return delete(new File(StaticConfig.UPLOAD_PATH + filePath, fileName));
    }

    /**
     * <p>해당 위치의 파일 또는 디렉토리 삭제 (디렉토리는 비어있을 경우에만 삭제)</p>
     *
     * @param location (삭제할 파일 또는 디렉토리 위치)
     * @return boolean (파일 또는 디렉토리 삭제 성공 여부)
     */
    public static boolean delete(String location) {
        return delete(new File(location));
    }

    /**
     * <p>파일 또는 디렉토리 삭제 (디렉토리는 비어있을 경우에만 삭제)</p>
     *
     * @param file (삭제할 파일 또는 디렉토리 객체)
     * @return boolean (파일 또는 디렉토리 삭제 성공 여부)
     */
    public static boolean delete(File file) {
        return delete(file, false);
    }

    /**
     * <p>파일 또는 디렉토리 삭제</p>
     *
     * @param file        (삭제할 파일 또는 디렉토리 객체)
     * @param isDeleteAll (디렉토리에 있는 모든 것 포함 삭제여부)
     * @return boolean (파일 또는 디렉토리 삭제 성공 여부)
     */
    public static boolean delete(File file, boolean isDeleteAll) {
        if (file.exists()) {
            if (file.isFile() || (file.isDirectory() && !isDeleteAll)) {
                return file.delete();
            } else {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].isFile()) {
                        fileList[i].delete();
                    } else {
                        delete(fileList[i], true);
                    }
                }
                return file.delete();
            }
        } else {
            logger.warn("파일 또는 디렉토리가 존재하지 않습니다. [경로 : " + file.getPath() + "]");
            return false;
        }
    }

    /**
     * <p>디렉토리 구분자 변경</p>
     *
     * <pre>
     * FileUtil.toSystemPath(null)      = ""
     * FileUtil.toSystemPath("")        = ""
     * FileUtil.toSystemPath("  ")      = ""
     * FileUtil.toSystemPath("\aa/bb\") = "/aa/bb/"  (유닉스계열 서버 일때)
     * FileUtil.toSystemPath("\aa/bb\") = "\aa\bb\"  (윈도우계열 서버 일때)
     * </pre>
     *
     * @param filePath (변경할 파일 경로)
     * @return String (변경 후 파일 경로)
     */
    public static String toSystemPath(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return "";
        }
        filePath = filePath.replaceAll("\\\\", java.io.File.separator);
        filePath = filePath.replaceAll("/", java.io.File.separator);
        return filePath;
    }

    /**
     * <p>디렉토리 구분자 변경 ("\" → "/")</p>
     *
     * <pre>
     * FileUtil.toUnixPath(null)      = ""
     * FileUtil.toUnixPath("")        = ""
     * FileUtil.toUnixPath("  ")      = ""
     * FileUtil.toUnixPath("\aa\bb\") = "/aa/bb/"
     * FileUtil.toUnixPath("/cc\dd/") = "/cc/dd/"
     * </pre>
     *
     * @param filePath (변경할 파일 경로)
     * @return String (변경 후 파일 경로)
     */
    public static String toUnixPath(String filePath) {
        return StringUtil.isBlank(filePath) ? "" : filePath.replaceAll("\\\\", "/");
    }

    /**
     * <p>디렉토리 구분자 변경 ("/" → "\")</p>
     *
     * <pre>
     * FileUtil.toWindowsPath(null)      = ""
     * FileUtil.toWindowsPath("")        = ""
     * FileUtil.toWindowsPath("  ")      = ""
     * FileUtil.toWindowsPath("/aa/bb/") = "\aa\bb\"
     * FileUtil.toWindowsPath("\cc/dd\") = "\cc\dd\"
     * </pre>
     *
     * @param filePath (변경할 파일 경로)
     * @return String (변경 후 파일 경로)
     */
    public static String toWindowsPath(String filePath) {
        return StringUtil.isBlank(filePath) ? "" : filePath.replaceAll("/", "\\\\");
    }

    /**
     * <p>파일 경로 정리</p>
     *
     * <pre>
     * FileUtil.cleanPath(null)            = ""
     * FileUtil.cleanPath("")              = ""
     * FileUtil.cleanPath("  ")            = ""
     * FileUtil.cleanPath("D:/aa\bb.jsp")  = "D:/aa/bb.jsp"
     * FileUtil.cleanPath("D:/aa//bb.jsp") = "D:/aa/bb.jsp"
     * FileUtil.cleanPath("D:\aa\\bb.jsp") = "D:\aa\bb.jsp"
     * FileUtil.cleanPath("/D:/aa/bb.jsp") = "D:/aa/bb.jsp"
     * FileUtil.cleanPath("/aa//bb.jsp")   = "/aa/bb.jsp"
     * FileUtil.cleanPath("\aa/bb.jsp")    = "/aa/bb.jsp"
     * </pre>
     *
     * @param filePath (정리할 파일 경로)
     * @return String (정리 후 파일 경로)
     */
    public static String cleanPath(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return "";
        }
        if (filePath.contains("/") && filePath.contains("\\")) {
            filePath = toUnixPath(filePath);
        }
        if (filePath.contains(":") && (filePath.startsWith("/") || filePath.startsWith(
            "\\"))) {
            filePath = filePath.replaceAll("^/*", "");
            filePath = filePath.replaceAll("^\\\\*", "");
        }
        filePath = filePath.replaceAll("/+", "/");
        filePath = filePath.replaceAll("\\\\+", "\\\\");
        return filePath;
    }

    /**
     * <p>리소스로 URL 객체 읽기</p>
     *
     * <pre>
     * FileUtil.getResource(null)                  = null
     * FileUtil.getResource("")                    = URL 객체 (Root Path)
     * FileUtil.getResource("  ")                  = URL 객체 (Root Path)
     * FileUtil.getResource("/WEB-INF/")           = URL 객체 (Root Path + "/WEB-INF/")
     * FileUtil.getResource("/assets/js/")         = URL 객체 (Root Path + "/assets/js/")
     * FileUtil.getResource("/global.properties")  = URL 객체 (Root Path + "/global.properties")
     * FileUtil.getResource("classpath:")          = URL 객체 (Root Path + "/WEB-INF/classes/")
     * FileUtil.getResource("classpath:/kr/co/")   = URL 객체 (Root Path + "/WEB-INF/classes/kr/co/")
     * FileUtil.getResource("classpath:/test.txt") = URL 객체 (Root Path + "/WEB-INF/classes/test.txt")
     * </pre>
     *
     * @param location (검색할 리소스 위치)
     * @return URL (URL 객체)
     */
    public static URL getResource(String location) {
        if (location == null) {
            return null;
        }
        location = StringUtil.trim(location);
        if (location.startsWith("classpath:")) {
            location = location.substring("classpath:".length());
            if (!location.startsWith("/")) {
                location = "/" + location;
            }
        } else {
            if (!location.startsWith("/")) {
                location = "/" + location;
            }
            location = "/../.." + location;
        }
        return FileUtil.class.getResource(location);
    }

    /**
     * <p>리소스로 경로 읽기</p>
     *
     * <pre>
     * FileUtil.getResourcePath(null)                  = null
     * FileUtil.getResourcePath("")                    = Root Path
     * FileUtil.getResourcePath("  ")                  = Root Path
     * FileUtil.getResourcePath("/WEB-INF/")           = Root Path + "/WEB-INF/"
     * FileUtil.getResourcePath("/assets/js/")         = Root Path + "/assets/js/"
     * FileUtil.getResourcePath("/global.properties")  = Root Path + "/global.properties"
     * FileUtil.getResourcePath("classpath:")          = Root Path + "/WEB-INF/classes/"
     * FileUtil.getResourcePath("classpath:/kr/co/")   = Root Path + "/WEB-INF/classes/kr/co/"
     * FileUtil.getResourcePath("classpath:/test.txt") = Root Path + "/WEB-INF/classes/test.txt"
     * </pre>
     *
     * @param location (검색할 리소스 위치)
     * @return String (리소스 경로)
     */
    public static String getResourcePath(String location) {
        try {
            return cleanPath(getResource(location).getPath());
        } catch (Exception e) {
            String resourcePath = "";
            if (location.startsWith("classpath:")) {
                location = location.substring("classpath:".length());
                resourcePath =
                    cleanPath(FileUtil.class.getResource("/").getPath()) + (location.startsWith("/")
                        ? location.substring(1) : location);
            } else {
                resourcePath = cleanPath(FileUtil.class.getResource("/../../").getPath()) + (
                    location.startsWith("/") ? location.substring(1) : location);
            }
            logger.error(ExceptionUtil.addMessage(e,
                "java.io.FileNotFoundException: 리소스가 존재하지 않거나 액세스할 수 없습니다. [" + resourcePath
                    + "]"));
            return null;
        }
    }

    /**
     * <p>클래스 기준 리소스로 URL 객체 읽기</p>
     *
     * <pre>
     * FileUtil.getResource(null, null)              = null
     * FileUtil.getResource(null, *)                 = null
     * FileUtil.getResource(Class, null)             = null
     * FileUtil.getResource(Class, "")               = URL 객체 (Root Path + Class Package Path)
     * FileUtil.getResource(Class, "  ")             = URL 객체 (Root Path + Class Package Path)
     * FileUtil.getResource(Class, "mapping.xml")    = URL 객체 (Root Path + Class Package Path + "/mapping.xml")
     * FileUtil.getResource(Class, "./mapping.xml")  = URL 객체 (Root Path + Class Package Path + "/mapping.xml")
     * FileUtil.getResource(Class, "../poi/poi.xml") = URL 객체 (Root Path + Class Package Path + "/../poi/poi.xml")
     * FileUtil.getResource(Class, "/log4j.xml")     = URL 객체 (Root Path + "/WEB-INF/classes/log4j.xml")
     * FileUtil.getResource(Class, "kr/co/")         = URL 객체 (Root Path + Class Package Path + "/kr/co/")
     * FileUtil.getResource(Class, "/kr/co/")        = URL 객체 (Root Path + "/WEB-INF/classes/kr/co/")
     * </pre>
     *
     * @param clazz    (Class 객체)
     * @param location (검색할 리소스 위치)
     * @return URL (URL 객체)
     */
    public static URL getResource(Class<?> clazz, String location) {
        if (clazz == null || location == null) {
            return null;
        }
        return clazz.getResource(StringUtil.trim(location));
    }

    /**
     * <p>클래스 기준 리소스로 경로 읽기</p>
     *
     * <pre>
     * FileUtil.getResourcePath(null, null)              = null
     * FileUtil.getResourcePath(null, *)                 = null
     * FileUtil.getResourcePath(Class, null)             = null
     * FileUtil.getResourcePath(Class, "")               = Root Path + Class Package Path
     * FileUtil.getResourcePath(Class, "  ")             = Root Path + Class Package Path
     * FileUtil.getResourcePath(Class, "mapping.xml")    = Root Path + Class Package Path + "/mapping.xml"
     * FileUtil.getResourcePath(Class, "./mapping.xml")  = Root Path + Class Package Path + "/mapping.xml"
     * FileUtil.getResourcePath(Class, "../poi/poi.xml") = Root Path + Class Package Path + "/../poi/poi.xml"
     * FileUtil.getResourcePath(Class, "/log4j.xml")     = Root Path + "/WEB-INF/classes/log4j.xml"
     * FileUtil.getResourcePath(Class, "kr/co/")         = Root Path + Class Package Path + "/kr/co/"
     * FileUtil.getResourcePath(Class, "/kr/co/")        = Root Path + "/WEB-INF/classes/kr/co/"
     * </pre>
     *
     * @param clazz    (Class 객체)
     * @param location (검색할 리소스 위치)
     * @return String (리소스 경로)
     */
    public static String getResourcePath(Class<?> clazz, String location) {
        try {
            return cleanPath(getResource(clazz, location).getPath());
        } catch (Exception e) {
            String resourcePath = cleanPath(clazz.getResource("").getPath()) + location;
            if (location.startsWith("/")) {
                resourcePath = cleanPath(clazz.getResource("/").getPath()) + location.substring(1);
            }
            logger.error(ExceptionUtil.addMessage(e,
                "java.io.FileNotFoundException: 리소스가 존재하지 않거나 액세스할 수 없습니다. [" + resourcePath
                    + "]"));
            return null;
        }
    }

    /**
     * <p>파일 확장자 리턴</p>
     *
     * <pre>
     * FileUtil.getFileExt(null)       = ""
     * FileUtil.getFileExt("")         = ""
     * FileUtil.getFileExt("alin")     = ""
     * FileUtil.getFileExt("alin.jpg") = "jpg"
     * FileUtil.getFileExt("alin.GIF") = "GIF"
     * </pre>
     *
     * @param fileName (파일명)
     * @return String (파일의 확장자)
     */
    public static String getFileExt(String fileName) {
        if (StringUtil.isBlank(fileName)) {
            return "";
        }
        if (fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * <p>파일 확장자 리턴 (소문자)</p>
     *
     * <pre>
     * FileUtil.getFileExtLowerCase(null)       = ""
     * FileUtil.getFileExtLowerCase("")         = ""
     * FileUtil.getFileExtLowerCase("alin")     = ""
     * FileUtil.getFileExtLowerCase("alin.jpg") = "jpg"
     * FileUtil.getFileExtLowerCase("alin.GIF") = "gif"
     * </pre>
     *
     * @param fileName (파일명)
     * @return String (파일의 확장자)
     */
    public static String getFileExtLowerCase(String fileName) {
        return getFileExt(fileName).toLowerCase();
    }

    /**
     * <p>파일 종류 리턴</p>
     *
     * <pre>
     * FileUtil.getFileCategory(null)        = "unknown"
     * FileUtil.getFileCategory("")          = "unknown"
     * FileUtil.getFileCategory("alin")      = "unknown"
     * FileUtil.getFileCategory("alin.egg")  = "unknown"
     * FileUtil.getFileCategory("alin.jpg")  = "jpg"
     * FileUtil.getFileCategory("alin.jpeg") = "jpg"
     * FileUtil.getFileCategory("alin.GIF")  = "gif"
     * </pre>
     *
     * @param fileName (파일명)
     * @return String (파일의 종류)
     */
    public static String getFileCategory(String fileName) {
        String extension = getFileExtLowerCase(fileName);
        if (!StringUtil.isBlank(extension)) {
            if (StringUtil.isContain("alz", extension, "|")) {
                return "alz";
            } else if (StringUtil.isContain("avi|divx|asx|asf|wmv|mpg|mpeg|mpeg4|flv|mov",
                extension, "|")) {
                return "avi";
            } else if (StringUtil.isContain("bmp", extension, "|")) {
                return "bmp";
            } else if (StringUtil.isContain("cab", extension, "|")) {
                return "cab";
            } else if (StringUtil.isContain("doc|docx", extension, "|")) {
                return "doc";
            } else if (StringUtil.isContain("exe", extension, "|")) {
                return "exe";
            } else if (StringUtil.isContain("fla", extension, "|")) {
                return "fla";
            } else if (StringUtil.isContain("gif", extension, "|")) {
                return "gif";
            } else if (StringUtil.isContain("htm|html", extension, "|")) {
                return "html";
            } else if (StringUtil.isContain("hwp", extension, "|")) {
                return "hwp";
            } else if (StringUtil.isContain("jpg|jpeg", extension, "|")) {
                return "jpg";
            } else if (StringUtil.isContain("aac|ac3|ape|flac|mp3|ogg|wma", extension, "|")) {
                return "mp3";
            } else if (StringUtil.isContain("mp4", extension, "|")) {
                return "mp4";
            } else if (StringUtil.isContain("pdf", extension, "|")) {
                return "pdf";
            } else if (StringUtil.isContain("png", extension, "|")) {
                return "png";
            } else if (StringUtil.isContain("ppt|pptx", extension, "|")) {
                return "ppt";
            } else if (StringUtil.isContain("psd", extension, "|")) {
                return "psd";
            } else if (StringUtil.isContain("swf", extension, "|")) {
                return "swf";
            } else if (StringUtil.isContain("txt", extension, "|")) {
                return "txt";
            } else if (StringUtil.isContain("xls|xlsx", extension, "|")) {
                return "xls";
            } else if (StringUtil.isContain("xml", extension, "|")) {
                return "xml";
            } else if (StringUtil.isContain("zip", extension, "|")) {
                return "zip";
            }
        }
        return "unknown";
    }

    /**
     * <p>디렉토리 내 파일 리스트 읽기</p>
     *
     * @param dirPath (전체 디렉토리)
     * @return List<File> (파일 리스트)
     */
    public static List<File> getDirFileList(String dirPath) {
        // 디렉토리 파일 리스트
        List<File> dirFileList = null;

        // 파일 목록을 요청한 디렉토리를 가지고 파일 객체를 생성함
        File dir = new File(dirPath);

        // 디렉토리가 존재한다면
        if (dir.exists()) {
            // 파일 목록을 구함
            File[] files = dir.listFiles();

            // 파일 배열을 파일 리스트로 변화함
            dirFileList = Arrays.asList(files);
        }

        return dirFileList;
    }

    /**
     * <p>파일 존재 여부 확인</p>
     *
     * @param isLivefile (전체 디렉토리 포함 파일명)
     * @return Boolean (파일 존재 여부)
     */
    public static Boolean fileIsLive(String isLivefile) {
        File file = new File(isLivefile);

        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>파일 생성</p>
     *
     * @param makeFileName (전체 디렉토리 포함 파일명)
     */
    public static void fileMake(String makeFileName) {
        File file = new File(makeFileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>파일 삭제</p>
     *
     * @param deleteFileName (전체 디렉토리 포함 파일명)
     */
    public static void fileDelete(String deleteFileName) {
        File file = new File(deleteFileName);
        file.delete();
    }

    /**
     * <p>파일 복사</p>
     *
     * @param inDir       (디렉토리 풀패스 정보 포함 복사 대상 파일)
     * @param outDir      (디렉토리 풀패스 정보 포함 복사 타겟 파일)
     * @param inFileName  (복사 대상 파일명)
     * @param outFileName (복사 타겟 파일명)
     */
    public static void fileCopy(String inDir, String outDir, String inFileName,
        String outFileName) {
        if (StringUtil.isBlank(inFileName) || StringUtil.isBlank(outFileName)) {
            return;
        }
        try {
            inFileName = inDir + "\\" + inFileName;
            outFileName = outDir + "\\" + outFileName;

            FileInputStream fis = new FileInputStream(inFileName);
            FileOutputStream fos = new FileOutputStream(outFileName);

            int data = 0;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }

            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>단일 파일 이동</p>
     *
     * @param inDir    (디렉토리 풀패스 정보 포함 복사 대상 파일)
     * @param outDir   (디렉토리 풀패스 정보 포함 복사 타겟 파일)
     * @param fileName (이동 대상 파일명)
     */
    public static void fileMove(String inDir, String outDir, String fileName) {
        if (fileName == null || "".equals(fileName)) {
            return;
        }
        try {
            String inFileName = inDir + "\\" + fileName;
            String outFileName = outDir + "\\" + fileName;

            FileInputStream fis = new FileInputStream(inFileName);
            FileOutputStream fos = new FileOutputStream(outFileName);

            int data = 0;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }
            fis.close();
            fos.close();

            // 복사한 뒤 원본 파일 삭제
            fileDelete(inFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>디렉토리 내 파일 이동</p>
     *
     * @param inDir  (디렉토리 풀패스 정보 포함 복사 대상 파일)
     * @param outDir (디렉토리 풀패스 정보 포함 복사 타겟 파일)
     * @param gubun  ('del', 'cpy', 'mov')
     */
    public static void dirHandling(String inDir, String outDir, String gubun) {
        try {
            // 이동 전 폴더에 있는 파일들을 읽는다.
            List<File> dirList = getDirFileList(inDir);

            // 폴더의 사이즈만큼 돌면서 파일을 이동시킨다.
            for (int i = 0; i < dirList.size(); i++) {
                // i번째 저장되어 있는 파일을 불러온다.
                String fileName = dirList.get(i).getName();

                if ("del".equals(gubun)) {
                    fileDelete(inDir + "\\" + fileName);
                } else if ("cpy".equals(gubun)) {
                    fileCopy(inDir, outDir, fileName, fileName);
                } else if ("mov".equals(gubun)) {
                    fileMove(inDir, outDir, fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
