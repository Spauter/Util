package com.bloducspauter.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.excel.output.BsOutputExcel;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import com.bloducspauter.excel.read.BsReadServise;
import org.apache.poi.ss.usermodel.Cell;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用Json字符串接收Excel表格数据,继承自{@link ExcelUtil}
 * @author Bloduc Spauter
 */
public class BsExcelUtil extends ExcelUtil implements BsReadServise, BsOutputExcel {


    private final TableDefinition entityTableDefinition;

    /**
     *  初始化实体类,使用{@link MyAnnotationConfigApplicationContext#getTableDefinition(Class)}
     *  <p>
     *  读取该类的{@link com.bloducspauter.annotation.ExcelTable}和{@link com.bloducspauter.annotation.ExcelField}
     *  <p>
     *  并将相关结果存入{@link TableDefinition}
     * @param entity 实体类
     */
    public BsExcelUtil(Class<?> entity) {
        MyAnnotationConfigApplicationContext myAnnotationConfigApplicationContext = new MyAnnotationConfigApplicationContext();
        entityTableDefinition = myAnnotationConfigApplicationContext.getTableDefinition(entity);
    }


    /**
     * 将Excel获取的数据以Json字符串形式存入List集合中
     *
     * @param file 文件路径
     * @return {@code List<String>}
     * @throws IOException IO流异常
     */
    private List<String> readImpl(String file) throws IOException {
        List<String> stringList = new ArrayList<>();
        sheet = super.getSheet(file);
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
                    throw new IllegalArgumentException(err);
                }
                String filedName = field.getName();
                map.put(filedName, o);
            }
            String jsonString = JSON.toJSONString(map);
            stringList.add(jsonString);
        }
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
        System.out.println(("This method is deprecated in the class,"));
    }

    /**
     * 设置截止列，在此类中不适用
     *
     * @param endWithCol 截止列
     */
    @Deprecated
    @Override
    public void setEndWithCol(int endWithCol) {
        System.out.println(("This method is deprecated in the class,"));
    }


    /**
     *
     * @param path 文件路径
     * @return {@code List<Object}带有Json字符串的List集合
     * @throws IOException IO流异常
     */
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

    @Override
    public void outPutFile(String sheetName, String path, List<Object> entities) {

    }
}
