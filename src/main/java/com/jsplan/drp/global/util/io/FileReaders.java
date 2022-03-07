package com.jsplan.drp.global.util.io;

import com.jsplan.drp.global.util.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import com.jsplan.drp.global.config.StaticConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class : FileReaders
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 파일 읽기
 */
public class FileReaders {

    private static final Logger logger = LoggerFactory.getLogger(FileReaders.class);

    /**
     * <p>기본 Charset 으로 파일 읽기</p>
     *
     * <pre>
     * FileReaders.read(null)           = null
     * FileReaders.read("")             = null
     * FileReaders.read(!file.exists()) = null
     * FileReaders.read("D:/test.txt")  = 파일내용
     * </pre>
     *
     * @param fileLocation (파일 위치)
     * @return String (파일 내용)
     */
    public static String read(String fileLocation) {
        return read(fileLocation, StaticConfig.DEFAULT_ENCODING);
    }

    /**
     * <p>특정 Charset 으로 파일 읽기</p>
     *
     * <pre>
     * FileReaders.read(null, *)           = null
     * FileReaders.read("", *)             = null
     * FileReaders.read(!file.exists(), *) = null
     * FileReaders.read("D:/test.txt", *)  = 파일내용
     * </pre>
     *
     * @param fileLocation (파일 위치)
     * @param charset      (파일 캐릭터셋)
     * @return String (파일 내용)
     */
    public static String read(String fileLocation, String charset) {
        if (StringUtil.isBlank(fileLocation)) {
            return null;
        }
        return read(new File(fileLocation), charset);
    }

    /**
     * <p>기본 Charset 으로 파일 읽기</p>
     *
     * <pre>
     * FileReaders.read(null)           = null
     * FileReaders.read(!file.exists()) = null
     * FileReaders.read(File)           = 파일내용
     * </pre>
     *
     * @param file (파일 객체)
     * @return String (파일 내용)
     */
    public static String read(File file) {
        return read(file, StaticConfig.DEFAULT_ENCODING);
    }

    /**
     * <p>특정 Charset 으로 파일 읽기</p>
     *
     * <pre>
     * FileReaders.read(null, *)           = null
     * FileReaders.read(!file.exists(), *) = null
     * FileReaders.read(File, *)           = 파일내용
     * </pre>
     *
     * @param file    (파일 객체)
     * @param charset (파일 캐릭터셋)
     * @return String (파일 내용)
     */
    public static String read(File file, String charset) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        String line = null;
        try {
            if (file == null || !file.exists()) {
                logger.warn("파일이 존재하지 않습니다. [파일경로 : " + file.getPath() + "]");
                return null;
            }
            fis = new FileInputStream(file);
            if (StringUtil.isBlank(charset)) {
                isr = new InputStreamReader(fis);
            } else {
                isr = new InputStreamReader(fis, charset);
            }
            br = new BufferedReader(isr);
            sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error(e.toString(), e.fillInStackTrace());
            return null;
        } finally {
            line = null;
            sb = null;
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
