package com.bloducspauter.excelutil.ewxce.write;

import com.bloducspauter.excelutil.annotation.FiledProperty;
import com.bloducspauter.excelutil.ewxce.wrapper.WriteWrapper;
import com.bloducspauter.excelutil.base.init.TableDefinition;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class CellWriter<T> extends RawUseCellWriter{

    public CellWriter(){
        throw new  UnsupportedOperationException("This class is not allowed to be instantiated");
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
}
