package com.bloducspauter.excel;


import com.bloducspauter.excel.read.CellReader;
import com.bloducspauter.excel.read.WorkBookReader;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import com.bloducspauter.origin.wrapper.ReadWrapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 */
public class WrapperExcelUtil<T>  {
    private final TableDefinition tableDefinition;
    private ExcelTool excelTool;
    private Workbook workbook;
    private Sheet sheet;
    private int maxRow;
    private int maxColumn;
    private int startRow;
    private int startColumn;
    private int endRow;
    private int endColumn;
    /**
     * 初始化实体类,使用{@link MyAnnotationConfigApplicationContext#getTableDefinition(Class)}
     * <p>
     * 将相关结果存入{@link TableDefinition}
     *
     * @param entity 实体类
     */
    public WrapperExcelUtil(Class<?> entity,ReadWrapper readWrapper) throws Exception{
        tableDefinition = MyAnnotationConfigApplicationContext.getTableDefinition(entity);
        init(readWrapper);
    }

    private void init(ReadWrapper readWrapper) throws GeneralSecurityException, IOException {
        WorkBookReader workBookReader=new WorkBookReader();
        workbook=workBookReader.getWorkbook(readWrapper);
        sheet= workBookReader.getSheet(readWrapper.getReadSheetAt());
        maxRow=workBookReader.getMaxRow();
        maxColumn=workBookReader.getMaxColumn(readWrapper.getTitleLine());
        endRow=readWrapper.getEndRow();
        excelTool.checkRowCol(startRow,startColumn,endRow,endColumn,maxRow,maxColumn);
    }

    private List<T> read(ReadWrapper readWrapper) throws GeneralSecurityException, IOException {
        String path=readWrapper.getPath();
        excelTool.checkIsDirectory(path);
        excelTool.checkSuffix(path);
        int startRow=readWrapper.getStartRow();
        int startColumn=readWrapper.getStartColumn();
        endRow = readWrapper.getEndRow() == -1 ? maxRow : endRow;
        for (int row = startRow; row < endRow; row++) {
            if (row == readWrapper.getTitleLine()) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            for (int column = startColumn; column < endColumn; column++) {
                String dateformat = readWrapper.getDateformat();
                Object cellValue= CellReader.getCellValue(sheet,row,column,dateformat);

            }
        }
        return null;
    }
}
