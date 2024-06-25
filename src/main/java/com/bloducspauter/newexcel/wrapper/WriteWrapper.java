package com.bloducspauter.newexcel.wrapper;

import lombok.*;

/**
 * @author Bloduc Spauter
 * @version 1.19
 */
@Data
@Builder
public class WriteWrapper {
    private String sheetName;
    @NonNull
    private String path;

    private String fileName;

    private String password;
}
