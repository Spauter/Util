package com.bloducspauter.text;

import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import com.bloducspauter.origin.service.FIleReadAndOutput;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 *
 */
public interface TextService extends FIleReadAndOutput {

     List<Map<String,Object>> readToList(String path,String separator) throws IOException;

     default  List<Map<String,Object>> readToList(File file, String separator) throws IOException {
         return readToList(file.getAbsolutePath(),separator);
     }

     Object[][] readToArray(String path,String separator) throws IOException;

     default  Object[][] readToArray(File file,String separator) throws IOException {
         return readToArray(file.getAbsolutePath(),separator);
     }

    void output(List<Map<String, Object>> list, String path,String separator) throws IOException, UnsupportedFileException;

     default void output(List<Map<String, Object>> list, File file,String separator) throws IOException {
         output(list,file.getAbsolutePath(),separator);
     }

    void output(Object[][] obj, String path,String separator) throws IOException, UnsupportedFileException;

     default void output(Object[][] obj, File file,String separator) throws IOException, UnsupportedFileException{
         output(obj, file.getAbsolutePath(),separator);
     }

    String[] getTitle();
}
