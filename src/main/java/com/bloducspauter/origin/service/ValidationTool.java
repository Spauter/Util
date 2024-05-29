package com.bloducspauter.origin.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 */
public abstract class ValidationTool {

    List<Map<String, Object>> list = new ArrayList<>();

    /**
     * 检查文件名的方法
     * @param path 文件路径
     * @throws com.bloducspauter.origin.exceptions.UnsupportedFileException 当文件参数不合法时
     */
    public void checkSuffix(String path) {
        File file = new File(path);
        checkSuffix(file);
    }

    protected abstract void checkSuffix(File file);

    public boolean checkFileExists(String path) {
        File file = new File(path);
        return checkFileExists(file);
    }

    public boolean checkFileExists(File file) {
        return file.exists();
    }

    public boolean checkIsDirectory(File file) {
        return file.isDirectory();
    }

    public boolean checkIsDirectory(String path) {
        File file = new File(path);
        return checkIsDirectory(file);
    }


    public Object[][] conformity(List<Map<String, Object>> list, Map<Integer, String> titles) {
        Object[][] objects = new Object[list.size()][titles.size()];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < titles.size(); j++) {
                objects[i][j] = list.get(i).get(titles.get(j));
            }
        }
        return objects;
    }


    public List<Map<String, Object>> conformity(Object[][] obj, String[] title) throws IndexOutOfBoundsException, NullPointerException {
        if (obj == null || obj.length == 0 || title == null || title.length == 0) {
            throw new NullPointerException("Unable to invoke an empty data. Did you forgot to read file or clean it?");
        }
        int lens = obj[0].length;
        if (title.length < lens) {
            throw new IndexOutOfBoundsException("Title length of title does not match the content");
        }
        for (Object[] objects : obj) {
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < title.length; j++) {
                if (j < obj[0].length) {
                    map.put(title[j], objects[j]);
                } else {
                    map.put(title[j], "");
                }
            }
            list.add(map);
        }
        return list;
    }

    public void checkTitleLine(int titleLine, int maxRow) throws IndexOutOfBoundsException {
        if (titleLine > maxRow || titleLine < 0) {
            throw new IndexOutOfBoundsException(titleLine);
        }
    }


    public void checkRowCol(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol) {
        if (startCol < 0 || startRow < 0 || endWithRow < 0 || endWithCol < 0) {
            throw new IndexOutOfBoundsException(-1);
        }
        if (startCol > endWithCol || startRow > endWithRow) {
            throw new IndexOutOfBoundsException(startCol > endWithCol ? startCol : startRow);
        }
        if (startCol > maxCol || startRow > maxRow) {
            throw new IndexOutOfBoundsException(startCol > maxCol ? startCol : startRow);
        }
        if (endWithCol > maxCol || endWithRow > maxRow) {
            throw new IndexOutOfBoundsException(endWithCol > maxCol ? endWithCol : endWithRow);
        }
    }
}
