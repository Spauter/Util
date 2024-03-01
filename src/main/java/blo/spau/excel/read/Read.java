package blo.spau.excel.read;


import blo.spau.FileReadAndOutPutUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauer
 * @version 1.14.514
 * <h1>blo.spau.excel.FileReadAndOutPutUtil</h1>
 *  <table class="reference notranslate">
 * <tbody><tr><th style="width:30%">头信息</th><th>描述</th></tr>
 * <tr><td>readXlsx</td><td>读取xlsx文档，值<b>String filePath</b>或<b>File</b>是两种常见的值.返回<b>java.util.List</b>。</td></tr>
 * <tr><td>readXls</td><td>读取xls文档，值<b>filePath</b>或<b>File</b>是两种常见的值. 返回<b>java.util.List</b></td>。</tr>
 * <tr><td>readLsx</td><td>读取xlsx文档，值<b>String filePath</b>或<b>File</b> 是两种常见的值.返回<b>Object[][]</b>。</td></tr>
 * <tr><td>readLs</td><td>读取xls文档，值<b>filePath</b>或<b>File</b>是两种常见的值. 返回<b>Object[][]</b>。</td></tr>
 * <tr><td>getTitle</td><td>获取表头（标题）。值<b>excel/xlsx</b>或<b>excel/xls</b>是两种常见的值.返回<b>Object[][]</b><p>。不输入任何值则返回当前读取表格的表头(标题）</p></td></tr>
 */
public interface Read extends FileReadAndOutPutUtil {
    /**
     * 返回一个包含当前读取表格的表头的数组
     * @return
     * @throws IOException
     */
    String[] getTitle() throws IOException;

    /**
     * 返回一个包含需要读取文件路径的表头的数组
     * @param Path
     * @return
     * @throws IOException
     */
    String[] getTitle(String Path) throws IOException;

    /**
     * 返回一个包含需要读取文件的表头的数组
     * @param file
     * @return
     * @throws IOException
     */
    String[] getTitle(File file) throws IOException;

    /**
     * 返回Map集合里键值，用于获取标题
     * @param list
     * @return
     */
    String[] getTitle(List<Map<String, Object>> list);


    Map<Integer,String>titleMap();

    /**
     * 返回总行数
     * @return
     */
    int getMaxRows();

    /**
     * 返回总列数
     * @return
     */
    int getMaxCols();

    /**
     * 返回List
     * @param path
     * @return
     * @throws
     * @throws IOException
     */
    List<Map<String,Object>>readToList(String path) throws IOException;


    List<Map<String,Object>>readToList(File file) throws IOException;

    Object[][]  readToArray(String path) throws  IOException;
    Object[][]  readToArray(File file) throws  IOException;
}
