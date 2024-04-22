package com.bloducspauter.excel.input.expand;

import com.bloducspauter.excel.input.ReadExcel;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 拓展的接口
 * @param <T> 实体类
 * @author Bloduc Spauter
 * @version 1.18
 * @see BsReadByFile
 * @see BsReadByPath
 */
public interface BsReadExcel<T> extends BsReadByPath<T>,BsReadByFile<T> {
}
