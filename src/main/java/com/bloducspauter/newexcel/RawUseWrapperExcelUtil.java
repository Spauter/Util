package com.bloducspauter.newexcel;

import com.bloducspauter.excel.ExcelUtil;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.newexcel.read.RowDataReader;
import com.bloducspauter.newexcel.read.TitleReader;
import com.bloducspauter.newexcel.read.WorkbookReader;
import com.bloducspauter.newexcel.wrapper.ReadWrapper;
import com.bloducspauter.origin.init.TableDefinition;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 简单读取工具
 *
 * @author Bloduc Spauter
 * @see ExcelUtil
 * @since 1.19
 */
public class RawUseWrapperExcelUtil {
    TableDefinition tableDefinition = null;
    final ExcelTool excelTool = new ExcelTool();
    Workbook workbook;
    Sheet sheet;
    int maxRow;
    int maxColumn;
    int startRow;
    int startColumn;
    int endRow;
    int endColumn;
    Map<Integer, String> titleMap;
    ReadWrapper wrapper;

    public RawUseWrapperExcelUtil(ReadWrapper readWrapper) throws Exception {
        init(readWrapper);
    }

    void init(ReadWrapper readWrapper) throws Exception {
        String path = readWrapper.getPath();
        if (!excelTool.checkFileExists(path)) {
            throw new FileNotFoundException("File not found: " + path);
        }
        if (excelTool.checkIsDirectory(path)) {
            throw new IllegalArgumentException("Path is a directory: " + path);
        }
        if (readWrapper.getDateformat() == null) {
            readWrapper.setDateformat("yyyy-MM-dd");
        }
        String suffix = excelTool.getSuffix(path);
        excelTool.checkSuffix(suffix);
        wrapper = readWrapper;
        int sheetNum = readWrapper.getReadSheetAt();
        WorkbookReader workBookReader = new WorkbookReader();
        workbook = workBookReader.getWorkbook(readWrapper);
        sheet = workBookReader.getSheet(sheetNum);
        maxRow = workBookReader.getMaxRow();
        maxColumn = workBookReader.getMaxColumn(readWrapper.getTitleLine());
        endRow = readWrapper.getEndRow() == 0 ? maxRow : readWrapper.getEndRow();
        endColumn = readWrapper.getEndColumn() == 0 ? maxColumn : readWrapper.getEndColumn();
        excelTool.checkRowCol(startRow, startColumn, endRow, endColumn, maxRow, maxColumn);
        titleMap = TitleReader.readTitle(sheet, sheetNum, startColumn, endColumn, excelTool);
        startRow = wrapper.getStartRow();
        startColumn = wrapper.getStartColumn();
        endRow = wrapper.getEndRow() == 0 ? maxRow : endRow;
    }

    private List<Map<String, Object>> readMap() throws NoSuchFieldException {
        List<Map<String, Object>> objects = new ArrayList<>();
        String dateformat = wrapper.getDateformat();
        for (int row = startRow; row < endRow; row++) {
            if (row == wrapper.getTitleLine()) {
                continue;
            }
            Map<String, Object> map = RowDataReader.read(sheet, titleMap,
                    row, startColumn, maxColumn, dateformat);
            objects.add(map);
        }
        return objects;
    }

    /**
     * 读取数据,返回的键值是单元格标题值
     */
    public List<Map<String, Object>> readToSimpleMap() throws NoSuchFieldException {
        return readMap();
    }
}
