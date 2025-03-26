package com.bloducspauter.text;

import com.bloducspauter.excelutil.base.service.FIleReadAndOutput;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 * @since 1.18.2
 */
public interface TextService extends FIleReadAndOutput {

    /**
     * 读取文档路径,将结果存储到List集合中
     * @param path 文件路径
     * @param separator 分隔符如果是CSV文件分隔符为”，“
     */
    List<Map<String,Object>> readToList(String path,String separator) throws Exception;

    /**
     *  读取文档路径,将结果存储到List集合中
     * @param file 文件
     * @param separator 分隔符
     */
    default  List<Map<String,Object>> readToList(File file, String separator) throws Exception {
        return readToList(file.getAbsolutePath(),separator);
    }

    /**
     *  读取文档路径,将结果存储到L一个二位数组
     * @param path 文件路径
     * @param separator 分隔符
     */
    Object[][] readToArray(String path,String separator) throws Exception;

    /**
     *  读取文档路径,将结果存储到L一个二位数组
     * @param file 文件
     * @param separator 分隔符
     */
    default  Object[][] readToArray(File file,String separator) throws Exception {
        return readToArray(file.getAbsolutePath(),separator);
    }

    /**
     *  输出纯文本文件
     * @param list 数据集合
     * @param path 文件路径
     * @param separator 分隔符
     */
    void output(List<Map<String, Object>> list, String path,String separator) throws Exception;

    /**
     *  输出纯文本文件
     * @param list 数据集合
     * @param file 文件
     */
    default void output(List<Map<String, Object>> list, File file,String separator) throws Exception {
        output(list,file.getAbsolutePath(),separator);
    }

    /**
     *  输出纯文本文件
     * @param obj 二维数组
     * @param path 文件路径
     * @param separator 分隔符
     */
    void output(Object[][] obj, String path,String separator) throws Exception;

    /**
     *  输出纯文本文件
     * @param obj 二维数组
     * @param file 文件
     */
    default void output(Object[][] obj, File file,String separator) throws Exception{
        output(obj, file.getAbsolutePath(),separator);
    }

    String[] getTitle();
}
