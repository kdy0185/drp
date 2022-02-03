package com.jsplan.drp.cmmn.util.io;

import com.jsplan.drp.cmmn.util.StringUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Class : FileCmprs
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 파일 압축
 */
public class FileCmprs {

    private static final int COMPRESSION_LEVEL = 3;
    private static final int BUFFER_SIZE = 1024 * 2;

    /**
     * <p>폴더 지정 압축</p>
     *
     * @param sourcePath (압축 대상 디렉토리)
     * @param output     (저장 zip 파일 이름)
     * @throws Exception throws Exception
     */
    public static void zip(String sourcePath, String output) throws Exception {
        // 압축 대상(sourcePath)이 디렉토리나 파일이 아니면 리턴한다.
        File sourceFile = new File(sourcePath);
        if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
            throw new Exception("압축 대상의 파일을 찾을 수가 없습니다.");
        }

        // output 의 확장자가 zip이 아니면 리턴한다.
        if (!(StringUtil.substringAfterLast(output, ".")).equalsIgnoreCase("zip")) {
            throw new Exception("압축 후 저장 파일명의 확장자를 확인하세요");
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(output); // FileOutputStream
            bos = new BufferedOutputStream(fos); // BufferedStream
            zos = new ZipOutputStream(bos); // ZipOutputStream
            zos.setLevel(COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8
            zipEntry(sourceFile, sourcePath, zos); // Zip 파일 생성
            zos.finish(); // ZipOutputStream finish
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * <p>압축</p>
     *
     * @param sourceFile (압축 대상 파일)
     * @param sourcePath (압축 대상 디렉토리)
     * @param zos        (ZipOutputStream 객체)
     * @throws Exception throws Exception
     */
    private static void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos)
        throws Exception {
        // sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
        if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) { // .metadata 디렉토리 return
                return;
            }
            File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos); // 재귀 호출
            }
        } else { // sourceFile 이 디렉토리가 아닌 경우
            BufferedInputStream bis = null;
            try {
                String sFilePath = sourceFile.getPath();
                sourcePath = sourcePath.replaceAll("\\\\\\\\", "\\\\");
                String zipEntryName = sFilePath.substring(sourcePath.length() + 1,
                    sFilePath.length());
                bis = new BufferedInputStream(new FileInputStream(sourceFile));
                ZipEntry zentry = new ZipEntry(zipEntryName);
                zentry.setTime(sourceFile.lastModified());
                zos.putNextEntry(zentry);

                byte[] buffer = new byte[BUFFER_SIZE];
                int cnt = 0;
                while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, cnt);
                }
                zos.closeEntry();
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }
        }
    }

    /**
     * <p>압축 해제</p>
     *
     * @param zipFile             (압축 풀 Zip 파일)
     * @param targetDir           (압축 푼 파일이 들어간 디렉토리)
     * @param fileNameToLowerCase (파일명을 소문자로 바꿀지 여부)
     * @throws Exception throws Exception
     */
    public static void unzip(File zipFile, File targetDir,
        boolean fileNameToLowerCase) throws Exception {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zentry = null;

        try {
            fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(fis);

            while ((zentry = zis.getNextEntry()) != null) {
                String fileNameToUnzip = zentry.getName();
                if (fileNameToLowerCase) {
                    fileNameToUnzip = fileNameToUnzip.toLowerCase();
                }

                File targetFile = new File(targetDir, fileNameToUnzip);

                if (zentry.isDirectory()) {// Directory 인 경우
                    File saveFolder = new File(targetFile.getAbsolutePath());
                    if (!saveFolder.exists() || saveFolder.isFile()) {
                        saveFolder.mkdirs();
                    }
                } else { // File 인 경우
                    // parent Directory 생성
                    File saveFolder = new File(targetFile.getParent());
                    if (!saveFolder.exists() || saveFolder.isFile()) {
                        saveFolder.mkdirs();
                    }
                    unzipEntry(zis, targetFile);
                }
            }
        } finally {
            if (zis != null) {
                zis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * <p>압축 해제</p>
     *
     * @param zis        (ZipInputStream 객체)
     * @param targetFile (압축 푼 파일)
     * @return File
     * @throws Exception throws Exception
     */
    protected static File unzipEntry(ZipInputStream zis, File targetFile)
        throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return targetFile;
    }
}
