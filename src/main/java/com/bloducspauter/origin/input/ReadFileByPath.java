package com.bloducspauter.origin.input;

import com.bloducspauter.origin.FileReadAndOutPutUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * 通过{@link String}文件路径作为参数来读取Excel表格
 * @author Bloduc Spauter
 * @since 1.18
 */
public interface ReadFileByPath extends FileReadAndOutPutUtil {
    /**
     *  读取文件并把结果保存到List集合中
     * @param path 文件路径
     * @return List
     * @throws IOException IO流异常
     */
    List<Map<String, Object>> readToList(String path) throws IOException;

    /**
     * 读取文件将结果存入二维数组中
     * @param path 文件路径
     * @return Object[][]
     * @throws IOException IO流异常
     */
    Object[][] readToArray(String path) throws IOException;

}
