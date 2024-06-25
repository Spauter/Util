package com.bloducspauter.newexcel.write;

import com.bloducspauter.annotation.FiledProperty;
import com.bloducspauter.newexcel.wrapper.WriteWrapper;
import com.bloducspauter.origin.init.TableDefinition;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
/**
 * @author Bloduc Spauter
 *
 */
public class RawUseCellWriter {
    private static Row row;

    public static Row createContent(WorkbookWriter workbookWriter, WriteWrapper writeWrapper,
                         int rowNum, int cloumnNum,
                         Map<String, Object> value, Map<Integer, String> title) {
        row = workbookWriter.getSheet(writeWrapper).createRow(rowNum);
        for (int i = 0; i < cloumnNum; i++) {
            Object o=value.getOrDefault(title.get(i),"");
            createCell(cloumnNum).setCellValue(o.toString());
        }
        return row;
    }

    public static void createTitle(WorkbookWriter workbookWriter, WriteWrapper writeWrapper,Map<Integer, String> title)  {
       row = workbookWriter.getSheet(writeWrapper).createRow(0);
        for (int i = 0; i < title.size(); i++) {
            row.createCell(i).setCellValue(title.get(i));
        }
    }

    public static void createTitle(WorkbookWriter workbookWriter, WriteWrapper writeWrapper, TableDefinition tableDefinition) {
        row = workbookWriter.getSheet(writeWrapper).createRow(0);
        TreeMap<Integer, Field> fieldTreeMap = tableDefinition.getIndexForCellName();
        int cellIndex = 0;
        for (Map.Entry<Integer, Field> entry : fieldTreeMap.entrySet()) {
            Field field = entry.getValue();
            FiledProperty excelField = field.getAnnotation(FiledProperty.class);
            String title = (excelField != null && !excelField.value().isEmpty()) ? excelField.value() : field.getName();
            row.createCell(cellIndex++).setCellValue(title);
        }
    }

    public static Cell createCell(int columnNum) {
        return row.createCell(columnNum);
    }

}
