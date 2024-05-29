package com.bloducspauter.enums;
/**
 *
 * 常见excel表格类型
 * @author Bloduc Spauter
 *
 */
public enum ExcelType {
    XLS(".xls"),
    XLSX(".xlsx"),
    CSV(".csv"),
    Unknown("Unknown");
    final String typeName;

    ExcelType(String typeName) {
        this.typeName = typeName;
    }
}

