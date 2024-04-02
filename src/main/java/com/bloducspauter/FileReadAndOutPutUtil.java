package com.bloducspauter;


/**
 * @author Bloduc Spauer
 * @version 1.14.514
*/
public interface FileReadAndOutPutUtil {
    String SUFFIX_1 = ".xlsx";
    String SUFFIX_2 = ".xls";
    String SUFFIX_3=".docx";
    String SUFFIX_4=".doc";
    String SUFFIX_5=".txt";
    String SHEET_NAME = "Sheet1";

    /**
     * 返回总行数
     * @return
     */
    int getMaxRows();

    /**
     * 返回总列数
     * @return
     */
    int getMaxCols();
    /**
     * 清除所有数据
     */
    void clearAll();
}
