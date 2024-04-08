package com.bloducspauter.txt.read;

import com.bloducspauter.origin.output.OutputFile;

import java.io.IOException;

public interface OutPutTxt extends OutputFile {

    void output(String excelFilePath, String fileName) throws IOException;
}
