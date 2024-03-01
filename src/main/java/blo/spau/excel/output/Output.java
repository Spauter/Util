package blo.spau.excel.output;



import blo.spau.FileReadAndOutPutUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <h1>表格输出工具</h1>
 * <h2>outPut</h2>
 * <table class="reference notranslate">
 * <tbody><tr><th style="width:30%">值</th><th>描述</th></tr>
 * <tr><td>ouPut</td><td>输出为Excel文档，存储格式 <b>excel/xlsx</b>和<b>excel/xls</b>两种格式</td></tr>
 * </table>
 * </tbody>
 */
public interface Output extends FileReadAndOutPutUtil {

    /**
     * @param sheetName
     * @param obj
     * @param title
     * @param Path
     * @throws IOException
     */
    void outPut(String sheetName, Object[][] obj, String[] title, String Path) throws IOException;


    /**
     *
     * @param sheetName
     * @param obj
     * @param title
     * @param file
     * @throws IOException
     */
    void outPut(String sheetName, Object[][] obj, String[] title, File file) throws IOException;

    /**
     *
     * @param sheetName
     * @param list
     * @param Path
     * @throws IOException
     */
    void outPut(String sheetName, List<Map<String, Object>> list, String Path) throws IOException;

    /**
     *
     * @param sheetName
     * @param list
     * @param file
     * @throws IOException
     */
    void outPut(String sheetName, List<Map<String, Object>> list, File file) throws IOException;

    /**
     *
     * @param list
     * @param Path
     * @throws IOException
     */
    void outPut(List<Map<String, Object>> list, String Path) throws IOException;

    /**
     *
     * @param list
     * @param file
     * @throws IOException
     */
    void outPut(List<Map<String, Object>> list, File file) throws IOException;

    /**
     *
     * @param obj
     * @param title
     * @param Path
     * @throws IOException
     */
    void outPut(Object[][] obj, String[] title, String Path) throws IOException;

    /**
     *
     * @param obj
     * @param title
     * @param file
     * @throws IOException
     */
    void outPut(Object[][] obj, String[] title, File file) throws IOException;
}
