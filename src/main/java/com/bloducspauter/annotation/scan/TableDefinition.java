package com.bloducspauter.annotation.scan;


import lombok.Data;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author BS
 */
@Data
public class TableDefinition {
    private Field[] fields;
    private String className;
    private Map<String,Field> cellNameAndField;
    private TreeMap<Integer,String> indexForCellName;
}
