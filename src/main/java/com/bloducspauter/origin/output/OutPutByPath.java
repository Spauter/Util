package com.bloducspauter.origin.output;

import com.bloducspauter.origin.FileReadAndOutPutUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 * @version 1.18
 */
public interface OutPutByPath extends FileReadAndOutPutUtil {
    /**
     * 将结果输出保持文件中
     *
     * @param list List集合
     * @param path 文件路径
     * @throws IOException Io流异常
     */
    void outPut(List<Map<String, Object>> list, String path) throws IOException;


    /**
     * 将结果输出保持文件中
     *
     * @param obj   二维数组
     * @param title 标题
     * @param path  文件路径
     * @throws IOException  Io流异常
     */
    void outPut(Object[][] obj, String[] title, String path) throws IOException;

    /**
     * 将结果输出保持文件中，在使用有参构造后可以使用此方法
     *
     * @param path 文件路径
     * @throws IOException          IO异常
     * @throws NullPointerException 如果书库为空可能抛出
     */
    void outPut(String path) throws IOException;
}
