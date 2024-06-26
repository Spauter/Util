package com.bloducspauter.text;

import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;
import com.bloducspauter.excelutil.base.service.ValidationTool;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

/**
 * 这个类主要原本是处理文本型csv表格的，对于其它纯文本都适用
 *
 * @author Bloduc Spauter
 * @since 1.18.2
 */
public class TextUtil implements TextService {

    public static final String TXT_CSV_SEPARATOR = ",";

    protected String[] title;

    protected boolean first = true;

    protected final ValidationTool validationTool = new ValidationTool() {
        @Override
        public void checkSuffix(File file) throws UnsupportedFileException {

        }
    };

    private List<Map<String, Object>> readImpl(String path, String separator) throws Exception {
        separator = separator == null ? TXT_CSV_SEPARATOR : separator;
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
            if (e instanceof FileNotFoundException) {
                throw e;
            }
        }
        try {
            iis.close();
        } catch (IOException ignore) {
        }
        list.remove(0);
        System.out.println("Reading text file successfully");
        return list;
    }

    private File getOutputFile(String path) throws Exception {
        File file = new File(path);
        //如果是目录创建一个文件名
        if (validationTool.checkIsDirectory(file)) {
            path = path + File.separator + UUID.randomUUID() + ".txt";
            file = new File(path);
        }
        if (validationTool.checkFileExists(file)) {
            throw new FileAlreadyExistsException("File " + file.getAbsolutePath() + " already exists");
        }
        return file;
    }

    public void outputImpl(String path, Object[][] obj, String separator) throws Exception{
        separator=separator==null?TXT_CSV_SEPARATOR:separator;
        try {
            File file = getOutputFile(path);
            FileWriter out = new FileWriter(file);
            for (String t : title) {
                out.write(t + separator);
            }
            out.write("\r\n");
            for (Object[] objects : obj) {
                for (int j = 0; j < title.length; j++) {
                    String info = (String) objects[j];
                    info = info == null ? "" : info;
                    byte[] bytes = info.getBytes(StandardCharsets.UTF_8);
                    out.write(new String(bytes, StandardCharsets.UTF_8) + separator);
                }
                out.write("\r\n");
                System.out.println();
            }
            out.close();
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.out.println("Write failed because of " + e.getClass() + ":" + e.getMessage());
        }
    }

    private void outputImpl(String path, List<Map<String, Object>> list, String separator) throws Exception {
        File file = getOutputFile(path);
        separator=separator==null?TXT_CSV_SEPARATOR:separator;
        try (FileWriter out = new FileWriter(file)) {
            out.write(Arrays.toString(title) + "\r\n");
            for (Map<String, Object> entity : list) {
                for (Object value : entity.values()) {
                    value = (value == null) ? "" : value.toString();
                    out.write(value + separator);
                }
                out.write("\r\n");
            }
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.out.println("Write failed because of " + e.getClass() + ":" + e.getMessage());
        }
    }

    Map<String, Object> checkData(String s, String separator) throws NoSuchFieldException {
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
    public List<Map<String, Object>> readToList(String path, String separator) throws Exception {
        return readImpl(path, separator);
    }

    @Override
    public Object[][] readToArray(String path, String separator) throws Exception {
        List<Map<String, Object>> list = readImpl(path, separator);
        return validationTool.conformity(list, title);
    }

    @Override
    public void output(List<Map<String, Object>> list, String path, String separator) throws Exception {
        outputImpl(path,list,separator);
    }

    @Override
    public void output(Object[][] obj, String path, String separator) throws Exception {
        outputImpl(path,obj,separator);
    }

    @Override
    public String[] getTitle() {
        return title;
    }

    @Override
    public List<Map<String, Object>> readToList(String path) throws Exception {
        return readToList(path, null);
    }

    @Override
    public Object[][] readToArray(String path) throws Exception{
        return readToArray(path, null);
    }

    @Override
    public void output(List<Map<String, Object>> list, String path) throws Exception {
        outputImpl(path, list, null);
    }

    @Override
    public void output(Object[][] obj, String[] title, File file) throws Exception {
        this.title = title;
        outputImpl(file.getAbsolutePath(), obj, null);
    }


}
