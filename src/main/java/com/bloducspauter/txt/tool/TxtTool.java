package com.bloducspauter.txt.tool;

import com.bloducspauter.MyTool;
import com.bloducspauter.excel.tool.ExcelToolImpl;
import com.bloducspauter.FileReadAndOutPutUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TxtTool extends MyTool {
    MyTool myTool = new ExcelToolImpl();

    @Override
    public void Check_suffix(File file) {
        Check_suffix(file.getName());
    }

    @Override
    public void Check_suffix(String path) {
        if ((path.endsWith(FileReadAndOutPutUtil.SUFFIX_5))) {
            throw new IllegalArgumentException("\tUnsupported suffix. It need '" + FileReadAndOutPutUtil.SUFFIX_5 + "' file,but you provide a unsupported file");
        }
    }

    @Override
    public void Check_file(File file) throws FileNotFoundException {
        myTool.Check_file(file);
    }

    @Override
    public void Check_file(String path) throws FileNotFoundException {
        myTool.Check_file(path);
    }

    @Override
    public String PrintInfo(String content, int color, int type) {
        return myTool.PrintInfo(content, color, type);
    }

    @Override
    public void Check_IsDirectory(File file) throws IOException {
        myTool.Check_IsDirectory(file);
    }

    @Override
    public Object[][] conformity(List<Map<String, Object>> list, Map<Integer, String> titles) {
        return myTool.conformity(list, titles);
    }

    @Override
    public File conformity(String path) {
        return myTool.conformity(path);
    }

    @Override
    public List<Map<String, Object>> conformity(Object[][] obj, String[] title) throws IndexOutOfBoundsException, NullPointerException {
        return myTool.conformity(obj, title);
    }

    @Override
    public void check_titleLine(int titleLine, int maxRow) {
        myTool.check_titleLine(titleLine, maxRow);
    }

    @Override
    public void check_row_col(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol) {

    }
}
