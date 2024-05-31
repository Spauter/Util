package com.bloducspauter.text;

import com.bloducspauter.origin.service.FIleReadAndOutput;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 * @since 1.18.2
 */
public interface TextService extends FIleReadAndOutput {

     List<Map<String,Object>> readToList(String path,String separator) throws Exception;

     default  List<Map<String,Object>> readToList(File file, String separator) throws Exception {
         return readToList(file.getAbsolutePath(),separator);
     }

     Object[][] readToArray(String path,String separator) throws Exception;

     default  Object[][] readToArray(File file,String separator) throws Exception {
         return readToArray(file.getAbsolutePath(),separator);
     }

    void output(List<Map<String, Object>> list, String path,String separator) throws Exception;

     default void output(List<Map<String, Object>> list, File file,String separator) throws Exception {
         output(list,file.getAbsolutePath(),separator);
     }

    void output(Object[][] obj, String path,String separator) throws Exception;

     default void output(Object[][] obj, File file,String separator) throws Exception{
         output(obj, file.getAbsolutePath(),separator);
     }

    String[] getTitle();
}
