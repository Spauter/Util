package com.bloducspauter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 读取文件
 * @author Bloduc Spauter
 * @version 1.6
 */
public interface ReadFile extends FileReadAndOutPutUtil{
    /**
     *  读取文件并把结果保存到List集合中
     * @param path 文件路径
     * @return List
     * @throws IOException IO流异常
     */
    List<Map<String, Object>> readToList(String path) throws IOException;

    /**
     * 读取文件并把结果保存到List集合中
     * @param file 文件
     * @return List
     * @throws IOException IO流异常
     */
    List<Map<String, Object>> readToList(File file) throws IOException;

    /**
     *  提供文件后直接读取,不需要额外输入文件路径
     * @return List
     * @throws IOException IO流异常
     */
    List<Map<String, Object>> readToList() throws IOException;

    /**
     * 读取文件将结果存入二维数组中
     * @param path 文件路径
     * @return Object[][]
     * @throws IOException IO流异常
     */
    Object[][] readToArray(String path) throws IOException;

    /**
     * 读取文件将结果存入二维数组中
     * @param file 读取文件将结果存入二维数组中
     * @return Object[][]
     * @throws IOException IO流异常
     */
    Object[][] readToArray(File file) throws IOException;

    /**
     *
     * @return
     * @throws IOException
     */
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
