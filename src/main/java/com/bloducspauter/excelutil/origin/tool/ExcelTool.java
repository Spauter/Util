package com.bloducspauter.excelutil.origin.tool;


import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;
import java.io.File;
import com.bloducspauter.excelutil.enums.ExcelType;

/**
 * 表格文档检查工具
 *
 * @author Bloduc Spauter
 * @since  1.16
 */
public class ExcelTool extends ExcelValidationTool {

    @Override
    public void checkSuffix(File file) throws UnsupportedFileException {
        if (checkIsDirectory(file)) {
            throw new UnsupportedFileException("Can not read a directory:"+ file.getAbsolutePath());
        }
        String suffix=getSuffix(file.getName());
        ExcelType.forSuffix(suffix);
    }

}
