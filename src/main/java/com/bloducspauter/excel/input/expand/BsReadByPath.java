package com.bloducspauter.excel.input.expand;

import java.io.IOException;
import java.util.List;

/**
 * 通过文件路径读取表格,返回一个或者多个List集合
 * @param <T> 实体类
 * @author Bloduc Spauter
 * @version 1.18
 */
public interface BsReadByPath<T> {
    /**
     * 通过文件路径读取文档,并通过指定行数返回一个实体类
     * @param path 文件路径
     * @param index 行数
     * @return 实体类
     */
    public T readOne(String path,int index)throws IOException, NoSuchFieldException;

    /**
     * 通过获取文件路径读取所有的数据返回实体类List集合
     * @param path 文件路径
     * @return {@code List<T>}
     */
    public List<T> readAll(String path) throws IOException, NoSuchFieldException;

}
