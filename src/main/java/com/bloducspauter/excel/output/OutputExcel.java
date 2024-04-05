package com.bloducspauter.excel.output;



import com.bloducspauter.OutputFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <h3>表格输出工具</h3>
 * <h4>outPut</h4>
 * <table class="reference notranslate">
 * <tbody><tr><th style="width:30%">值</th><th>描述</th></tr>
 * <tr><td>ouPut</td><td>输出为Excel文档，存储格式 <b>excel/xlsx</b>和<b>excel/xls</b>两种格式</td></tr>
 * </table>
 * </tbody>
 * @author Bloduc Spauter
 * @version 1.16
 * @see com.bloducspauter.OutputFile
 */
public interface OutputExcel extends OutputFile {
    /**
     * 将存储结果输出为表格
     * @param sheetName 自定义Sheet名字
     * @param obj 二维数组 {@code Object[][]}
     * @param title 标题{@code String[]}
     * @param Path 文件路径
     * @throws IOException IO流异常
     */
    void outPut(String sheetName, Object[][] obj, String[] title, String Path) throws IOException;


    /**
     * 将存储结果输出为表格
     * @param sheetName 自定义Sheet名字
     * @param obj 二维数组 {@code Object[][]}
     * @param title 标题{@code String[]}
     * @param file 文件
     * @throws IOException IO流异常
     */
    void outPut(String sheetName, Object[][] obj, String[] title, File file) throws IOException;

    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}
     * @param sheetName 自定义Sheet名字
     * @param list List集合
     * @param Path 文件路径
     * @throws IOException IO流异常
     */
    void outPut(String sheetName, List<Map<String, Object>> list, String Path) throws IOException;

    /** 将存储结果输出为表格，不需要额外的{@code String[] title}
     * @param sheetName 自定义Sheet名字
     * @param list List集合
     * @param file 文件
     * @throws IOException IO流异常
     */
    void outPut(String sheetName, List<Map<String, Object>> list, File file) throws IOException;
}
