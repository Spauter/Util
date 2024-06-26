package com.bloducspauter.enums;

import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;

import java.util.Arrays;

/**
 * 常见excel表格类型
 *
 * @author Bloduc Spauter
 * @version 1.18.2
 */
public enum ExcelType {
    XLS("xls"),
    XLSX("xlsx"),
    XLSM("xlsm"),
    CSV("csv");
    public final String suffix;

    ExcelType(String suffix) {
        this.suffix = suffix;
    }

    public static ExcelType forSuffix(String suffix) throws UnsupportedFileException {
        for (ExcelType type : values()) {
            if (type.suffix.equals(suffix)) {
                return type;
            }
        }
        throw new UnsupportedFileException("We need " + Arrays.toString(ExcelType.values()) + ",but you provided a \"" + suffix + "\" file");
    }
}

