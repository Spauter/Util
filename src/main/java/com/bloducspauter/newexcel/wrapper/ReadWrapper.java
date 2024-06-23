package com.bloducspauter.newexcel.wrapper;

import lombok.Builder;
import lombok.NonNull;
import lombok.Data;
import lombok.Setter;

/**
 * 读取文档设置的参数
 * @author Bloduc Spauter
 * @version 1.19
 */
@Data
@Builder
@Setter
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
    private int titleLine;

    /**
     * 起始行
     */
    private int startRow;

    /**
     * 截至行
     */
    private int endRow;

    /**
     * 起始列
     */
    private int startColumn;

    /**
     * 截至列
     */
    private int endColumn;

    /**
     * 读取的sheet
     *
     */
    private int readSheetAt;

    /**
     * 密码
     */
    private String password;

    /**
     * 日期格式
     */
    private String dateformat;
}
