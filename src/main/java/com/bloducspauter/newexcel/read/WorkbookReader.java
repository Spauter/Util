package com.bloducspauter.newexcel.read;

import com.bloducspauter.excelutil.enums.ExcelType;
import com.bloducspauter.excelutil.origin.tool.ExcelTool;
import com.bloducspauter.excelutil.base.exceptions.UnsupportedFileException;
import com.bloducspauter.newexcel.wrapper.ReadWrapper;
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
     * @throws IOException 文件读取异常
     * @throws UnsupportedFileException 文件类型不支持
     */
    public Workbook getWorkbook(String path, String password) throws GeneralSecurityException, IOException {
        String suffix = excelTool.getSuffix(path);
        InputStream in;
        in = DecryptExcel.decrypt(path, password);
        switch (ExcelType.forSuffix(suffix)) {
            // 2003 版本以下的 Excel 文件
            case XLS:
                workbook = new HSSFWorkbook(in);
                break;
            // 2007 版本以上的 Excel 文件
            case XLSX:
            case XLSM:
                workbook = new XSSFWorkbook(in);
                break;
            // 其他文件类型
            case CSV:
                String err = "This CSV file is a text file. Please use TextUtil(" + path + ")";
                throw new UnsupportedFileException(err);
            default:
                throw new UnsupportedFileException("Unsupported file type");
        }
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Input stream close error");
        }
        return workbook;
    }
}
