package com.bloducspauter.excel.input;


import com.bloducspauter.excel.input.title.DetermineTitle;
import com.bloducspauter.origin.init.DetermineReadRange;
import com.bloducspauter.origin.input.ReadFileByPath;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 表格文件读取
 *
 * @author Bloduc Spauer
 * @version 1.14.514
 */
public interface ReadExcel extends ReadFileByPath, DetermineReadRange,ReadExcelByFile, DetermineTitle {
    /**
     *  提供文件后直接读取,不需要额外输入文件路径
     * @return List
     * @throws IOException IO流异常
     */
    List<Map<String, Object>> readToList() throws IOException;


    /**
     * 读取文件将结果存入二维数组中，在提供有参构造后可使用
     * @return Object[][]
     * @throws IOException IO流异常
     */
    Object[][] readToArray() throws IOException;

}
