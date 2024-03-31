package com.bloducspauter.excel.read;
import com.bloducspauter.ReadFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauer
 * @version 1.14.514
 */
public interface ReadExcel extends ReadFile {
    /**
     * 返回一个包含当前读取表格的表头的数组
     *
     * @return
     * @throws IOException
     */
    String[] getTitle() throws IOException;

    /**
     * 返回一个包含需要读取文件路径的表头的数组
     *
     * @param Path
     * @return
     * @throws IOException
     */
    String[] getTitle(String Path) throws IOException;

    /**
     * 返回一个包含需要读取文件的表头的数组
     *
     * @param file
     * @return
     * @throws IOException
     */
    String[] getTitle(File file) throws IOException;

    /**
     * 返回Map集合里键值，用于获取标题
     *
     * @param list
     * @return
     */
    String[] getTitle(List<Map<String, Object>> list);


    Map<Integer, String> titleMap();


    void readSheetAt(int var);

    void setTitleLine(int titleLine);
}
