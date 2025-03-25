package com.bloducspauter.excelutil.base.init;


import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author BS
 * @version 1.18
 */
public record TableDefinition(Field[] fields, Class<?> className, Map<String, Field> cellNameAndField,
                              TreeMap<Integer, Field> indexForCellName, boolean ignoreOtherCells) {
}
