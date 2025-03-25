package com.bloducspauter.excelutil.base.init;

import com.bloducspauter.excelutil.annotation.FiledProperty;
import com.bloducspauter.excelutil.annotation.TableProperty;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 获取{@link TableDefinition}
 * @author Bloduc Spauter
 * @version 1.18
 * @since 1.19
 */
public class FiledPropertyLoader {

    private static final int MAX_VALUE=0x10000;

    /**
     *
     * @param configClass 被提供类
     * @return TableDefinition {@link TableDefinition}
     */
    public static TableDefinition getTableDefinition(Class<?> configClass) {
        TableProperty tableProperty=configClass.getAnnotation(TableProperty.class);
        boolean ignoreOtherCells= tableProperty != null && tableProperty.ignoreOtherCells();
        Field[] fields = configClass.getDeclaredFields();
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
        TreeMap<Integer, Field> treeMap = new TreeMap<>();
        TableDefinition tableDefinition= new TableDefinition(fields,configClass,map,treeMap,ignoreOtherCells);
        treeMap=integerStringTreeMap(tableDefinition);
        return new TableDefinition(fields,configClass,map,treeMap,ignoreOtherCells);
    }


    /**
     * @param tableDefinition {@link  TableDefinition}
     * @return {@link TreeMap}
     * @throws IndexOutOfBoundsException 因为XSSFWorkbook和HSSFWorkbook最大能读取65535行数据,如果{@link FiledProperty#index() }大于此值抛出异常
     */
    public static TreeMap<Integer, Field> integerStringTreeMap(TableDefinition tableDefinition) {
        TreeMap<Integer, Field> treeMap = new TreeMap<>();
        Map<String, Field> fieldMap = tableDefinition.cellNameAndField();
        int i = 0;
        for (Map.Entry<String, Field> m : fieldMap.entrySet()) {
            FiledProperty cellName = m.getValue().getAnnotation(FiledProperty.class);
            int index = cellName == null ? MAX_VALUE : cellName.index();
            //判断index是否大于65535，因为XSSFWorkbook和HSSFWorkbook最大能读取65535行数据
            if (index > MAX_VALUE||index<0) {
                String fieldName=  m.getValue().getName();
                throw new IndexOutOfBoundsException("The index of field "+fieldName+" is out of bounds");
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
