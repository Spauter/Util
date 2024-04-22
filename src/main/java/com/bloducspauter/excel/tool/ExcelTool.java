package com.bloducspauter.excel.tool;

import com.bloducspauter.origin.tool.MyTool;

public abstract class ExcelTool extends MyTool {
    /**
     * 用来把列数转化相应的表格的列
     * @param columnNumber 列数
     * @return String 表格列
     */
    abstract public String convertToExcelColumn(int columnNumber);
}