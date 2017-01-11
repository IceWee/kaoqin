package bing.util;

import bing.Constants;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel单元格样式工厂
 * 
 * @author IceWee
 */
public class ExcelCellStyleUtils {
    
    /**
     * 生成备注单元格样式
     * 
     * @param workbook
     * @return 
     */
    public static CellStyle createRemarkCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true); // 自动换行
        cellBorderDecorate(cellStyle); // 边框
        cellStyle.setAlignment(HorizontalAlignment.LEFT); // 水平居左  
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        Font font = workbook.createFont();
        font.setFontName("新宋体");
        font.setFontHeightInPoints((short) 9);
        cellStyle.setFont(font);
        return cellStyle;
    }
    
    /**
     * 生成表头样式
     * 
     * @param workbook
     * @return 
     */
    public static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellBorderDecorate(cellStyle);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        Font font = workbook.createFont();
        font.setBold(true);  // 加粗
        cellStyle.setFont(font);
        return cellStyle;
    }
    
    /**
     * 生成单元格样式
     * 
     * @param workbook
     * @param cellStyleCode
     * @return 
     */
    public static CellStyle createCellStyle(Workbook workbook, int cellStyleCode) {
        CellStyle cellStyle = workbook.createCellStyle();   
        cellBorderDecorate(cellStyle);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        switch(cellStyleCode) {
            case Constants.CELL_STYLE_DEFAULT:
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                break;
            case Constants.CELL_STYLE_YELLOW:
                cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                break;
            case Constants.CELL_STYLE_RED:
                cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                break;
        }
        return cellStyle;
    }
    
    /**
     * 设置单元格边框
     * 
     * @param cellStyle 
     */
    static void cellBorderDecorate(CellStyle cellStyle) {
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
    }
    
}
