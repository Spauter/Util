package com.bloducspauter.text;

import com.bloducspauter.origin.service.FIleReadAndOutput;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 *
 */
public interface TextService extends FIleReadAndOutput {

     List<Map<String,Object>> readToList(String path,String separator);

     default  List<Map<String,Object>> readToList(File file, String separator){
         return readToList(file.getAbsolutePath(),separator);
     }

     Object[][] readToArray(String path,String separator);

     default  Object[][] readToArray(File file,String separator){
         return readToArray(file.getAbsolutePath(),separator);
     }
}
