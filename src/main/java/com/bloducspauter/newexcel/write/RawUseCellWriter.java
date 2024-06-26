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
    static Row row;

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

    public static Cell createCell(int columnNum) {
        return row.createCell(columnNum);
    }

}