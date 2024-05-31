package com.bloducspauter.origin.service;

import com.bloducspauter.origin.exceptions.UnsupportedFileException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 * @version 1.18.2
 */
public abstract class ValidationTool {

    List<Map<String, Object>> list = new ArrayList<>();

    /**
     * 通过文件路径检查文件名的方法
     * @param path 文件路径
     * @throws com.bloducspauter.origin.exceptions.UnsupportedFileException 当文件参数不合法时
     */
    public void checkSuffix(String path) throws UnsupportedFileException {
        File file = new File(path);
        checkSuffix(file);
    }
    /**
     * 检查文件名的方法
     * @param file 文件
     * @throws com.bloducspauter.origin.exceptions.UnsupportedFileException 当文件参数不合法时
     */
    public abstract void checkSuffix(File file) throws UnsupportedFileException;

    /**
     * 通过文件路径检查文件是否存在
     * @param path 文件路径
     */
    public boolean checkFileExists(String path) {
        File file = new File(path);
        return checkFileExists(file);
    }

    /**
     * 检查文件是否存在
     * @param file 文件
     */
    public boolean checkFileExists(File file) {
        return file.exists();
    }

    /**
     * 判断受否为文件目录
     * @param file 文件
     */
    public boolean checkIsDirectory(File file) {
        return file.isDirectory();
    }

    /**
     * 通过文件路面判断受否为文件目录
     * @param path 文件目录
     */
    public boolean checkIsDirectory(String path) {
        File file = new File(path);
        return checkIsDirectory(file);
    }


    /**
     * 将一个{@code List<Map<String,Object>>}转化为一个二维数组
     * @param list List<Map<String,Object>>
     * @param titles 标题行
     */
    public Object[][] conformity(List<Map<String, Object>> list, Map<Integer, String> titles) {
        Object[][] objects = new Object[list.size()][titles.size()];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < titles.size(); j++) {
                objects[i][j] = list.get(i).get(titles.get(j));
            }
        }
        return objects;
    }

    public Object[][] conformity(List<Map<String, Object>> list,String[] titles){
        Map<Integer,String>map=new HashMap<>();
        for (int i=0;i< titles.length;i++) {
            map.put(i,titles[i]);
        }
        return conformity(list,map);
    }

    /**
     * 将一个二维数组和一个标题行转化为{@code List<Map<String,Object>>}
     * @param obj 二维数组
     * @param title 标题
     */
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

    public String getSuffix(String filename){
        if (filename == null) {
            throw new NullPointerException();
        }
        String[] parts=filename.split("\\.");
        if (parts.length == 1) {
            return filename;
        }
        String suffix;
        suffix = parts[parts.length - 1];
        return suffix;
    }
}
