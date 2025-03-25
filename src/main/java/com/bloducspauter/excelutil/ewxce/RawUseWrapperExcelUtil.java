package com.bloducspauter.excelutil.ewxce;

import com.bloducspauter.excelutil.ewxce.read.RowDataReader;
import com.bloducspauter.excelutil.ewxce.read.SheetReader;
import com.bloducspauter.excelutil.ewxce.read.TitleReader;
import com.bloducspauter.excelutil.ewxce.wrapper.ReadWrapper;
import com.bloducspauter.excelutil.ewxce.wrapper.WriteWrapper;
import com.bloducspauter.excelutil.ewxce.write.EncryptExcel;
import com.bloducspauter.excelutil.origin.ExcelUtil;
import com.bloducspauter.excelutil.origin.service.ExcelService;
import com.bloducspauter.excelutil.origin.tool.ExcelTool;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class RawUseWrapperExcelUtil implements ExcelService {
    final ExcelTool excelTool = new ExcelTool();
    SheetReader sheetReader;
    @Setter
    int maxRow;
    @Setter
    int maxColumn;
    @Setter
    int startRow;
    @Setter
    int startColumn;
    @Setter
    int endRow;
    @Setter
    int endColumn;
    Map<Integer, String> titleMap;
    ReadWrapper wrapper;

    public RawUseWrapperExcelUtil(ReadWrapper readWrapper) throws Exception {
        wrapper = readWrapper;
    }


    public RawUseWrapperExcelUtil() {
        wrapper = ReadWrapper.builder().sheetIndex(0).build();
    }

    protected void init() throws Exception {
        if (wrapper == null) {
            throw new NullPointerException("ReadWrapper is null, please check your ReadWrapper");
        }
        String path = wrapper.getPath();
        if (!excelTool.checkFileExists(path)) {
            throw new FileNotFoundException("File not found: " + path);
        }
        if (excelTool.checkIsDirectory(path)) {
            throw new IllegalArgumentException("Path is a directory: " + path);
        }
        if (wrapper.getDateformat() == null) {
            wrapper.setDateformat("yyyy-MM-dd");
        }
        String suffix = excelTool.getSuffix(path);
        excelTool.checkSuffix(suffix);
        sheetReader = new SheetReader();
        sheetReader.getWorkbookReader().getWorkbook(wrapper);
        sheetReader.getSheet(wrapper);
        maxRow = sheetReader.getMaxRow(wrapper.getSheetIndex());
        maxColumn = sheetReader.getMaxColumn(wrapper.getTitleLine());
        updateReadRange();
    }

    protected void updateReadRange() throws IOException {
        endRow = wrapper.getEndRow() == 0 ? maxRow : wrapper.getEndRow();
        endColumn = wrapper.getEndColumn() == 0 ? maxColumn : wrapper.getEndColumn();
        excelTool.checkRowCol(startRow, startColumn, endRow, endColumn, maxRow, maxColumn);
        startRow = wrapper.getStartRow();
        startColumn = wrapper.getStartColumn();
        endRow = wrapper.getEndRow() == 0 ? maxRow : endRow;
        titleMap = TitleReader.readTitle(sheetReader.getSheet(wrapper.getSheetIndex()),
                wrapper.getSheetIndex(), startColumn, endColumn, excelTool);
    }

    private List<Map<String, Object>> readMap() throws Exception {
        init();
        List<Map<String, Object>> objects = new ArrayList<>();
        String dateformat = wrapper.getDateformat();
        for (int row = startRow; row < endRow; row++) {
            if (row == wrapper.getTitleLine()) {
                continue;
            }
            try {
                Map<String, Object> map = RowDataReader.read(sheetReader.getSheet(wrapper.getSheetIndex()),
                        titleMap, row, startColumn, maxColumn, dateformat);
                objects.add(map);
            } catch (Exception e) {
                System.out.println("Loading info failed in line " + row + ": " + e.getMessage());
            }
        }
        startRow = 0;
        endRow = maxRow;
        return objects;
    }

    /**
     * 读取数据,返回的键值是单元格标题值
     */
    public List<Map<String, Object>> readToSimpleMap() throws Exception {
        return readMap();
    }

    public void close() {
        try {
            sheetReader.getWorkbookReader().getWorkbook().close();
        } catch (IOException e) {
            System.out.println("Close workbook error");
        }
    }

    /**
     * 读一列数据
     *
     * @param index 列数
     */
    public List<Object> readLine(int index) throws Exception {
        startColumn = index;
        endColumn = index + 1;
        List<Map<String, Object>> result = readToSimpleMap();
        List<Object> list = new ArrayList<>();
        result.forEach(map -> list.add(map.get(titleMap.get(index))));
        return list;
    }


    private void write(WriteWrapper wrapper, List<Map<String, Object>> list) throws Exception {
        new ExcelUtil().output(list, wrapper.getPath());
        if (wrapper.getPassword() != null) {
            EncryptExcel.encryptExcl(wrapper.getPath(), wrapper.getPassword());
        }
    }

    @Override
    public void output(String sheetName, List<Map<String, Object>> list, File file) throws Exception {
        WriteWrapper wrapper = WriteWrapper.builder().path(file.getAbsolutePath()).sheetName(sheetName).build();
        write(wrapper, list);
    }

    @Override
    public void output(String sheetName, Object[][] obj, String[] title, File file) throws Exception {
        List<Map<String, Object>> list = excelTool.conformity(obj, title);
        output(sheetName, list, file);
    }

    @Override
    public List<Map<String, Object>> readToList(String path) throws Exception {
        wrapper.setPath(path);
        updateReadRange();
        return readToSimpleMap();
    }

    @Override
    public Object[][] readToArray(String path) throws Exception {
        List<Map<String, Object>> mapList = readToList(path);
        return excelTool.conformity(mapList, titleMap);
    }

    @SneakyThrows
    @Override
    public void setEndWithRow(int endRow) {
        wrapper.setEndRow(endRow);
        updateReadRange();
    }

    @SneakyThrows
    @Override
    public void setEndWithColumn(int endColumn) {
        wrapper.setEndColumn(endColumn);
        updateReadRange();
    }

    @SneakyThrows
    @Override
    public void readSheetAt(int index) {
        if (wrapper.getSheetIndex() != index) {
            wrapper.setSheetIndex(index);
            init();
        }
    }
}
