package com.bloducspauter.excel.read;

import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.excel.tool.ExcelValidationTool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bloduc Spauter
 */
public class TitleReader {
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
