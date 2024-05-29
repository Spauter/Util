package com.bloducspauter.enums;

import org.apache.poi.ss.usermodel.CellType;

/**
 * 常见excel表格类型
 *
 * @author Bloduc Spauter
 */
public enum ExcelType {
    XLS("xls"),
    XLSX("xlsx"),
    CSV("csv"),
    Unknown("Unknown");
    public final String suffix;

    ExcelType(String suffix) {
        this.suffix = suffix;
    }

    public static ExcelType forSuffix(String suffix) {
        for (ExcelType type : values()) {
            if (type.suffix.equals(suffix)) {
                return type;
            }
        }
        return Unknown;
    }
}

