package com.bloducspauter.origin.wrapper;

import lombok.Builder;
import lombok.NonNull;
import lombok.Data;

/**
 * 读取文档设置的参数
 * @author Bloduc Spauter
 *
 */
@Data
@Builder
public class ReadWrapper {
    /**
     * 文件路径
     *
     */
    @NonNull
    private String path;

    /**
     * 标题栏
     */
    private int titleLine=0;

    /**
     * 起始行
     */
    private int startRow=0;

    /**
     * 截至行
     */
    private int endRow=-1;

    /**
     * 起始列
     */
    private int startColumn=0;

    /**
     * 截至列
     */
    private int endColumn=-1;

    /**
     * 读取的sheet
     *
     */
    private int readSheetAt=0;

    /**
     * 密码
     */
    private String password =null;

    /**
     * 日期格式
     */
    private String dateformat="yyy-MM-dd";
}
