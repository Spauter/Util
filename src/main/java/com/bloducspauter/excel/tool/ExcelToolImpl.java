package com.bloducspauter.excel.tool;


import com.bloducspauter.MyTool;
import lombok.extern.slf4j.Slf4j;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bloducspauter.FileReadAndOutPutUtil.SUFFIX_1;
import static com.bloducspauter.FileReadAndOutPutUtil.SUFFIX_2;
@Slf4j
//用继承的方式实现的
public class ExcelToolImpl extends MyTool {
    private final List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public void Check_suffix(File file) throws IOException {
        Check_IsDirectory(file);
        Check_suffix(file.getName());
    }

    @Override
    public void Check_suffix(String path) throws IOException {
        if (!(path.endsWith(SUFFIX_1) || (path.endsWith(SUFFIX_2)))) {
        log.error("Unsupported suffix");
        throw new IllegalArgumentException("Unsupported suffix. It need 'xls' or 'xlsx' file,but you provide a unsupported file");
    }
}

@Override
public void Check_file(File file) throws FileNotFoundException {
    if (!file.exists()) {
        log.error("File not found");
        throw new FileNotFoundException("The file is not found:" + file.getAbsoluteFile());
    }
}

@Override
    public void Check_file(String path) throws FileNotFoundException {
        Check_file(new File(path));
    }

    @Override
    public void Check_IsDirectory(File file) throws IOException {
        if (file.isDirectory()) {
            log.error("Access denied");
            throw new IOException("The folder cannot be read or written.");
        }
    }


    public String PrintInfo(String content, int color, int type) {
        boolean hasType = type != 1 && type != 3 && type != 4;
        if (hasType) {
            return String.format("\033[%dm%s\033[0m", color, content);
        } else {
            return String.format("\033[%d;%dm%s\033[0m", color, type, content);
        }

    }

    @Override
    public Object[][] conformity(List<Map<String, Object>> list, Map<Integer, String> titles) {
        Object[][] objects = new Object[list.size()][titles.size()];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < titles.size(); j++) {
                objects[i][j] = list.get(i).get(titles.get(j));
            }
        }
        return objects;
    }


    @Override
    public File conformity(String path) {
        return new File(path);
    }

    @Override
    public List<Map<String, Object>> conformity(Object[][] obj, String[] title) throws IndexOutOfBoundsException, NullPointerException {
        if (obj == null || obj.length == 0 || title == null || title.length == 0) {
            log.error("Unable to invoke an empty data.");
            throw new NullPointerException("Unable to invoke an empty data. Did you forgot to read file or clean it?");
        }
        int lenx = obj[0].length;
        int leny = obj.length;
        if (title.length < lenx) {
            throw new IndexOutOfBoundsException("Title length of title does not match the content");
        }
        for (Object[] objects : obj) {
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < title.length; j++) {
                if (j < obj[0].length) {
                    map.put(title[j], objects[j]);
                } else {
                    log.warn("WARRING:The data is null and will be replaced with a null character: Row");
                    map.put(title[j], "");
                }
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public void check_titleLine(int titleLine, int maxRow)throws IndexOutOfBoundsException {
        if (titleLine > maxRow || titleLine < 0) {
            log.error("Invalid title");
            throw new IndexOutOfBoundsException(titleLine);
        }
    }

    @Override
    public void check_row_col(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol) {
       try{
           if (startCol < 0 || startRow < 0 || endWithRow < 0 || endWithCol < 0) {
               throw new IndexOutOfBoundsException(-1);
           }
           if (startCol > endWithCol || startRow > endWithRow) {
               throw new IndexOutOfBoundsException(startCol>endWithCol?startCol:startRow);
           }
           if (startCol > maxCol || startRow > maxRow) {
               throw new IndexOutOfBoundsException(startCol>maxCol?startCol:startRow);
           }
           if (endWithCol > maxCol || endWithRow > maxRow) {
               throw new IndexOutOfBoundsException(endWithCol>maxCol?endWithCol:endWithRow);
           }
       }catch (IndexOutOfBoundsException e){
           log.error("Invalid row or col,read failed");
           throw e;
       }
    }
}
