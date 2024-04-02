package com.bloducspauter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OutputFile extends FileReadAndOutPutUtil{


    /**
     * @param list
     * @param Path
     * @throws IOException
     */
    void outPut(List<Map<String, Object>> list, String Path) throws IOException;

    /**
     * @param list
     * @param file
     * @throws IOException
     */
    void outPut(List<Map<String, Object>> list, File file) throws IOException;

    /**
     * @param obj
     * @param title
     * @param Path
     * @throws IOException
     */
    void outPut(Object[][] obj, String[] title, String Path) throws IOException;

    /**
     * @param obj
     * @param title
     * @param file
     * @throws IOException
     */
    void outPut(Object[][] obj, String[] title, File file) throws IOException;

    void outPut(String path) throws IOException;

    void outPut(File file) throws IOException;

}
