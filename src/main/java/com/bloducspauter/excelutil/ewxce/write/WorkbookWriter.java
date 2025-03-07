package com.bloducspauter.excelutil.ewxce.write;

import com.bloducspauter.excelutil.enums.ExcelType;
import com.bloducspauter.excelutil.origin.tool.ExcelTool;
import com.bloducspauter.excelutil.ewxce.wrapper.WriteWrapper;
import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.UUID;

/**
 * @author Bloduc Spauter
 */
public class WorkbookWriter {

    ExcelTool excelTool = new ExcelTool();
    private Workbook workbook;

    public Workbook getWorkbook(WriteWrapper wrapper) {
        String path= wrapper.getPath();
        String suffix = excelTool.getSuffix(path);
        switch (ExcelType.forSuffix(suffix)) {
            case XLS:
                workbook = new XSSFWorkbook();
                return workbook;
            case XLSX:
            case XLSM:
                workbook = new HSSFWorkbook();
                return workbook;
            default:
                throw new UnsupportedFileException("Unsupported file type");
        }
    }

    public Sheet getSheet(WriteWrapper wrapper) {
        if (workbook == null) {
            throw new NullPointerException("Workbook is null, please check your output file");
        }
        String sheetName = wrapper.getSheetName() == null ? "Sheet1" : wrapper.getSheetName();
        ;
        return workbook.createSheet(sheetName);
    }

}
