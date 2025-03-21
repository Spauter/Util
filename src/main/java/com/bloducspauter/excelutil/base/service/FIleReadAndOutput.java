package com.bloducspauter.excelutil.base.service;

import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 输入输出顶级接口
 * @author Bloduc Spauter
 * @version 1.18.2
 */
public interface FIleReadAndOutput {
    /**
     * 读取文档路径,将结果存储到List集合中
     *
     * @param path 文件路径
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    List<Map<String, Object>> readToList(String path) throws Exception;

    /**
     * 读取文档,将结果存储到List集合中
     *
     * @param file 文件
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    default List<Map<String, Object>> readToList(File file) throws Exception {
        return readToList(file.getAbsolutePath());
    }

    /**
     * 读取文档路径,将结果存储到L一个二位数组中，但是此方法无法得到标题信息
     *
     * @param path 文件路径
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    Object[][] readToArray(String path) throws Exception;

    /**
     * 读取文档路径,将结果存储到L一个二位数组中，但是此方法无法得到标题信息
     *
     * @param file 文件
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    default Object[][] readToArray(File file) throws Exception {
        return readToArray(file.getAbsolutePath());
    }

    /**
     * 提供一些数据保存到本地文件中
     *
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    void output(List<Map<String, Object>> list, String path) throws Exception;

    /**
     * 提供一些数据保存到本地文件中
     *
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    default void output(List<Map<String, Object>> list, File file) throws Exception {
        output(list, file.getAbsolutePath());
    }

    /**
     * 提供一些数据保存到本地文件中
     *
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    void output(Object[][] obj, String[] title, File file) throws Exception;

    /**
     * 提供一些数据保存到本地文件中
     *
     * @throws IOException              IO流异常，比如{@code FileNotFoundException}
     * @throws UnsupportedFileException 当文件格式不支持时抛出磁异常
     */
    default void output(Object[][] obj, String[] title, String path) throws Exception {
        File file = new File(path);
        output(obj, title, file);
    }

}
