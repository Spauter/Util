package com.bloducspauter.excel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.excel.read.RowDataReader;
import com.bloducspauter.excel.read.TitleReader;
import com.bloducspauter.excel.read.WorkBookReader;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import com.bloducspauter.origin.wrapper.ReadWrapper;
import com.bloducspauter.origin.wrapper.WriteWrapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Bloduc Spauter
 */
public class WrapperExcelUtil<T> {
    private final TableDefinition tableDefinition;
    private final ExcelTool excelTool = new ExcelTool();
    private Workbook workbook;
    private Sheet sheet;
    private int maxRow;
    private int maxColumn;
    private int startRow;
    private int startColumn;
    private int endRow;
    private int endColumn;
    private Map<Integer, String> titleMap;
    private ReadWrapper wrapper;

    /**
     * 初始化实体类,使用{@link MyAnnotationConfigApplicationContext#getTableDefinition(Class)}
     * <p>
     * 将相关结果存入{@link TableDefinition}
     *
     * @param entity 实体类
     */
    public WrapperExcelUtil(Class<?> entity, ReadWrapper readWrapper) throws Exception {
        tableDefinition = MyAnnotationConfigApplicationContext.getTableDefinition(entity);
        init(readWrapper);
    }

    private void init(ReadWrapper readWrapper) throws Exception {
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
        WorkBookReader workBookReader = new WorkBookReader();
        workbook = workBookReader.getWorkbook(readWrapper);
        sheet = workBookReader.getSheet(sheetNum);
        maxRow = workBookReader.getMaxRow();
        maxColumn = workBookReader.getMaxColumn(readWrapper.getTitleLine());
        endRow = readWrapper.getEndRow() == 0 ? maxRow : readWrapper.getEndRow();
        endColumn = readWrapper.getEndColumn() == 0 ? maxColumn : readWrapper.getEndColumn();
        excelTool.checkRowCol(startRow, startColumn, endRow, endColumn, maxRow, maxColumn);
        titleMap = TitleReader.readTitle(sheet, sheetNum, startColumn, endColumn, excelTool);
    }

    @SuppressWarnings("unchecked")
    private List<T> read() throws NoSuchFieldException {
        Class<?> entity = tableDefinition.getClassName();
        List<T> objects = new ArrayList<>();
        int startRow = wrapper.getStartRow();
        int startColumn = wrapper.getStartColumn();
        String dateformat = wrapper.getDateformat();
        endRow = wrapper.getEndRow() == 0 ? maxRow : endRow;
        for (int row = startRow; row < endRow; row++) {
            if (row == wrapper.getTitleLine()) {
                continue;
            }
            Map<String, Object> map = RowDataReader.read(sheet, tableDefinition, titleMap,
                    row, startColumn, maxColumn, dateformat);
            String jsonString = JSON.toJSONString(map);
            Object o = JSONObject.parseObject(jsonString, entity);
            try {
                objects.add((T) o);
            } catch (ClassCastException e) {
                System.out.println("Loading info failed in line " + row);
            }
        }
        return objects;
    }

    public List<T> readData() {
        int munCores = Runtime.getRuntime().availableProcessors();
        List<T> entities = new ArrayList<>();
        //todo 多线程读取
        return entities;
    }

    /**
     * 读取所有数据
     */
    public List<T> readAll() throws Exception {
        return read();
    }

    /**
     * 读取一条
     * @param index 行数
     */
    public T readOne(int index) throws Exception {
        if (index == wrapper.getTitleLine()) {
            throw new IllegalArgumentException("Don't know how to turn title line" + " into class " + tableDefinition.getClassName().getSimpleName());
        }
        return read().get(0);
    }

    public void write(WriteWrapper wrapper, List<T> entities) {
        //todo
    }

}
