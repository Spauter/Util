package com.bloducspauter.excel.output;

import com.bloducspauter.origin.output.OutputFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 * @version 1.18
 */
public interface OutputByFile extends OutputFile {

    /**
     * 将存储结果输出为表格
     *
     * @param sheetName 自定义Sheet名字
     * @param obj       二维数组 {@code Object[][]}
     * @param title     标题{@code String[]}
     * @param file      文件
     * @throws IOException IO流异常
     */
    void outPut(String sheetName, Object[][] obj, String[] title, File file) throws IOException;


    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}
     *
     * @param sheetName 自定义Sheet名字
     * @param list      List集合
     * @param file      文件
     * @throws IOException IO流异常
     */
    void outPut(String sheetName, List<Map<String, Object>> list, File file) throws IOException;
}
