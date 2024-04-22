package com.bloducspauter.excel.output.expand;

import java.io.IOException;
import java.util.List;

/**
 * 相似的描述,见
 *
 * @param <T> 实体类
 * @see com.bloducspauter.excel.input.expand.BsReadByPath
 * @see com.bloducspauter.excel.input.expand.BsReadByFile
 */
public interface BsOutputByPath<T> {
    /**
     * 输出表格
     * @param entities 实体类
     * @param path 文件路径
     * @param sheetName Sheet名字
     */
    public void write(List<T> entities, String path, String sheetName) throws IOException;

    /**
     * @see #write(List, String, String) 不需要提供Sheetm名字,由{@link com.bloducspauter.origin.FileReadAndOutPutUtil#SHEET_NAME} 代替
     */
    public void write(List<T> entities,String path) throws IOException;
}
