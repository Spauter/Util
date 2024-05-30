package com.bloducspauter.excel.tool;

import com.bloducspauter.origin.service.ValidationTool;


/**
 * 关于Excel工具
 * @author Bloduc Spauter
 *
 */
public abstract class ExcelValidationTool extends ValidationTool {
    /**
     * 返回具体的表格所在列数
     * @param columnNumber 列数
     * @return excel表格显示列数
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
