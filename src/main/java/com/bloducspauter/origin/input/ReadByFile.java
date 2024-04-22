package com.bloducspauter.origin.input;

import com.bloducspauter.origin.FileReadAndOutPutUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 通过{@link File}作为参数来读取Excel表格
 * @author Bloduc Spauter
 * @since 1.18
 */
public interface ReadByFile extends FileReadAndOutPutUtil {
    /**
     * 读取文件并把结果保存到List集合中
     * @param file 文件
     * @return List
     * @throws IOException IO流异常
     */
    List<Map<String, Object>> readToList(File file) throws IOException;

    /**
     * 读取文件将结果存入二维数组中
     * @param file 读取文件将结果存入二维数组中
     * @return Object[][]
     * @throws IOException IO流异常
     */
    Object[][] readToArray(File file) throws IOException;

}
