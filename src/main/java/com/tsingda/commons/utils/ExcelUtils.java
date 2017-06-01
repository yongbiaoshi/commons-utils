package com.tsingda.commons.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtils {

    public static final String EXCEL_SUFFIX = ".xlsx";
    public static final Integer DEFAULT_ROW_ACCESS_WINDOW_SIZE = 1000;

    public static SXSSFWorkbook createWorkBook() {
        SXSSFWorkbook wb = new SXSSFWorkbook(DEFAULT_ROW_ACCESS_WINDOW_SIZE);
        return wb;
    }

    public static SXSSFWorkbook createWorkBook(int rowAccessWindowSize) {
        return new SXSSFWorkbook(rowAccessWindowSize);
    }

    public static void setTitles(SXSSFSheet sheet, List<String> titles) {
        SXSSFRow row = sheet.createRow(0);
        int size = titles.size();
        for (int i = 0; i < size; i++) {
            SXSSFCell cell = row.createCell(i);
            cell.setCellValue(titles.get(i));
        }
    }

    public static void putRow(SXSSFRow row, CellStyle dateCellStyle, List<String> headers,
            Map<String, Object> infoMap) {

        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String head = headers.get(i);
            SXSSFCell cell = row.createCell(i);
            Object value = infoMap.get(head);
            if (value != null) {
                if (value instanceof Date) {
                    cell.setCellValue((Date) value);
                    cell.setCellStyle(dateCellStyle);
                } else if (value instanceof Number) {
                    if (value instanceof Double || value instanceof BigDecimal) {
                        cell.setCellValue(Double.parseDouble(value.toString()));
                    } else if (value instanceof Float) {
                        cell.setCellValue(Float.parseFloat(value.toString()));
                    } else if (value instanceof Long) {
                        cell.setCellValue(Long.parseLong(value.toString()));
                    } else {
                        cell.setCellValue(Integer.parseInt(value.toString()));
                    }
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        }
    }

    /**
     * 把数据放入Sheet中，起始行默认为第二行
     * 
     * @param sheet ExcelSheet
     * @param headers 头
     * @param list 数据
     */
    public static void putMapToSheet(SXSSFSheet sheet, List<String> headers, List<Map<String, Object>> list) {
        putMapToSheet(sheet, headers, list, 1);
    }

    /**
     * 把数据放入Sheet中
     * 
     * @param sheet ExcelSheet
     * @param headers 头
     * @param list 数据
     * @param rowNum 起始行号，第一行为：0
     */
    public static void putMapToSheet(SXSSFSheet sheet, List<String> headers, List<Map<String, Object>> list,
            int rowNum) {
        SXSSFWorkbook wb = sheet.getWorkbook();
        CellStyle dateCellStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        dateCellStyle.setDataFormat(format.getFormat("yyyy/m/d h:mm"));
        int total = list.size();
        for (int i = 0; i < total; i++) {
            Map<String, Object> infoMap = list.get(i);
            SXSSFRow row = sheet.createRow(i + rowNum);
            putRow(row, dateCellStyle, headers, infoMap);
        }
    }

    public static File excelToFile(SXSSFWorkbook workBook, String dir, String fileName)
            throws FileNotFoundException, IOException {
        File d = new File(dir);
        if (!d.exists() || !d.isDirectory()) {
            d.mkdirs();
        }
        File file = new File(dir + "/" + fileName + EXCEL_SUFFIX);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        workBook.write(fos);
        fos.close();
        return file;
    }

}
