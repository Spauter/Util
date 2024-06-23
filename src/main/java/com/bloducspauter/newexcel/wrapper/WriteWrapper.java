package com.bloducspauter.newexcel.wrapper;

import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 *
 */
@Setter
@Builder
public class WriteWrapper {
    private String sheetName;
    @NonNull
    private String path;

    private String fileName;

    private String passWord;
}
