package com.bloducspauter.excel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.excel.read.CellReader;
import com.bloducspauter.excel.read.TitleReader;
import com.bloducspauter.excel.read.WorkBookReader;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import com.bloducspauter.origin.wrapper.ReadWrapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 */
public class WrapperExcelUtil<T>  {
    private final TableDefinition tableDefinition;
    private ExcelTool excelTool;
    private Workbook workbook;
    private Sheet sheet;
    private int maxRow;
    private int maxColumn;
    private int startRow;
    private int startColumn;
    private int endRow;
    private int endColumn;
    private Map<Integer,String>titleMap;
    private ReadWrapper wrapper;
    /**
     * 初始化实体类,使用{@link MyAnnotationConfigApplicationContext#getTableDefinition(Class)}
     * <p>
     * 将相关结果存入{@link TableDefinition}
     *
     * @param entity 实体类
     */
    public WrapperExcelUtil(Class<?> entity,ReadWrapper readWrapper) throws Exception{
        tableDefinition = MyAnnotationConfigApplicationContext.getTableDefinition(entity);
        init(readWrapper);
    }

    private void init(ReadWrapper readWrapper) throws Exception {
        wrapper=readWrapper;
        int sheetNum=readWrapper.getReadSheetAt();
        WorkBookReader workBookReader=new WorkBookReader();
        workbook=workBookReader.getWorkbook(readWrapper);
        sheet= workBookReader.getSheet(sheetNum);
        maxRow=workBookReader.getMaxRow();
        maxColumn=workBookReader.getMaxColumn(readWrapper.getTitleLine());
        endRow=readWrapper.getEndRow();
        excelTool.checkRowCol(startRow,startColumn,endRow,endColumn,maxRow,maxColumn);
        titleMap= TitleReader.readTitle(sheet,sheetNum,startColumn,endColumn,excelTool);
    }

    @SuppressWarnings("unchecked")
    private List<T> read() throws GeneralSecurityException, IOException, NoSuchFieldException {
        String path=wrapper.getPath();
        excelTool.checkIsDirectory(path);
        excelTool.checkSuffix(path);
        Class<?> entity = tableDefinition.getClassName();
        List<T> objects = new ArrayList<>();
        int startRow=wrapper.getStartRow();
        int startColumn=wrapper.getStartColumn();
        endRow = wrapper.getEndRow() == -1 ? maxRow : endRow;
        for (int row = startRow; row < endRow; row++) {
            if (row == wrapper.getTitleLine()) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            for (int column = startColumn; column < endColumn; column++) {
                String dateformat = wrapper.getDateformat();
                Object cellValue= CellReader.getCellValue(sheet,row,column,dateformat);
                String title=titleMap.get(column);
                Field field =tableDefinition.getCellNameAndField().get(title);
                if (field == null) {
                    String err = "The field \"" + title + "\" does not exists in this field in class:" +
                            tableDefinition.getClassName().getSimpleName();
                    throw new NoSuchFieldException(err);
                }
                String filedName = field.getName();
                map.put(filedName, cellValue);
            }
            String jsonString = JSON.toJSONString(map);
            Object o = JSONObject.parseObject(jsonString, entity);
            try {
                objects.add((T) o);
            } catch (ClassCastException e) {
                System.out.println("Loading info failed in line " + row);
            }
        }
        return objects;
    }

    public List<T>readData() {
        int munCores = Runtime.getRuntime().availableProcessors();
        List<T> entities = new ArrayList<>();
        //todo 多线程读取
        return entities;
    }

    public List<T>readAll() throws Exception {
        return read();
    }

    public T readOne(int index) throws Exception {
        if (index == wrapper.getTitleLine()) {
            throw new IllegalArgumentException("Don't know how to turn title line" + " into class " + tableDefinition.getClassName().getSimpleName());
        }
        return read().get(0);
    }

    public void write(File file,List<T>entities) {
        //todo
    }

    public void write(String path,List<T>entities) {
        //todo
    }
}
