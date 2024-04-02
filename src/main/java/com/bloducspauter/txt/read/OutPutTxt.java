package com.bloducspauter.txt.read;

import com.bloducspauter.FileReadAndOutPutUtil;
import com.bloducspauter.OutputFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OutPutTxt extends OutputFile {

    void output(String excelFilePath, String fileName) throws IOException;
}
