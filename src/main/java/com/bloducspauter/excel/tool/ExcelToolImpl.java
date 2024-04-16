package com.bloducspauter.excel.tool;


import com.bloducspauter.origin.tool.MyTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bloducspauter.origin.FileReadAndOutPutUtil.*;

/**
 * 表格文档检查
 *
 * @author Bloduc Spauter
 * @see MyTool
 * @version 1.16
 */
public class ExcelToolImpl extends ExcelTool {
    private final List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public void checkSuffix(File file) throws IOException {
        checkIsDirectory(file);
        checkSuffix(file.getName());
    }

    @Override
    public void checkSuffix(String path) {
        if (!(path.endsWith(SUFFIX_1) || (path.endsWith(SUFFIX_2)) || path.endsWith(SUFFIX_6))) {
            throw new IllegalArgumentException("Unsupported suffix. It need 'xls' or 'xlsx' file,but you provide a unsupported file");
        }
    }

    @Override
    public void checkFile(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("The file is not found:" + file.getAbsoluteFile());
        }
    }

    @Override
    public void checkFile(String path) throws FileNotFoundException {
        checkFile(new File(path));
    }

    @Override
    public void checkIsDirectory(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("The folder cannot be read or written.");
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

    @Override
    public void checkTitleLine(int titleLine, int maxRow) throws IndexOutOfBoundsException {
        if (titleLine > maxRow || titleLine < 0) {
            throw new IndexOutOfBoundsException(titleLine);
        }
    }

    @Override
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

    /**
     *
     * @param columnNumber
     * @return
     */
    @Override
    public  String convertToExcelColumn(int columnNumber) {
        StringBuilder columnName = new StringBuilder();

        while (columnNumber > 0) {
            // 计算余数
            int remainder = (columnNumber - 1) % 26;
            // 将余数对应的字母添加到结果字符串的开头
            columnName.insert(0, (char) ('A' + remainder));
            // 更新整数，除以26向下取整
            columnNumber = (columnNumber - 1) / 26;
        }

        return columnName.toString();
    }
}
