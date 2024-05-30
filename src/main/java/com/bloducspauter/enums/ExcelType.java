package com.bloducspauter.enums;

import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import org.apache.poi.ss.usermodel.CellType;

import java.util.Arrays;

/**
 * 常见excel表格类型
 *
 * @author Bloduc Spauter
 */
public enum ExcelType {
    XLS("xls"),
    XLSX("xlsx"),
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

