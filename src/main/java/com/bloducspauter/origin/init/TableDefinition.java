package com.bloducspauter.origin.init;


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
    private Class<?> className;
    private Map<String,Field> cellNameAndField;
    private TreeMap<Integer,Field> indexForCellName;
}
