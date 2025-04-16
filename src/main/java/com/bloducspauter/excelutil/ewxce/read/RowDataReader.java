package com.bloducspauter.excelutil.ewxce.read;

import com.bloducspauter.excelutil.base.init.TableDefinition;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取某一行的单元格
 *
 * @author Bloduc Spauter
 */
public class RowDataReader {

    public RowDataReader(){
        throw new  UnsupportedOperationException("This class is not allowed to be instantiated");
    }

    /**
     * 读取某一行的单元格
     */
    public static Map<String, Object> readToFieldKeyMap(Sheet sheet, TableDefinition tableDefinition, Map<Integer, String> titles,
                                                        int row, int startRol, int maxCol, String dateformat) throws NoSuchFieldException {
        Map<String, Object> map = new HashMap<>();
        for (int rol = startRol; rol < maxCol; rol++) {
            Object o = CellReader.getCellValue(sheet, row, rol, dateformat);
            String title = titles.get(rol);
            Field field = tableDefinition.getCellNameAndField().get(title);
            if (field != null) {
                String filedName = field.getName();
                map.put(filedName, o);
            } else  {
                if (tableDefinition.isIgnoreOtherCells()) {
                    continue;
                }
                String err = "The field \"" + title + "\" does not exists in this field in class:" +
                        tableDefinition.getClassName().getSimpleName();
                throw new NoSuchFieldException(err);
            }

        }
        return map;
    }

    public static Map<String, Object> readToSimpleKeyMap(Sheet sheet, Map<Integer, String> titles
            , int row, int startRol, int maxCol, String dateformat) {
        Map<String, Object> map = new HashMap<>();
        for (int rol = startRol; rol < maxCol; rol++) {
            Object o = CellReader.getCellValue(sheet, row, rol, dateformat);
            String title = titles.get(rol);
            map.put(title, o);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> T readToEntity(Sheet sheet, TableDefinition tableDefinition, Map<Integer, String> titles,
                                          int row, int startRol, int maxCol, String dateformat) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object entity = tableDefinition.getClassName().getConstructor().newInstance();
        for (int rol = startRol; rol < maxCol; rol++) {
            Object value = CellReader.getCellValue(sheet, row, rol, dateformat);
            String title = titles.get(rol);
            Field field = tableDefinition.getCellNameAndField().get(title);
            if (field != null) {
                String filedName = field.getName();
                Method setter=tableDefinition.getClassName().getMethod("set"+filedName.substring(0,1).toUpperCase()+filedName.substring(1),field.getType());
                entity=setter.invoke(entity,value);
            } else  {
                if (tableDefinition.isIgnoreOtherCells()) {
                    continue;
                }
                String err = "The field \"" + title + "\" does not exists in this field in class:" +
                        tableDefinition.getClassName().getSimpleName();
                throw new NoSuchFieldException(err);
            }
        }
        return (T) entity;
    }
}
