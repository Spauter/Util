package com.bloducspauter.txt;

import com.bloducspauter.origin.tool.MyTool;
import com.bloducspauter.txt.output.ReadTxt;
import com.bloducspauter.txt.read.OutPutTxt;
import com.bloducspauter.txt.tool.TxtTool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TxtUtil implements ReadTxt, OutPutTxt {
    List<Map<String,Object>>mapList=new ArrayList<>();
    String[] titles;

    private int maxRows;
    private int maxCols;

    MyTool tool= new TxtTool();

    List<Map<String,Object>>list=new ArrayList<>();

    private String[] titleLoad(){
        return null;
    }

    private List<Map<String,Object>>readImpl(File  file) throws IOException {
        tool.checkFile(file);
        tool.checkSuffix(file);
        FileReader iis = null;
        BufferedReader bfr = null;
        try {
            iis = new FileReader(file);
            bfr = new BufferedReader(iis);
            String info = null;
            while ((info = bfr.readLine()) != null) {
//                list.add(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != iis) {
            try {
                iis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }



    @Override
    public void output(String excelFilePath, String fileName) throws IOException {

    }

    @Override
    public int getMaxRows() {
        return 0;
    }

    @Override
    public int getMaxCols() {
        return 0;
    }

    @Override
    public void clearAll() {

    }

    @Override
    public void outPut(List<Map<String, Object>> list, String Path) throws IOException {

    }

    @Override
    public void outPut(List<Map<String, Object>> list, File file) throws IOException {

    }

    @Override
    public void outPut(Object[][] obj, String[] title, String Path) throws IOException {

    }

    @Override
    public void outPut(Object[][] obj, String[] title, File file) throws IOException {

    }

    @Override
    public void outPut(String path) throws IOException {

    }

    @Override
    public void outPut(File file) throws IOException {

    }

    @Override
    public List<Map<String, Object>> readToList(String path) throws IOException {
        return null;
    }

    @Override
    public List<Map<String, Object>> readToList(File file) throws IOException {
        return null;
    }

    @Override
    public List<Map<String, Object>> readToList() throws IOException {
        return null;
    }

    @Override
    public Object[][] readToArray(String path) throws IOException {
        return new Object[0][];
    }

    @Override
    public Object[][] readToArray(File file) throws IOException {
        return new Object[0][];
    }

    @Override
    public Object[][] readToArray() throws IOException {
        return new Object[0][];
    }

    @Override
    public void setTitleLine(int titleLine) {

    }

    @Override
    public void setStartRow(int startRow) {

    }

    @Override
    public void setStartCol(int startCol) {

    }

    @Override
    public void setEndWithCol(int endWithCol) {

    }

    @Override
    public void setEndWithRow(int endWithRow) {

    }
}
