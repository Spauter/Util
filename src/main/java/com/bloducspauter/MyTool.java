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
 */
public abstract class MyTool {
    /**
     * 检查文件后缀名
     *
     * @param file 被检查文件，如果是文件路径
     * @throws IllegalArgumentException 如果文件后缀名不符合要求
     */
    public abstract void Check_suffix(File file) throws IOException;

    /**
     * 检查文件后缀名
     * @param path 被检查文件路径
     * @throws IllegalArgumentException 如果文件后缀名不符合要求
     */
    public abstract void Check_suffix(String path) throws IOException;

    /**
     * 在读取前检查文件是否存在
     *
     * @param file  被检查文件
     * @throws FileNotFoundException 如果找不到文件
     */
    public abstract void Check_file(File file) throws FileNotFoundException;

    /**
     * 检查文件后缀名
     * @param path 被检查文件路径
     * @throws FileNotFoundException 如果找不到文件
     */
    public abstract void Check_file(String path) throws FileNotFoundException;

    /**
     * 打印特殊文字信息
     * <p>样式</p>
     * <p>0  空样式,
     * 1  粗体,
     * 4  下划线,
     * 7  反色;</p>
     * <p>颜色</p>
     * 30  白色,
     * 31  红色,
     * 32  绿色,
     * 33  黄色,
     * 34  蓝色,
     * 35  紫色,
     * 36  浅蓝,
     * 37  灰色
     * </p>
     * <p>
     * 背景颜色：40-47 和颜色顺序相同
     * 颜色2：90-97  比颜色1更鲜艳一些
     * </p>
     *
     * @param content 文字内容
     * @param color 颜色
     * @param type 样式
     * @return String
     * @deprecated
     */
    @Deprecated(since = "1.7")
    public abstract String PrintInfo(String content, int color, int type);

    /**
     * 检查文件是否为目录
     *
     * @param file 被检查文件
     */
    public abstract void Check_IsDirectory(File file) throws IOException;


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
    public abstract void check_titleLine(int titleLine, int maxRow)throws IndexOutOfBoundsException;

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
    public abstract void check_row_col(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol)throws IndexOutOfBoundsException;
}
