package com.bloducspauter.origin.init;

import com.bloducspauter.annotation.FiledProperty;


import java.lang.reflect.Field;
import java.util.*;

/**
 * 获取{@link TableDefinition}
 * @author Bloduc Spauter
 * @version 1.18
 */
public class MyAnnotationConfigApplicationContext {

    private static final int MAX_VALUE=0x10000;

    /**
     *
     * @param configClass 被提供类
     * @return TableDefinition {@link TableDefinition}
     */
    public static TableDefinition getTableDefinition(Class<?> configClass) {
        TableDefinition tableDefinition = new TableDefinition();
        Field[] fields = configClass.getDeclaredFields();
        tableDefinition.setFields(fields);
        tableDefinition.setClassName(configClass);
        Map<String, Field> map = new HashMap<>();
        for (Field f : fields) {
            FiledProperty cellName = f.getAnnotation(FiledProperty.class);
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
        TreeMap<Integer, Field> treeMap = integerStringTreeMap(tableDefinition);
        tableDefinition.setIndexForCellName(treeMap);
        return tableDefinition;
    }


    /**
     * 进一步获取TreeMap后存入{@link TableDefinition#setIndexForCellName(TreeMap)}
     * @param tableDefinition {@link  TableDefinition}
     * @return {@link TreeMap}
     * @throws IndexOutOfBoundsException 因为XSSFWorkbook和HSSFWorkbook最大能读取65535行数据,如果{@link FiledProperty#index() }大于此值抛出异常
     */
    public static TreeMap<Integer, Field> integerStringTreeMap(TableDefinition tableDefinition) {
        TreeMap<Integer, Field> treeMap = new TreeMap<>();
        Map<String, Field> fieldMap = tableDefinition.getCellNameAndField();
        int i = 0;
        for (Map.Entry<String, Field> m : fieldMap.entrySet()) {
            FiledProperty cellName = m.getValue().getAnnotation(FiledProperty.class);
            int index = cellName == null ? MAX_VALUE : cellName.index();
            //判断index是否大于65535，因为XSSFWorkbook和HSSFWorkbook最大能读取65535行数据
            if (index > MAX_VALUE) {
                String fieldName=  m.getValue().getName();
                System.out.println("A error occurred at analyzing class:"+tableDefinition.getClassName());
                System.out.println("@ExcelField(value=\""+cellName.value()+"\",index="+cellName.index()+")\n" +
                                    m.getValue());
                throw new IndexOutOfBoundsException("The field \""+fieldName+"\" annotation's index is out of range:"+index);
            }
            if(treeMap.get(index)==null){
                if (index !=MAX_VALUE) {
                    treeMap.put(index, m.getValue());
                } else {
                    treeMap.put(MAX_VALUE+i, m.getValue());
                }
            }else {
                treeMap.put(MAX_VALUE + i, m.getValue());
            }
            i++;
        }
        return treeMap;
    }
}
