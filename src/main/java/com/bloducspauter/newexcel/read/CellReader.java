package com.bloducspauter.newexcel.read;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 用于处理单元格数据的类
 * @author Bloduc Spauter
 * @version 1.19
 */
public class CellReader {

    /**
     * 这段代码用于从单元格（Cell）中获取值，并根据单元格类型进行相应的处理。
     * @param dateformat 日期格式
     * @return 单元格值
     */
    public static Object getCellValue(Sheet sheet, int row, int col, String dateformat) {
        Cell cell = sheet.getRow(row).getCell(col);
        String cellValue = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                // 处理日期格式、时间格式
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf;
                    // 如果单元格是日期类型，则使用指定的日期格式进行格式化
                    if (DateUtil.isADateFormat(-1, cell.getCellStyle().getDataFormat() + "")) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    } else {
                        // 如果单元格是日期类型，则使用指定的日期格式进行格式化
                        sdf = new SimpleDateFormat(dateformat);
                    }
                    cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else if ("".equals(cell.getCellStyle().getDataFormatString())) {
                    //转换成整型
                    DecimalFormat df = new DecimalFormat("#");
                    cellValue = df.format(cell.getNumericCellValue());
                } else {
                    //转换成整型
                    cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case FORMULA:
                cellValue = cell.getCellFormula();
                break;
            case BLANK:
                cellValue = "";
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
        }
        return cellValue;
    }
}
