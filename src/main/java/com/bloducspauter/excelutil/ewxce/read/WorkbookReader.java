package com.bloducspauter.excelutil.ewxce.read;

import com.bloducspauter.excelutil.enums.ExcelType;
import com.bloducspauter.excelutil.origin.tool.ExcelTool;
import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;
import com.bloducspauter.excelutil.ewxce.wrapper.ReadWrapper;
import lombok.NonNull;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * @author Bloduc Spauter
 * @version 1.19
 */
public class WorkbookReader {
    @Setter
    private Workbook workbook;
    ExcelTool excelTool = new ExcelTool();

    public Workbook getWorkbook() {
        if (workbook == null) {
            throw new NullPointerException();
        }
        return workbook;
    }

    /**
     * 获取工作簿
     */
    public Workbook getWorkbook(@NonNull ReadWrapper readWrapper) throws IOException, GeneralSecurityException {
        return getWorkbook(readWrapper.getPath(), readWrapper.getPassword());
    }

    /**
     * 获取工作簿
     *
     * @throws GeneralSecurityException 加密异常
     * @throws IOException              文件读取异常
     * @throws UnsupportedFileException 文件类型不支持
     */
    public Workbook getWorkbook(String path, String password) throws GeneralSecurityException, IOException {
        String suffix = excelTool.getSuffix(path);
        InputStream in;
        in = DecryptExcel.decrypt(path, password);
        return switch (ExcelType.forSuffix(suffix)) {
            case XLS -> new HSSFWorkbook(in);
            case XLSX, XLSM -> new XSSFWorkbook(in);
            case CSV -> throw new UnsupportedFileException( "This CSV file is a text file. Please use TextUtil(" + path + ")");
        };
    }
}
