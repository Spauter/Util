package com.bloducspauter.excel.read;

import com.bloducspauter.origin.read.ReadFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 表格文件读取
 *
 * @author Bloduc Spauer
 * @version 1.14.514
 */
public interface ReadExcel extends ReadFile {
    /**
     * 返回一个包含当前读取表格的表头的数组
     *
     * @return {@code String[]}
     * @throws IOException IO异常
     */
    String[] getTitle() throws IOException;

    /**
     * 返回一个包含需要读取文件路径的表头的数组
     *
     * @param Path 文件路径
     * @return {@code String[]}
     * @throws IOException IO异常
     */
    String[] getTitle(String Path) throws IOException;

    /**
     * 返回一个包含需要读取文件的表头的数组
     *
     * @param file 文件
     * @return {@code String[]}
     * @throws IOException IO异常
     */
    String[] getTitle(File file) throws IOException;

    /**
     * 返回Map集合里键值，用于获取标题
     *
     * @param list {@code List集合}
     * @return {@code String[]}
     */
    String[] getTitle(List<Map<String, Object>> list);

    /**
     * 以键值对的方式返回标题
     * @return {@code  Map<Integer, String>}
     */
    Map<Integer, String> titleMap();

    /**
     * 设置被读取的Sheet;
     * @param var Sheet
     */
    void readSheetAt(int var);
}
