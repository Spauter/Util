package com.bloducspauter.text;

import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import com.bloducspauter.origin.service.ValidationTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 */
public class TextUtil implements TextService {

    public static final String TXT_CSV_SEPARATOR = ",";

    private String[] title;

    private boolean first = true;

    private final ValidationTool validationTool=new ValidationTool() {
        @Override
        public void checkSuffix(File file) throws UnsupportedFileException {

        }
    };

    private List<Map<String, Object>> readImpl(String path, String separator) {
        separator=separator==null?TXT_CSV_SEPARATOR:separator;
        List<Map<String, Object>> list = new ArrayList<>();
        File file = new File(path);
        FileReader iis = null;
        BufferedReader bfr = null;
        try {
            iis = new FileReader(file);
            bfr = new BufferedReader(iis);
            String info;
            while ((info = bfr.readLine()) != null) {
                if (first) {
                    title = info.split(separator);
                    first = false;
                }
                list.add(checkData(info, separator));
            }
        } catch (IOException e) {
            System.out.println("Error reading because of:" + e.getMessage());
        }
        if (null != iis) {
            try {
                iis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        list.remove(0);
        System.out.println("Reading text file successfully");
        return list;
    }

    private Map<String, Object> checkData(String s, String separator) {
        separator = separator == null ? TXT_CSV_SEPARATOR : separator;
        Map<String, Object> map = new HashMap<>();
        String[] infos = s.split(separator);
        for (int i = 0; i < title.length; i++) {
            if (i >= infos.length) {
                map.put(title[i], null);
            } else {
                map.put(title[i], infos[i]);
            }
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> readToList(String path, String separator) {
        return readImpl(path,separator);
    }

    @Override
    public Object[][] readToArray(String path, String separator) {
        List<Map<String,Object>>list=readImpl(path,separator);
      return validationTool.conformity(list,title);
    }

    @Override
    public List<Map<String, Object>> readToList(String path) throws UnsupportedFileException {
        return readToList(path,null);
    }

    @Override
    public Object[][] readToArray(String path) throws UnsupportedFileException {
        return readToArray(path,null);
    }

    @Override
    public void output(List<Map<String, Object>> list, String path) throws UnsupportedFileException {

    }

    @Override
    public void output(Object[][] obj, String[] title, File file) throws UnsupportedFileException {

    }


}
