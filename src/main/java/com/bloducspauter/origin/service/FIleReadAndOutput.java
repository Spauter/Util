package com.bloducspauter.origin.service;

import com.bloducspauter.origin.exceptions.UnsupportedFileException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 *
 */
public interface FIleReadAndOutput {
     List<Map<String,Object>>readToList(String path)throws IOException, UnsupportedFileException;;

    default List<Map<String,Object>>readToList(File file) throws IOException, UnsupportedFileException {
       return readToList(file.getAbsolutePath());
    }

    Object[][] readToArray(String path) throws IOException, UnsupportedFileException;

    default Object[][] readToArray(File file) throws IOException, UnsupportedFileException {
        return readToArray(file.getAbsolutePath());
    }

    void output(List<Map<String,Object>> list,String path)throws IOException, UnsupportedFileException;;

    default void output(List<Map<String,Object>>list, File file) throws IOException, UnsupportedFileException {
        output(list,file.getAbsolutePath());
    }
}
