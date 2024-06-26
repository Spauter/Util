package com.bloducspauter.excelutil.origin.service;


import com.bloducspauter.origin.service.FIleReadAndOutput;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 拓展的excel接口类，主要在输出方面
 *
 * @author Bloduc Spauter
 * @version 1.18.2
 */
public interface ExcelService extends FIleReadAndOutput {
    String SHEET_NAME = "Sheet1";

    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}，
     *
     * @param sheetName 自定义Sheet名字
     * @param list      List集合
     * @param file      文件，可以为目录
     * @throws IOException IO流异常
     */
    void output(String sheetName, List<Map<String, Object>> list, File file) throws Exception;

    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}，
     *
     * @param sheetName 自定义Sheet名字
     * @param list      List集合
     * @param path      文件路径，可以为目录
     * @throws IOException IO流异常
     */
    default void output(String sheetName, List<Map<String, Object>> list, String path) throws Exception {
        File file = new File(path);
        output(sheetName, list, file);
    }

    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}，
     * Sheet会采用默认名字
     *
     * @param list List集合
     * @param file 文件，可以为目录
     * @throws IOException IO流异常
     */
    @Override
    default void output(List<Map<String, Object>> list, File file) throws Exception {
        output(SHEET_NAME, list, file);
    }

    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}，
     * Sheet会采用默认名字
     *
     * @param list List集合
     * @param path 文件路径，可以为目录
     * @throws IOException IO流异常
     */
    @Override
    default void output(List<Map<String, Object>> list, String path) throws Exception {
        File file = new File(path);
        output(SHEET_NAME, list, file);
    }

    /**
     * 将存储结果输出为表格
     *
     * @param sheetName 自定义Sheet名字
     * @param obj       二维数组 {@code Object[][]}
     * @param title     标题{@code String[]}
     * @param file      文件，可以为目录
     * @throws IOException IO流异常
     */
    void output(String sheetName, Object[][] obj, String[] title, File file) throws Exception;


    /**
     * 将存储结果输出为表格
     *
     * @param sheetName 自定义Sheet名字
     * @param obj       二维数组 {@code Object[][]}
     * @param title     标题{@code String[]}
     * @param path      文件路径，可以为目录
     * @throws IOException IO流异常
     */
    default void output(String sheetName, Object[][] obj, String[] title, String path) throws Exception {
        File file = new File(path);
        output(sheetName, obj, title, file);
    }

    /**
     * 将结果输出保持文件中，在使用有参构造后可以使用此方法
     *
     * @param path 文件路径，可以为目录
     * @throws IOException          IO异常
     * @throws NullPointerException 如果书库为空可能抛出
     */
    @Override
    default void output(Object[][] objects, String[] title, String path) throws Exception{
        File file = new File(path);
        output(SHEET_NAME, objects, title, file);
    }

    /**
     * 将结果输出保持文件中
     *
     * @param obj   二维数组
     * @param title 标题
     * @param file  文件，可以为目录
     * @throws IOException Io流异常
     */
    @Override
    default void output(Object[][] obj, String[] title, File file) throws Exception{
        output(SHEET_NAME, obj, title, file);
    }
}
