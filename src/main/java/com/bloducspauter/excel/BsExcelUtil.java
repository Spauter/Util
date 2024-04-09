package com.bloducspauter.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import com.bloducspauter.excel.read.BsReadServise;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 */
@Slf4j
public class BsExcelUtil extends ExcelUtil implements BsReadServise {


    private final TableDefinition entityTableDefinition;


    public BsExcelUtil(Class<?> entity) {
        MyAnnotationConfigApplicationContext myAnnotationConfigApplicationContext = new MyAnnotationConfigApplicationContext();
        entityTableDefinition = myAnnotationConfigApplicationContext.getTableDefinition(entity);
    }


    private List<String> readImpl(String file) throws IOException {
        List<String> stringList = new ArrayList<>();
        try {
            sheet = super.getSheet(file);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            log.error("Reading file failed");
            throw e;
        }
        log.info("File check passed,Starting read");
        maxRow = sheet.getLastRowNum();
        maxCol = sheet.getRow(titleLine).getLastCellNum();
        super.setEndWithRow(maxRow);
        super.setEndWithCol(maxCol);
        super.readTitle(sheet);
        for (int row = 0; row < maxRow; row++) {
            //如果是标题行则跳过
            if (row == titleLine) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            for (int rol = 0; rol < maxCol; rol++) {
                Cell info = sheet.getRow(row).getCell(rol);
                Object o = getCellValue(info);
                String title = titles.get(rol);
                Field field=entityTableDefinition.getCellNameAndField().get(title);
                if (field == null) {
                    String err="THe field \""+title+"\" does not exists in this field in class:" +
                            entityTableDefinition.getClassName().getName();
                    log.error(err);
                    throw new IllegalArgumentException(err);
                }
                String filedName = field.getName();
                map.put(filedName, o);
            }
            String jsonString = JSON.toJSONString(map);
            stringList.add(jsonString);
        }
        log.info("Reading successfully");
        return stringList;
    }

    /**
     * 设置起始列，在此类中不适用
     *
     * @param startCol 截止列
     */
    @Override
    @Deprecated
    public void setStartCol(int startCol) {
        log.warn("This method is deprecated in the class,");
    }

    /**
     * 设置截止列，在此类中不适用
     *
     * @param endWithCol 截止列
     */
    @Deprecated
    @Override
    public void setEndWithCol(int endWithCol) {
        log.warn("This method is deprecated in the class,");
    }


    @Override
    public List<Object> readFile(String path) throws IOException {
        Class<?> entity = entityTableDefinition.getClassName();
        List<String> stringList = readImpl(path);
        List<Object> objects = new ArrayList<>();
        for (String s : stringList) {
            Object o = JSONObject.parseObject(s, entity);
            objects.add(o);
        }
        return objects;
    }

    @Override
    public List<Object> readFile(File file) throws IOException {
        return readFile(file.getAbsolutePath());
    }
}
