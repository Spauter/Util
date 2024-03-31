package com.bloducspauter.excel.tool;


import com.bloducspauter.MyTool;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bloducspauter.FileReadAndOutPutUtil.SUFFIX_1;
import static com.bloducspauter.FileReadAndOutPutUtil.SUFFIX_2;

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
            throw new IllegalArgumentException("\tUnsupported suffix. It need 'xls' or 'xlsx' file,but you provide a unsupported file");
        }
    }

    @Override
    public void Check_file(File file) throws FileNotFoundException {
        if (!file.exists()) {
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
            throw new IOException("The folder cannot be read or written.");
        }
    }

    //  样式
//      0  空样式
//      1  粗体
//      4  下划线
//      7  反色
//  颜色：
//      30  白色
//      31  红色
//      32  绿色
//      33  黄色
//      34  蓝色
//      35  紫色
//      36  浅蓝
//      37  灰色
//      背景颜色：40-47 和颜色顺序相同
//      颜色2：90-97  比颜色1更鲜艳一些
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
            throw new NullPointerException("Unable to invoke an empty data. Did you forgot to read file or clean it?");
        }
        int lenx = obj[0].length;
        int leny = obj.length;
        if (title.length < lenx) {
            throw new IndexOutOfBoundsException("Title length of title does not match the content");
        }
        for (int i = 0; i < leny; i++) {
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < title.length; j++) {
                if (j < obj[0].length) {
                    map.put(title[j], obj[i][j]);
                } else {
                    System.out.println(PrintInfo("WARRING:The data is null and will be replaced with a null character: Row " + (i + 1) + " " + "column " + (j + 1), 31, 0));
                    map.put(title[j], "");
                }
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public void check_titleLine(int titleLine, int maxrow) {
        if (titleLine > maxrow || titleLine < 0) {
            throw new IndexOutOfBoundsException(titleLine);
        }
    }

    @Override
    public void check_row_col(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol) {
        if (startCol < 0 || startRow < 0 || endWithRow < 0 || endWithCol < 0) {
            throw new IndexOutOfBoundsException(-1);
        }
        if (startCol > endWithCol || startRow > endWithCol) {
            throw new IndexOutOfBoundsException(startCol>endWithCol?startCol:startRow);
        }
        if (startCol > maxCol || startRow > maxRow) {
            throw new IndexOutOfBoundsException(startCol>maxCol?startCol:startRow);
        }
    }
}
