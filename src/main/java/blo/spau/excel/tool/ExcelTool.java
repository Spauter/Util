package blo.spau.excel.tool;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *<h1>Tool</h1>
 * <table class="reference notranslate">
 *<tbody><tr><th style="width:30%">头信息</th><th>描述</th></tr>
 *<tr><td>Ckeck_suffix</td><td>检查文件后缀名是否符合</td></tr>
 *<tr><td>Check_file</td><td>检查文件是否存在</td></tr>
 *<tr><td>Check_IsDirectory</td><td>检查文件是否为目录</td></tr>
 *<tr><td>Check_OutputPath</td><td>检查用户输入的文件输出目录</td></tr>
 *<tr><td>PrintInfo</td><td>打印特殊格式的字体</td></tr>
 * <tr><td>conformity</td><td>数据类型转换</td></tr>
 *</tbody></table>
 */
abstract class ExcelTool {
    /**
     * 检查文件后缀名
     * @param file
     * @throws IllegalArgumentException 如果文件后缀名不是xls或者xlsx
     */
    abstract void Ckeck_suffix(File file) throws IOException;

    /**
     * 在读取前检查文件是否存在
     * @param file
     * @throws FileNotFoundException 如果找不到文件
     */
    abstract void Check_file(File file) throws FileNotFoundException;

    /**
     * 打印特殊文字信息
     * @param content
     * @param color
     * @param type
     * @return String
     */
    abstract String PrintInfo(String content, int color, int type);

    /**
     * 检查文件是否为目录
     * @param file
     */
    abstract void Check_IsDirectory(File file) throws IOException;


    /**
     * 把list转化二维数组
     * @param list
     * @param titles
     * @return
     */
    abstract Object[][] conformity(List<Map<String, Object>> list,Map<Integer,String> titles);

    /**
     *
     * @param path
     * @return
     */
    abstract File conformity(String path);


    abstract List<Map<String,Object>>conformity(Object[][] obj,String[] title)throws IndexOutOfBoundsException,NullPointerException;


}
