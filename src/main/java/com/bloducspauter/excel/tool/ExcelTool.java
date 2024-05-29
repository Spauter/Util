package com.bloducspauter.excel.tool;


import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.bloducspauter.enums.ExcelType;

/**
 * 表格文档检查工具
 *
 * @author Bloduc Spauter
 * @version 1.16
 */
public class ExcelTool extends ExcelValidationTool {
    private final List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public void checkSuffix(File file) throws UnsupportedFileException {
        if (checkIsDirectory(file)) {
            throw new UnsupportedFileException("Can not read a directory:"+ file.getAbsolutePath());
        }
        String suffix=getSuffix(file.getName());
        if (Objects.requireNonNull(ExcelType.forSuffix(suffix)) == ExcelType.Unknown) {
            throw new UnsupportedFileException("Unsupported file:" + file.getAbsolutePath());
        }
    }
}
