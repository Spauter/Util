package com.bloducspauter.excelutil.ewxce.read;


import com.bloducspauter.excelutil.origin.tool.ExcelValidationTool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类是用于读取标题并
 * @author Bloduc Spauter
 * @version 1.19
 */
public class TitleReader {

    public TitleReader(){
        throw new  UnsupportedOperationException("This class is not allowed to be instantiated");
    }

    /**
     * 读取标题并以Mao返回
     */
    public static Map<Integer, String> readTitle(Sheet sheet, int titleLine, int startCol, int endWithCol, ExcelValidationTool excelTool) {
        Map<Integer, String> titles = new HashMap<>();
        for (int col = startCol; col < endWithCol; col++) {
            Cell title = sheet.getRow(titleLine).getCell(col);
            if (title == null) {
                System.out.println(("Read title field"));
                String column = excelTool.convertToExcelColumn(col);
                throw new NullPointerException("Empty column in row " + (titleLine + 1) + ",column " + column);
            }
            String titleInfo = String.valueOf(title);
            titles.put(col, titleInfo);
        }
        return titles;
    }
}
