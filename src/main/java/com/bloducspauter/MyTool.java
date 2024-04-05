package com.bloducspauter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <h4>检查转换工具</h4>
 * <table class="reference notranslate">
 * <tbody><tr><th style="width:30%">头信息</th><th>描述</th></tr>
 * <tr><td>Check_suffix</td><td>检查文件后缀名是否符合</td></tr>
 * <tr><td>Check_file</td><td>检查文件是否存在</td></tr>
 * <tr><td>Check_IsDirectory</td><td>检查文件是否为目录</td></tr>
 * <tr><td>Check_OutputPath</td><td>检查用户输入的文件输出目录</td></tr>
 * <tr><td>PrintInfo</td><td>打印特殊格式的字体</td></tr>
 * <tr><td>conformity</td><td>数据类型转换</td></tr>
 * </tbody></table>
 * @author Bloducspauter
 */
public abstract class MyTool {
    /**
     * 检查文件后缀名
     *
     * @param file 被检查文件，如果是文件路径
     * @throws IllegalArgumentException 如果文件后缀名不符合要求
     */
    public abstract void checkSuffix(File file) throws IOException;

    /**
     * 检查文件后缀名
     * @param path 被检查文件路径
     * @throws IllegalArgumentException 如果文件后缀名不符合要求
     */
    public abstract void checkSuffix(String path) throws IOException;

    /**
     * 在读取前检查文件是否存在
     *
     * @param file  被检查文件
     * @throws FileNotFoundException 如果找不到文件
     */
    public abstract void checkFile(File file) throws FileNotFoundException;

    /**
     * 检查文件后缀名
     * @param path 被检查文件路径
     * @throws FileNotFoundException 如果找不到文件
     */
    public abstract void checkFile(String path) throws FileNotFoundException;


    /**
     * 检查文件是否为目录
     *
     * @param file 被检查文件
     */
    public abstract void checkIsDirectory(File file) throws IOException;


    /**
     * 把list转化二维数组
     *
     * @param list 被转化Lisr集合
     * @param titles 标题
     * @return Object[][]
     */
    public abstract Object[][] conformity(List<Map<String, Object>> list, Map<Integer, String> titles);

    /** 其实就是new File(Path)
     * @param path 文件路径
     * @return File
     */
    public abstract File conformity(String path);

    /**
     * 把一个二维数组转化成List集合
     * @param obj 二维数组
     * @param title 标题
     * @return List
     * @throws IndexOutOfBoundsException 如果元素内容比标题长
     * @throws NullPointerException 输入的数据为空
     */
    public abstract List<Map<String, Object>> conformity(Object[][] obj, String[] title) throws IndexOutOfBoundsException, NullPointerException;

    /**
     *  将某一行设置为标题行
     * @param titleLine 被设置的行，默认值为0 （第一行}
     * @param maxRow 表格最大行数
     * @throws IndexOutOfBoundsException 如果 titleLine 小于0或者大于表格的最大行数
     * */
    public abstract void checkTitleLine(int titleLine, int maxRow)throws IndexOutOfBoundsException;

    /**
     *  检查表格范围读取是否合法
     * @param startRow 起始行
     * @param startCol 起始列
     * @param endWithRow 截止行
     * @param endWithCol 截止列
     * @param maxRow 最大行
     * @param maxCol 最大列
     * @throws IndexOutOfBoundsException 读取的范围超过了表格最大范围
     */
    public abstract void checkRowCol(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol)throws IndexOutOfBoundsException;
}
