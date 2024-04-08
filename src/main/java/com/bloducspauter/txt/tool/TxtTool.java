package com.bloducspauter.txt.tool;

import com.bloducspauter.origin.tool.MyTool;
import com.bloducspauter.excel.ExcelUtil;
import com.bloducspauter.excel.tool.ExcelToolImpl;
import com.bloducspauter.origin.FileReadAndOutPutUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文本文件工具
 *
 * @author Bloduc Spauter
 */
public class TxtTool extends MyTool {
    MyTool myTool = new ExcelToolImpl();

    @Override
    public void checkSuffix(File file) {
        checkSuffix(file.getName());
    }

    @Override
    public void checkSuffix(String path) {
        if ((path.endsWith(FileReadAndOutPutUtil.SUFFIX_5))) {
            throw new IllegalArgumentException("\tUnsupported suffix. It need '" + FileReadAndOutPutUtil.SUFFIX_5 + "' file,but you provide a unsupported file");
        }
    }

    @Override
    public void checkFile(File file) throws FileNotFoundException {
        myTool.checkFile(file);
    }

    @Override
    public void checkFile(String path) throws FileNotFoundException {
        myTool.checkFile(path);
    }

    @Override
    public void checkIsDirectory(File file) throws IOException {
        myTool.checkIsDirectory(file);
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
    public void checkTitleLine(int titleLine, int maxRow) {
        myTool.checkTitleLine(titleLine, maxRow);
    }

    @Override
    public void checkRowCol(int startRow, int startCol, int endWithRow, int endWithCol, int maxRow, int maxCol) {
        myTool.checkRowCol(startRow, startCol, endWithRow, endWithCol, maxRow, maxCol);
    }

    @Test
    public void rest() throws IOException {
        new ExcelUtil().readToList("C:\\1.xlsx");
    }
}
