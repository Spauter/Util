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
     * 读取文件将结果存入二维数组中，在提供有参构造后可使用
     * @return Object[][]
     * @throws IOException IO流异常
     */
    Object[][] readToArray() throws IOException;

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
}
