package com.bloducspauter.origin.init;

public interface DetermineReadRange {
    /**
     * 设置某一列作为标题,默认为0(第一行)
     * @param titleLine 被设置为标题的哪一行
     */
    void setTitleLine(int titleLine);

    /**
     * 设置起始读取的行数
     * @param startRow  起始行
     */
    void setStartRow(int startRow);

    /**
     * 设置起始读取的列数
     * @param startCol 起始列
     */
    void setStartCol(int startCol);

    /**
     * 设置截至读取的列数
     * @param endWithCol 截止列
     */
    void setEndWithCol(int endWithCol);

    /**
     * 设置截至读取的行数
     * @param endWithRow 截止行
     */
    void setEndWithRow(int endWithRow);

    /**
     * 设置被读取的Sheet;
     * @param var Sheet
     */
    void readSheetAt(int var);
}
