package com.bloducspauter.excel.output;



import com.bloducspauter.origin.output.OutputFile;
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
 * @see OutputFile
 */
public interface OutputExcel extends OutputByFile,OutputByPath {
}
