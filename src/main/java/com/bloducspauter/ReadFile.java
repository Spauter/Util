package com.bloducspauter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReadFile extends FileReadAndOutPutUtil{
    /**
     * 返回List
     *
     * @param path
     * @return
     * @throws
     * @throws IOException
     */
    List<Map<String, Object>> readToList(String path) throws IOException;


    List<Map<String, Object>> readToList(File file) throws IOException;

    List<Map<String, Object>> readToList() throws IOException;

    Object[][] readToArray(String path) throws IOException;

    Object[][] readToArray(File file) throws IOException;

    Object[][] readToArray() throws IOException;

    /**
     * 设置某一列作为标题
     * @param titleLine
     */
    void setTitleLine(int titleLine);

    /**
     * 设置起始读取的行数
     * @param startRow
     */
    void setStartRow(int startRow);

    /**
     * 设置起始读取的列数
     * @param startCol
     */
    void setStartCol(int startCol);

    /**
     * 设置截至读取的列数
     * @param endWithCol
     */
    void setEndWithCol(int endWithCol);

    /**
     * 设置截至读取的行数
     * @param endWithRow
     */
    void setEndWithRow(int endWithRow);
}
