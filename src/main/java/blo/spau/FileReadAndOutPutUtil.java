package blo.spau;

/**
 * @author Bloduc Spauer
 * @version 1.14.514
 * <h2>blo.spau.excel.FileReadAndOutPutUtil</h2>
 * <h3>用于读取和写出的工具</h3>
 *  <table class="reference notranslate">
 * <tbody><tr><th style="width:30%">头信息</th><th>描述</th></tr>
 * <tr><td>Read</td><td>读取文档并存储起来 <b>excel/xlsx</b> 或 <b>excel/xls</b> 是最常见的两种文档。</td></tr>
 * <tr><td>Output</td><td>把数据以excel文档的方式存储到本地磁盘 <b>excel/xlsx</b> 或 <b>excel/xls</b> 是最常见的两种文档存储格式。</td></tr>
 */
public interface FileReadAndOutPutUtil {
    String suffix1 = "xlsx";
    String suffix2 = "xls";
    String sheetName="Sheet1";

}
