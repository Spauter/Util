package com.bloducspauter.origin.service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 *
 */
public interface FIleReadAndOutput {
     List<Map<String,Object>>readToList(String path);

    default List<Map<String,Object>>readToList(File file){
       return readToList(file.getAbsolutePath());
    }

    Object[][] readToArray(String path);

    default Object[][] readToArray(File file){
        return readToArray(file.getAbsolutePath());
    }

    void output(List<Map<String,Object>> list,String path);

    default void output(List<Map<String,Object>>list, File file){
        output(list,file.getAbsolutePath());
    }
}
