package com.bloducspauter.newexcel.read;

import com.bloducspauter.origin.init.TableDefinition;
import org.apache.poi.ss.usermodel.Sheet;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取某一行的单元格
 *
 * @author Bloduc Spauter
 */
public class RowDataReader {
    /**
     * 读取某一行的单元格
     */
    public static Map<String, Object> read(Sheet sheet, TableDefinition tableDefinition, Map<Integer, String> titles,
                                           int row, int startRol, int maxCol, String dateformat) throws NoSuchFieldException {
        Map<String, Object> map = new HashMap<>();
        for (int rol = startRol; rol < maxCol; rol++) {
            Object o = CellReader.getCellValue(sheet, row, rol, dateformat);
            String title = titles.get(rol);
            Field field = tableDefinition.getCellNameAndField().get(title);
            if (field == null) {
                String err = "The field \"" + title + "\" does not exists in this field in class:" +
                        tableDefinition.getClassName().getSimpleName();
                throw new NoSuchFieldException(err);
            }
            String filedName = field.getName();
            map.put(filedName, o);
        }
        return map;
    }

    Map<String, Object> read(Sheet sheet, Map<Integer, String> titles
    ,int row, int startRol, int maxCol, String dateformat) {
        Map<String, Object> map = new HashMap<>();
        for (int rol = startRol; rol < maxCol; rol++) {
            Object o = CellReader.getCellValue(sheet, row, rol, dateformat);
            String title = titles.get(rol);
            map.put(title, o);
        }
        return map;
    }
}
