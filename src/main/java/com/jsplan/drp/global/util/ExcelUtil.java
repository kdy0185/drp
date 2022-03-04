package com.jsplan.drp.global.util;


import com.jsplan.drp.global.obj.ExcelVO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @Class : ExcelUtil
 * @Author : KDW
 * @Date : 2022-01-20
 * @Description : 엑셀 업로드, 다운로드 처리
 */
public class ExcelUtil extends AbstractXlsView {

    /**
     * <p>업로드 데이터 조회</p>
     *
     * @param filePath      (경로 포함 파일)
     * @param startRowIndex (시작 행 Index)
     * @return List (업로드 데이터)
     * @throws Exception throws Exception
     */
    public static List<ExcelVO> uploadExcelList(String filePath, int startRowIndex)
        throws Exception {
        List<ExcelVO> list = new ArrayList<ExcelVO>();
        FileInputStream fis = null;
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        ExcelVO vo;

        try {
            fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheetAt(0);

            // sheet.getPhysicalNumberOfRows();에서 row를 제대로 가져오지 못하는 문제가 발생하여 수정
            int lastRowNumCnt =
                sheet.getLastRowNum() + 1; // 마지막 rowNum + 1 = sheet.getPhysicalNumberOfRows() 와 동일
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            // 만일 phsicalNumberOfRows에서 row를 제대로 가져오지 못한다면 sheet.getLastRowNum()+1로 대체
            int rowTotalNum = Math.max(lastRowNumCnt, physicalNumberOfRows);

            for (int rowIndex = 0; rowIndex < rowTotalNum; rowIndex++) {
                // 실제 데이터가 있는 row부터 조회
                if (rowIndex >= startRowIndex) {
                    row = sheet.getRow(rowIndex);
                    vo = new ExcelVO();
                    String value = "";

                    // 해당 row의 첫번째 cell 값이 비어있지 않은 경우
                    if (row.getCell(0) != null && !StringUtil.isBlank(
                        changeStringCell(row.getCell(0)))) {
                        for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                            if (row.getCell(cellIndex) != null && !StringUtil.isBlank(
                                changeStringCell(row.getCell(cellIndex)))) { // 각 셀마다 null 체크
                                value = changeStringCell(row.getCell(cellIndex));

                                //해당 cell에 따른 vo 세팅
                                switch (cellIndex) {
                                    case 0: vo.setVal0(value); break; case 1: vo.setVal1(value); break;
                                    case 2: vo.setVal2(value); break; case 3: vo.setVal3(value); break;
                                    case 4: vo.setVal4(value); break; case 5: vo.setVal5(value); break;
                                    case 6: vo.setVal6(value); break; case 7: vo.setVal7(value); break;
                                    case 8: vo.setVal8(value); break; case 9: vo.setVal9(value); break;
                                    case 10: vo.setVal10(value); break; case 11: vo.setVal11(value); break;
                                    case 12: vo.setVal12(value); break; case 13: vo.setVal13(value); break;
                                    case 14: vo.setVal14(value); break; case 15: vo.setVal15(value); break;
                                    case 16: vo.setVal16(value); break; case 17: vo.setVal17(value); break;
                                    case 18: vo.setVal18(value); break; case 19: vo.setVal19(value); break;
                                    case 20: vo.setVal20(value); break; case 21: vo.setVal21(value); break;
                                    case 22: vo.setVal22(value); break; case 23: vo.setVal23(value); break;
                                    case 24: vo.setVal24(value); break; case 25: vo.setVal25(value); break;
                                    case 26: vo.setVal26(value); break; case 27: vo.setVal27(value); break;
                                    case 28: vo.setVal28(value); break; case 29: vo.setVal29(value); break;
                                    default: break;
                                }
                            }
                        }
                        list.add(vo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * <p>셀 데이터 String 변환</p>
     *
     * @param cell (셀 객체)
     * @return String (변환 데이터)
     * @throws Exception throws Exception
     */
    private static String changeStringCell(Cell cell) throws Exception {
        String cellValue = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_BLANK:
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cellValue = cell.getStringCellValue() + "";
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue() + "";
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue = cell.getErrorCellValue() + "";
                break;
            default:
                cellValue = "";
                break;
        }

        return cellValue;
    }

    /**
     * <p>엑셀 양식 설정</p>
     *
     * @param map      (엑셀 속성 정보)
     * @param workbook (HSSFWorkbook 객체)
     * @param request  (HttpServletRequest 객체)
     * @param response (HttpServletResponse 객체)
     * @throws Exception throws Exception
     */
    @Override
    protected void buildExcelDocument(Map<String, Object> map,
        Workbook workbook, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
        HSSFFont headerFont = (HSSFFont) workbook.createFont();
        HSSFFont bodyFont = (HSSFFont) workbook.createFont();
        HSSFSheet sheet = null;
        HSSFRow row = null;
        HSSFCell cell = null;

        // 단일 시트
        @SuppressWarnings("unchecked")
        List<String> colName = (List<String>) map.get("colName");
        @SuppressWarnings("unchecked")
        List<Integer> colWidth = (List<Integer>) map.get("colWidth");
        @SuppressWarnings("unchecked")
        List<String[]> colValue = (List<String[]>) map.get("colValue");
        @SuppressWarnings("unchecked")
        List<Integer> colHeight = (List<Integer>) map.get("colHeight");
        // 복수 시트
        @SuppressWarnings("unchecked")
        List<String> sheetNames = (List<String>) map.get("sheetNames");
        @SuppressWarnings("unchecked")
        Map<String, Object> colNames = (Map<String, Object>) map.get("colNames");
        @SuppressWarnings("unchecked")
        Map<String, Object> colWidths = (Map<String, Object>) map.get("colWidths");
        @SuppressWarnings("unchecked")
        Map<String, Object> colValues = (Map<String, Object>) map.get(
            "colValues"); // 시트명 별로 List<String[]>를 저장

        // 엑셀 속성 설정
        String excelName = EncodeUtil.toKorean(map.get("excelName"));
        String curDate = DateUtil.toDateFormat(new Date(), "yyyyMMdd_HHmmss");
        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"" + excelName + "_" + curDate + ".xls\"");

        // Cell Style 설정
        CellStyle headerStyle = setHeaderStyle(workbook, palette, headerFont);
        CellStyle bodyStyle = setBodyStyle(workbook, palette, bodyFont);

        if (sheetNames == null) { // 단일 시트
            sheet = (HSSFSheet) workbook.createSheet("Sheet");
            // 상단 메뉴명 생성
            row = sheet.createRow(0);
            for (int i = 0; i < colName.size(); i++) {
                // 컬럼 width 설정
                sheet.setColumnWidth(i, colWidth.get(i));

                // Header Cell Insert
                cell = row.createCell(i);
                cell.setCellValue(new HSSFRichTextString(colName.get(i)));
                cell.setCellStyle(headerStyle);
            }

            // Body Cell Insert
            int startRow = 1; // Body Cell 시작 Row 설정
            for (int i = 0; i < colValue.size(); i++) {
                row = sheet.createRow(i + startRow);
                for (int j = 0; j < colValue.get(i).length; j++) {
                    cell = row.createCell(j);

                    String cellValue = colValue.get(i)[j];
                    if ("〓".equals(StringUtil.left(cellValue, 1))) {
                        cell.setCellFormula(cellValue.substring(1));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(cellValue));
                    }
                    cell.setCellStyle(bodyStyle);
                }
                if (colHeight != null) {
                    row.setHeight((short) colHeight.get(i).intValue());
                }
            }
        } else { // 복수 시트
            HSSFSheet[] sheets = new HSSFSheet[sheetNames.size()];
            for (int i = 0; i < sheetNames.size(); i++) {
                sheets[i] = (HSSFSheet) workbook.createSheet(sheetNames.get(i));
                @SuppressWarnings("unchecked")
                List<String> colNameList = (List<String>) colNames.get(sheetNames.get(i));
                @SuppressWarnings("unchecked")
                List<Integer> colWidthList = (List<Integer>) colWidths.get(sheetNames.get(i));
                @SuppressWarnings("unchecked")
                List<String[]> colVal = (List<String[]>) colValues.get(sheetNames.get(i));

                // 상단 메뉴명 생성
                row = sheets[i].createRow(0);
                for (int j = 0; j < colNameList.size(); j++) {
                    // 컬럼 width 설정
                    sheets[i].setColumnWidth(j, colWidthList.get(j));

                    // Header Cell Insert
                    cell = row.createCell(j);
                    cell.setCellValue(new HSSFRichTextString(colNameList.get(j)));
                    cell.setCellStyle(headerStyle);
                }

                // Body Cell Insert
                int startRow = 1; // Body Cell 시작 Row 설정
                for (int j = 0; j < colVal.size(); j++) {
                    row = sheets[i].createRow(j + startRow);

                    for (int k = 0; k < colVal.get(j).length; k++) {
                        cell = row.createCell(k);
                        cell.setCellValue(new HSSFRichTextString(colVal.get(j)[k]));
                        cell.setCellStyle(bodyStyle);
                    }
                }
            }
        }
    }

    /**
     * <p>Header Style 설정</p>
     *
     * @param workbook (HSSFWorkbook 객체)
     * @param palette  (HSSFPalette 객체)
     * @param font     (Font 객체)
     * @return CellStyle (CellStyle 객체)
     * @throws Exception throws Exception
     */
    public static CellStyle setHeaderStyle(Workbook workbook,
        HSSFPalette palette, Font font) throws Exception {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setWrapText(true);
        palette.setColorAtIndex(HSSFColor.SKY_BLUE.index, (byte) 64, (byte) 67, (byte) 128);
        font.setFontName("나눔고딕");
        font.setFontHeightInPoints((short) 9);
        font.setColor(HSSFColor.WHITE.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        headerStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        headerStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        headerStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        headerStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerStyle.setFont(font);

        return headerStyle;
    }

    /**
     * <p>Body Style 설정</p>
     *
     * @param workbook (HSSFWorkbook 객체)
     * @param palette  (HSSFPalette 객체)
     * @param font     (Font 객체)
     * @return CellStyle (CellStyle 객체)
     * @throws Exception throws Exception
     */
    public static CellStyle setBodyStyle(Workbook workbook,
        HSSFPalette palette, Font font) throws Exception {
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setWrapText(true);
        palette.setColorAtIndex(HSSFColor.GREY_50_PERCENT.index, (byte) 242, (byte) 242,
            (byte) 242);
        palette.setColorAtIndex(HSSFColor.GREY_80_PERCENT.index, (byte) 128, (byte) 128,
            (byte) 128);
        font.setFontName("나눔고딕");
        font.setFontHeightInPoints((short) 9);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        bodyStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        bodyStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        bodyStyle.setBorderTop(CellStyle.BORDER_THIN);
        bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
        bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
        bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
        bodyStyle.setTopBorderColor(HSSFColor.GREY_80_PERCENT.index);
        bodyStyle.setRightBorderColor(HSSFColor.GREY_80_PERCENT.index);
        bodyStyle.setBottomBorderColor(HSSFColor.GREY_80_PERCENT.index);
        bodyStyle.setLeftBorderColor(HSSFColor.GREY_80_PERCENT.index);
        bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
        bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        bodyStyle.setFont(font);

        return bodyStyle;
    }

}
