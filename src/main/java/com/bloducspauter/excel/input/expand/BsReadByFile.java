package com.bloducspauter.excel.input.expand;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 通过接受{@link java.io.File}为参数通,返回一个或者多个List集合
 * @author Bloduc Spauter
 * @version 1.18
 */
public interface BsReadByFile<T> {

    /**
     * 通过文件读取文档,并通过指定行数返回一个实体类
     * @param file 文件路径
     * @param index 行数
     * @return 实体类
     */
    public T readOne(File file, int index)throws IOException, NoSuchFieldException;

    /**
     * 通过获取路径读取所有的数据返回实体类List集合
     * @param file 文件
     * @return {@code List<T>}
     */
    public List<T> readAll(File file)throws IOException, NoSuchFieldException;
}
