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
    @NonNull
    private String path;
    private int titleLine=0;
    private int startRow=0;
    private int endRow=-1;
    private int startColumn=0;
    private int endColumn=-1;
    private int readSheetAt=0;
    private String password =null;
    private String dateformat="yyy-MM-dd";
}
