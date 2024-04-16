package com.bloducspauter.excel.tool;

import com.bloducspauter.origin.tool.MyTool;

public abstract class ExcelTool extends MyTool {
    /**
     * @param columnNumber 列数
     * @return
     */
    abstract public String convertToExcelColumn(int columnNumber);
}
