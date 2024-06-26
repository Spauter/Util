package com.bloducspauter.excelutil.ewxce.read;


import com.bloducspauter.excelutil.ewxce.wrapper.ReadWrapper;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Bloduc Spauter
 */
@Setter
public class SheetReader {
    private WorkbookReader workbookReader;

    /**
     * 获取WorkbookReader
     *
     * @return WorkbookReader
     */
    public WorkbookReader getWorkbookReader() {
        if (workbookReader == null) {
            workbookReader = new WorkbookReader();
        }
        return workbookReader;
    }

    public Sheet getSheet(int sheetIndex) throws IOException {
        Workbook workbook = getWorkbookReader().getWorkbook();
        if (sheetIndex < 0 || sheetIndex > workbook.getNumberOfSheets()) {
            workbook.close();
            throw new IllegalArgumentException("The index is out of range:" + sheetIndex + "/" + workbook.getNumberOfSheets());
        }
        return getWorkbookReader().getWorkbook().getSheetAt(sheetIndex);
    }

    /**
     * 获取工作表
     *
     * @throws IllegalArgumentException  工作表索引不存在
     * @throws IndexOutOfBoundsException 工作表索引超出范围
     */
    public Sheet getSheet(ReadWrapper wrapper) throws GeneralSecurityException, IOException {
        return getSheet(wrapper.getSheetIndex());
    }

    /**
     * 获取最大行数
     *
     * @return 最大行数
     */
    public int getMaxRow() {
        Sheet sheet = getWorkbookReader().getWorkbook().getSheetAt(0);
        return sheet.getLastRowNum();
    }

    /**
     * 获取最大列数
     *
     * @param index 行索引
     * @return 最大列数
     */
    public int getMaxColumn(int index) {
        Sheet sheet = getWorkbookReader().getWorkbook().getSheetAt(0);
        return sheet.getRow(index).getLastCellNum();
    }
}
