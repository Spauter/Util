package com.bloducspauter.annotation.scan;

import com.bloducspauter.annotation.CellName;
import com.bloducspauter.annotation.ExcelTable;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Bloduc Spauter
 */
@Slf4j
public class MyAnnotationConfigApplicationContext {

    public TableDefinition getTableDefinition(Class<?> configClass) throws Exception {
        TableDefinition tableDefinition = new TableDefinition();
        ExcelTable excelTable = configClass.getAnnotation(ExcelTable.class);
        if (excelTable == null) {
            log.error("Cannot get excel table");
            throw new Exception("Cannot get excel table");
        }
        String className = configClass.getName();
        Field[] fields = configClass.getDeclaredFields();
        tableDefinition.setFields(fields);
        tableDefinition.setClassName(className);
        int index;
        Map<String, Field> map = new HashMap<>();
        for (Field f : fields) {
            CellName cellName = f.getAnnotation(CellName.class);
            String key;
            //如果没有注解就把属性名作为key
            if (cellName == null) {
                key = f.getName();
                map.put(key, f);
            } else {
                //如果元素存在,就存值
                if (cellName.isExists()) {
                    //判断单元格真正的名字是否配置,如果没配置就把属性名作为key
                    key = cellName.value().isEmpty() ? f.getName() : cellName.value();
                    map.put(key, f);
                }
            }
        }
        tableDefinition.setCellNameAndField(map);
        TreeMap<Integer, String> treeMap = integerStringTreeMap(tableDefinition);
        tableDefinition.setIndexForCellName(treeMap);
        return tableDefinition;
    }


    public TreeMap<Integer, String> integerStringTreeMap(TableDefinition tableDefinition) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        Map<String, Field> fieldMap = tableDefinition.getCellNameAndField();
        int i = 0;
        for (Map.Entry<String, Field> m : fieldMap.entrySet()) {
            CellName cellName = m.getValue().getAnnotation(CellName.class);
            int index = cellName == null ? Integer.MAX_VALUE : cellName.index();
            if(treeMap.get(index)==null){
                if (index != Integer.MAX_VALUE) {
                    treeMap.put(index, m.getKey());
                } else {
                    treeMap.put(Integer.MAX_VALUE-i, m.getKey());
                }
            }else {
                treeMap.put(Integer.MAX_VALUE-i, m.getKey());
            }
            i++;
        }
        return treeMap;
    }


}
