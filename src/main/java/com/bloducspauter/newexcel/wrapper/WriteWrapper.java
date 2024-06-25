package com.bloducspauter.newexcel.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 * @version 1.19
 */
@Getter
@Builder
public class WriteWrapper {
    private String sheetName;
    @NonNull
    private String path;

    private String fileName;

    private String passWord;
}
