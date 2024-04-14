package com.bloducspauter.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
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
 *
 * @author Bloduc Spauter
 */
public class BsExcelUtil<T> extends ExcelUtil {


    private final TableDefinition entityTableDefinition;

    /**
     * 初始化实体类,使用{@link MyAnnotationConfigApplicationContext#getTableDefinition(Class)}
     * <p>
     * 读取该类的{@link com.bloducspauter.annotation.ExcelTable}和{@link com.bloducspauter.annotation.ExcelField}
     * <p>
     * 并将相关结果存入{@link TableDefinition}
     *
     * @param entity 实体类
     */
    public BsExcelUtil(Class<?> entity) {
        MyAnnotationConfigApplicationContext myAnnotationConfigApplicationContext = new MyAnnotationConfigApplicationContext();
        entityTableDefinition = myAnnotationConfigApplicationContext.getTableDefinition(entity);
    }

    /**
     * 返回一个实体类
     * @param t 实体
     * @return
     */
    public T getEntity(T t) {
        return t;
    }


    /**
     * 将Excel获取的数据以Json字符串形式存入List集合中
     *
     * @param file 文件路径
     * @return {@code List<String>}
     * @throws IOException IO流异常
     */
    @SuppressWarnings("unchecked")
    public List<T> readImpl(String file) throws IOException {
        Class<?> entity = entityTableDefinition.getClassName();
        List<T> objects = new ArrayList<>();
        try {
            sheet = super.getSheet(file);
            maxRow = sheet.getLastRowNum();
            maxCol = sheet.getRow(titleLine).getLastCellNum();
        } catch (NotOLE2FileException e) {
            System.out.println("This error may occur if you are using HSSFWorkbook to read CSV files, as HSSFWorkbook is primarily used to handle Excel file formats based on OLE2 (Object Linking and Embedding), Instead of a plain text CSV file.\n" +
                    "You should use Apache Commons CSV or direct Java file read operations to read CSV files, which is much simpler and more efficient. Here is sample code for reading a CSV file using Apache Commons CSV:");
            System.out.println(("Reading file failed"));
            throw e;
        } catch (Exception e) {
            System.out.println(("Reading file failed"));
            throw e;
        }
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
                Field field = entityTableDefinition.getCellNameAndField().get(title);
                if (field == null) {
                    String err = "THe field \"" + title + "\" does not exists in this field in class:" +
                            entityTableDefinition.getClassName().getName();
                    throw new IllegalArgumentException(err);
                }
                String filedName = field.getName();
                map.put(filedName, o);
            }
            String jsonString = JSON.toJSONString(map);
            Object o = JSONObject.parseObject(jsonString, entity);
            objects.add((T) o);
        }
        return objects;
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
     * @param path 文件路径
     * @return {@code List<Object}带有Json字符串的List集合
     * @throws IOException IO流异常
     */
    public List<T> readFile(String path) throws IOException {
        return readImpl(path);
    }


    public List<T> readFile(File file) throws IOException {
        return readFile(file.getAbsolutePath());
    }


    public void outPutFile(String sheetName, String path, List<T> entities) {

    }

    public void outPutFile(String sheetName, File  file, List<T> entities) {

    }
}
