package com.bloducspauter.excel.input.title;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 确定读取的起始行,起始列,截止行,截止列,以及指定哪个工作表
 * @author Bloduc SPauter
 * @version 1.18
 * @see com.bloducspauter.origin.tool.MyTool#checkRowCol(int, int, int, int, int, int)
 */
public interface DetermineTitle {

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
     * @param path 文件路径
     * @return {@code String[]}
     * @throws IOException IO异常
     */
    String[] getTitle(String path) throws IOException;

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
     *
     * @return {@code  Map<Integer, String>}
     */
    Map<Integer, String> titleMap();
}
