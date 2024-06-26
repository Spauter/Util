package com.bloducspauter.newexcel.write;

import com.bloducspauter.enums.ExcelType;
import com.bloducspauter.excelutil.origin.tool.ExcelTool;
import com.bloducspauter.newexcel.wrapper.WriteWrapper;
import com.bloducspauter.origin.exceptions.UnsupportedFileException;
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
    private Sheet sheet;

    public File getOutputFile(WriteWrapper wrapper) throws FileAlreadyExistsException {
        String path = wrapper.getPath();
        String fileName = wrapper.getFileName();
        File file = new File(path);
        if (excelTool.checkFileExists(file)) {
            throw new FileAlreadyExistsException("File already exists");
        }
        if (fileName == null && excelTool.checkIsDirectory(file)) {
            path = path + File.separator + UUID.randomUUID() + ".xlsx";
        }
        if (fileName != null && excelTool.checkIsDirectory(file)) {
            String suffix = excelTool.getSuffix(fileName);
            if (suffix.equals(fileName)) {
                fileName = fileName + ".xlsx";
            }
            path = path + File.separator + fileName;
            file = new File(path);
        }
        return file;
    }

    public Workbook getWorkbook(String path) {
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
        sheet = workbook.createSheet(sheetName);
        return sheet;
    }

}
