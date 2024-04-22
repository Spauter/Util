package com.bloducspauter.excel.output.expand;

import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * 相似的描述,见
 *
 * @param <T> 实体类
 * @see com.bloducspauter.excel.input.expand.BsReadByPath
 * @see com.bloducspauter.excel.input.expand.BsReadByFile
 */
public interface BsOutputByFile<T> {
    /**
     * 输出表格
     *
     * @param entities  实体类
     * @param file      文件
     * @param sheetName Sheet名字
     * @throws IOException Sheet名字
     */
    public void write(List<T> entities, File file, String sheetName) throws IOException;

    /**
     * @see #write(List, File, String) 不需要提供Sheetm名字,由{@link com.bloducspauter.origin.FileReadAndOutPutUtil#SHEET_NAME} 代替
     */
    public void write(List<T> entities, File file) throws IOException, NoSuchFieldException;
}
