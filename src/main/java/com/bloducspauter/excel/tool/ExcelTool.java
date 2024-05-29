package com.bloducspauter.excel.tool;


import com.bloducspauter.origin.service.ValidationTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bloducspauter.origin.Util.*;

/**
 * 表格文档检查工具
 *
 * @author Bloduc Spauter
 * @version 1.16
 */
public class ExcelTool extends ValidationTool {
    private final List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void checkSuffix(String path) {


    }



    public void checkTitleLine(int titleLine, int maxRow) throws IndexOutOfBoundsException {
        if (titleLine > maxRow || titleLine < 0) {
            throw new IndexOutOfBoundsException(titleLine);
        }
    }


    public void checkRowCol(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol) {
        if (startCol < 0 || startRow < 0 || endWithRow < 0 || endWithCol < 0) {
            throw new IndexOutOfBoundsException(-1);
        }
        if (startCol > endWithCol || startRow > endWithRow) {
            throw new IndexOutOfBoundsException(startCol > endWithCol ? startCol : startRow);
        }
        if (startCol > maxCol || startRow > maxRow) {
            throw new IndexOutOfBoundsException(startCol > maxCol ? startCol : startRow);
        }
        if (endWithCol > maxCol || endWithRow > maxRow) {
            throw new IndexOutOfBoundsException(endWithCol > maxCol ? endWithCol : endWithRow);
        }
    }

    /**
     * @param columnNumber
     * @return
     */

    public String convertToExcelColumn(int columnNumber) {
        StringBuilder columnName = new StringBuilder();

        while (columnNumber > 0) {
            // 计算余数
            int remainder = (columnNumber - 1) % 26;
            // 将余数对应的字母添加到结果字符串的开头
            columnName.insert(0, (char) ('A' + remainder));
            // 更新整数，除以26向下取整
            columnNumber = (columnNumber - 1) / 26;
        }
        return columnName.toString();
    }

}
