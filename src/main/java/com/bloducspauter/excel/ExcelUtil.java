package com.bloducspauter.excel;

import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.origin.service.ValidationTool;
import lombok.Setter;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bloducspauter.origin.Util.*;

/**
 * 表格工具
 * <p>
 *
 * @author Bloduc Spauter
 * @version   1.0
 */
public class ExcelUtil {
    private String path;
    /**
     * 标题行
     */
    protected final Map<Integer, String> titles = new HashMap<>();
    /**
     * 读取到的List集合
     */
    private List<Map<String, Object>> list = new ArrayList<>();
    static ValidationTool excelTool = new ExcelTool();
    /**
     * 日期格式，默认yyyy-MM-dd
     * -- SETTER --
     * 设置日期格式仅在读取时有效,如果在输出时需要修改日期格式,见子类{@link BsExcelUtil}
     *
     * @see BsExcelUtil
     */
    @Setter
    protected String dateformat = "yyyy-MM-dd";
    /**
     * 标题行，默认0行
     * -- SETTER --
     * 设置某一列作为标题,默认为0(第一行)
     *
     * @param titleLine 被设置为标题的哪一行
     */
    @Setter
    protected int titleLine = 0;
    /**
     * 读取到的二维数组
     */
    private Object[][] arrayData;
    /**
     * 起始行
     * -- SETTER --
     * 设置起始读取的行数
     *
     * @param startRow  起始行
     */
    @Setter
    protected int startRow = 0;
    /**
     * 截止行
     * -- SETTER --
     * 设置截至读取的列数
     *
     * @param endWithRow 截止列
     */
    @Setter
    protected int endWithRow = -1;
    /**
     * 起始列
     * -- SETTER --
     * 设置起始读取的列数
     *
     * @param startCol 起始列
     */
    @Setter
    private int startCol = 0;
    /**
     * 截止列
     * -- SETTER --
     * 设置截至读取的列数
     *
     * @param endWithCol 截止列
     */
    @Setter
    private int endWithCol = -1;
    /**
     * 表格的最大行
     */
    protected int maxRow = 0;
    /**
     * 表格的最大列
     */
    protected int maxCol = 0;
    /**
     * 需要读取的Sheet
     */
    private int readSheetNumber = 0;
    /**
     * 表格的{@code Sheet}
     */
    protected Sheet sheet;
    /**
     * 表格密码,未实现
     */
    protected String password;

    /**
     * 无参构造
     */
    public ExcelUtil() {

    }

    /**
     * 提供文件后，会读取文件行数和列数信息
     * <p>
     *
     * @param file 文件
     */
    public ExcelUtil(File file) throws IOException {
        new ExcelUtil(file.getAbsolutePath());
    }

    /**
     * 提供文件路径后,会读取文件行数和列数信息
     * <p>
     *
     * @param path 文件路径
     */
    public ExcelUtil(String path) throws IOException {
        this.path = path;
        try {
            sheet = getSheet(path);
            getMaxRowsAndCols(sheet);
            setDefaultEndWithRowsAndCols(-1, -1);
        } catch (Exception e) {
            System.out.println(("Reading file failed"));
            throw e;
        }
    }

    /**
     * 获取最大行数和列数
     * <p>
     *
     * @param sheet {code sheet}
     */
    private void getMaxRowsAndCols(Sheet sheet) {
        maxRow = sheet.getLastRowNum();
        maxCol = sheet.getRow(titleLine).getLastCellNum();
    }

    /**
     * 设置读取范围
     * <p>
     *
     * @param endWithRow 截止行
     * @param endWithCol 截止列
     */
    private void setDefaultEndWithRowsAndCols(int endWithRow, int endWithCol) {
        this.endWithRow = this.endWithRow == -1 ? maxRow : endWithRow;
        this.endWithCol = this.endWithCol == -1 ? maxCol : this.endWithCol;
    }


    /**
     * 获取标题
     * <p>
     *
     * @param sheet {code sheet}
     */
    protected void readTitle(Sheet sheet) {
        excelTool.checkTitleLine(titleLine, maxRow);
        for (int col = startCol; col < endWithCol; col++) {
            Cell title = sheet.getRow(titleLine).getCell(col);
            if (title == null) {
                System.out.println(("Read title field"));
                String column = excelTool.convertToExcelColumn(col);
                throw new NullPointerException("Empty column in row " + (titleLine + 1) + ",column " + column);
            }
            String titleInfo = String.valueOf(title);
            titles.put(col, titleInfo);
        }
    }

    /**
     * 读取表格文件存入List集合中
     * <p>
     *
     * @param file 文件路径
     * @return {@code List<Map<String, Object>>}
     */
    private List<Map<String, Object>> readImpl(String file) throws IOException {
        try {
            //如果是有参构造获取的Sheet就不要再获取一遍了
            sheet = sheet == null ? getSheet(file) : sheet;
            getMaxRowsAndCols(sheet);
            if (endWithRow == -1 || endWithCol == -1) {
                setDefaultEndWithRowsAndCols(endWithRow, endWithCol);
            }
            excelTool.checkRowCol(startRow, startCol, endWithRow, endWithCol, maxRow, maxCol);
            readTitle(sheet);
        } catch (NotOLE2FileException e) {
            System.out.println("This error may occur if you are using HSSFWorkbook to read CSV files, as HSSFWorkbook is primarily used to handle Excel file formats based on OLE2 (Object Linking and Embedding), Instead of a plain text CSV file.\n" +
                    "You should use Apache Commons CSV or direct Java file read operations to read CSV files, which is much simpler and more efficient. Here is sample code for reading a CSV file using Apache Commons CSV:");
            System.out.println(("Reading file failed"));
            throw e;
        } catch (Exception e) {
            System.out.println(("Reading file failed"));
            throw e;
        }
        System.out.println(("File check passed,Starting read"));
        for (int row = startRow; row < endWithRow; row++) {
            //如果是标题行则跳过
            if (row == titleLine) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            for (int rol = startCol; rol < endWithCol; rol++) {
                Cell info = sheet.getRow(row).getCell(rol);
                Object o = getCellValue(info);
                String title = titles.get(rol);
                map.put(title, o);
            }
            list.add(map);
        }
        for (Map<String, Object> m : list) {
            System.out.println(m);
        }
        arrayData = excelTool.conformity(list, titles);
        System.out.println(("Reading successfully"));
        return list;
    }

    /**
     * 根据文件后缀名确定workbook是XSSFWorkbook还是HSSFWorkbook
     * <p>
     *
     * @param file 文件
     * @return Sheet
     * @throws IOException IO流异常
     */
    protected Sheet getSheet(String file) throws IOException {
        excelTool.checkSuffix(file);
        excelTool.checkFile(new File(file));
        Workbook workbook;
        Sheet sheet;
        if (file.endsWith(SUFFIX_1)) {
//        判断文件类型
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        }
        // 创建工作簿对象
        // 获取工作簿下sheet的个数
        int totalSheets = workbook.getNumberOfSheets();
        if (readSheetNumber > totalSheets) {
            System.out.println(("Invalid sheet number"));
            throw new IllegalArgumentException("Sheet index (" + readSheetNumber + ") is out of range " + totalSheets);
        }
        sheet = workbook.getSheetAt(readSheetNumber);
        return sheet;
    }

    /**
     * 处理单元格数据，转化为合适地表达值
     * <p>
     *
     * @param cell 单元格
     * @return {@code String}
     */
    protected String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                // 处理日期格式、时间格式
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf;
                    if (DateUtil.isADateFormat(-1, cell.getCellStyle().getDataFormat() + "")) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    } else {
                        sdf = new SimpleDateFormat(dateformat);
                    }
                    cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else if ("".equals(cell.getCellStyle().getDataFormatString())) {
                    //转换成整型
                    DecimalFormat df = new DecimalFormat("#");
                    cellValue = df.format(cell.getNumericCellValue());
                } else {
                    cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case FORMULA:
                cellValue = cell.getCellFormula();
                break;
            case BLANK:
                cellValue = "";
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
        }
        return cellValue;
    }


    private String[] getTitleImpl(File file) throws IOException {
        String[] title;
        if (file == null && list != null) {
            if (list.isEmpty()) {
                System.out.println(("WARRING: Failed to get title"));
            }
            title = new String[titles.size()];
            for (int i = 0; i < titles.size(); i++) {
                title[i] = titles.get(i);
            }
            return title;
        }
        assert file != null;
        list = readImpl(file.getAbsolutePath());
        if (list == null || list.isEmpty()) {
            System.out.println(("WARRING: Failed to get title"));
        }
        title = new String[titles.size()];
        for (int i = 0; i < titles.size(); i++) {
            title[i] = titles.get(i);
        }
        return title;
    }

    private void outPutImpl(String sheetName, Object[][] obj, String[] title, File file) throws IOException {
        excelTool.checkSuffix(file);
        if (file.exists()) {
            throw new IOException("This file is already exists");
        }
        if (obj == null || obj.length == 0 || title == null || title.length == 0) {
            throw new NullPointerException("Unable to invoke an empty data. Did you forgot to read file or clean it?");
        }
        Workbook wb;
//        判断文件类型
        if (file.getName().endsWith(SUFFIX_2)) {
            wb = new HSSFWorkbook();
        } else {
            wb = new XSSFWorkbook();
        }
        Sheet sheet = wb.createSheet(sheetName);
        Row row = sheet.createRow(0);
        Cell cell;
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        for (int i = 0; i < obj.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < obj[i].length; j++) {
                if (obj[i][j] == null) {
                    row.createCell(j).setCellValue("");
                } else {
                    row.createCell(j).setCellValue(obj[i][j].toString());
                }
            }
        }
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Invalid path:" + file.getAbsolutePath());
        }
        try {
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            throw new IOException("File export failure.");
        }
        System.out.println(("The file is successfully exported and saved to:\t" + file.getAbsolutePath()));
    }

    /**
     * 返回Map集合里键值，用于获取标题
     *
     * @param list {@code List集合}
     * @return {@code String[]}
     */

    public String[] getTitle(List<Map<String, Object>> list) {
        String[] title = new String[list.get(0).size()];
        int i = 0;
        for (Map<String, Object> map : list) {
            for (String key : map.keySet()) {
                if (i < title.length) {
                    title[i] = key;
                    titles.put(i, key);
                    i++;
                } else {
                    break;
                }
            }
        }
        return title;
    }


    /**
     * 以键值对的方式返回标题
     *
     * @return {@code  Map<Integer, String>}
     */
    public Map<Integer, String> titleMap() {
        return titles;
    }

    /**
     * 读取文件并把结果保存到List集合中
     *
     * @param path 文件路径
     * @return List
     * @throws IOException IO流异常
     */
    public List<Map<String, Object>> readToList(String path) throws IOException {
        return readImpl(path);
    }

    /**
     * 读取文件并把结果保存到List集合中
     *
     * @param file 文件
     * @return List
     * @throws IOException IO流异常
     */
    public List<Map<String, Object>> readToList(File file) throws IOException {
        return readImpl(file.getAbsolutePath());
    }

    /**
     * 提供文件后直接读取,不需要额外输入文件路径
     *
     * @return List
     * @throws IOException IO流异常
     */
    public List<Map<String, Object>> readToList() throws IOException {
        return readImpl(this.path);
    }

    /**
     * 读取文件并把结果保存到List集合中
     *
     * @param file 文件
     * @return List
     * @throws IOException IO流异常
     */
    public Object[][] readToArray(File file) throws IOException {
        if (arrayData == null) {
            list = readImpl(file.getAbsolutePath());
            arrayData = excelTool.conformity(list, titles);
        }
        return arrayData;
    }

    /**
     * 读取文件将结果存入二维数组中，在提供有参构造后可使用
     *
     * @return Object[][]
     * @throws IOException IO流异常
     */
    public Object[][] readToArray() throws IOException {
        return readToArray(path);
    }

    /**
     * 读取文件将结果存入二维数组中
     *
     * @param path 文件路径
     * @return Object[][]
     * @throws IOException IO流异常
     */
    public Object[][] readToArray(String path) throws IOException {
        File file = excelTool.conformity(path);
        return readToArray(file);
    }

    /**
     * 返回一个包含需要读取文件的表头的数组
     *
     * @param file 文件
     * @return {@code String[]}
     * @throws IOException IO异常
     */
    public String[] getTitle(File file) throws IOException {
        return getTitleImpl(file);
    }

    /**
     * 返回一个包含需要读取文件路径的表头的数组
     *
     * @param path 文件路径
     * @return {@code String[]}
     * @throws IOException IO异常
     */
    public String[] getTitle(String path) throws IOException {
        File file = excelTool.conformity(path);
        return getTitleImpl(file);
    }

    /**
     * 返回一个包含当前读取表格的表头的数组
     *
     * @return {@code String[]}
     * @throws IOException IO异常
     */
    public String[] getTitle() throws IOException {
        return getTitleImpl(null);
    }

    /**
     * 将存储结果输出为表格
     *
     * @param sheetName 自定义Sheet名字
     * @param obj       二维数组 {@code Object[][]}
     * @param title     标题{@code String[]}
     * @param file      文件
     * @throws IOException IO流异常
     */
    public void outPut(String sheetName, Object[][] obj, String[] title, File file) throws IOException {
        outPutImpl(sheetName, obj, title, file);
    }

    /**
     * 将存储结果输出为表格
     *
     * @param sheetName 自定义Sheet名字
     * @param obj       二维数组 {@code Object[][]}
     * @param title     标题{@code String[]}
     * @param path      文件路径
     * @throws IOException IO流异常
     */
    public void outPut(String sheetName, Object[][] obj, String[] title, String path) throws IOException {
        File file = excelTool.conformity(path);
        outPutImpl(sheetName, obj, title, file);
    }


    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}
     *
     * @param sheetName 自定义Sheet名字
     * @param list      List集合
     * @param path      文件路径
     * @throws IOException IO流异常
     */
    public void outPut(String sheetName, List<Map<String, Object>> list, String path) throws IOException {
        File file = excelTool.conformity(path);
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    /**
     * 将存储结果输出为表格，不需要额外的{@code String[] title}
     *
     * @param sheetName 自定义Sheet名字
     * @param list      List集合
     * @param file      文件
     * @throws IOException IO流异常
     */
    public void outPut(String sheetName, List<Map<String, Object>> list, File file) throws IOException {
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    /**
     * 将结果输出保持文件中
     *
     * @param list List集合
     * @param path 文件路径
     * @throws IOException Io流异常
     */
    public void outPut(List<Map<String, Object>> list, String path) throws IOException {
        File file = excelTool.conformity(path);
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    /**
     * 将结果输出保持文件中
     *
     * @param list List集合
     * @param file 文件
     * @throws IOException Io流异常
     */
    public void outPut(List<Map<String, Object>> list, File file) throws IOException {
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    /**
     * 将结果输出保持文件中
     *
     * @param obj   二维数组
     * @param title 标题
     * @param path  文件路径
     * @throws IOException Io流异常
     */
    public void outPut(Object[][] obj, String[] title, String path) throws IOException {
        File file = excelTool.conformity(path);
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    /**
     * 将结果输出保持文件中
     *
     * @param obj   二维数组
     * @param title 标题
     * @param file  文件
     * @throws IOException Io流异常
     */
    public void outPut(Object[][] obj, String[] title, File file) throws IOException {
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    /**
     * 将结果输出保持文件中，在使用有参构造后可以使用此方法
     *
     * @param path 文件路径
     * @throws IOException          IO异常
     * @throws NullPointerException 如果书库为空可能抛出
     */
    public void outPut(String path) throws IOException {
        outPut(new File(path));
    }

    /**
     * 将结果输出保持文件中，在使用有参构造后可以使用此方法
     *
     * @param file 文件
     * @throws IOException          IO异常
     * @throws NullPointerException 如果书库为空可能抛出
     */
    public void outPut(File file) throws IOException {
        String[] title = getTitle();
        outPutImpl(SHEET_NAME, arrayData, title, file);
    }


    public int getMaxRows() {
        return maxRow;
    }


    public int getMaxCols() {
        return maxCol;
    }


    /**
     * 设置被读取的Sheet;
     *
     * @param sheetNumber Sheet
     */
    public void readSheetAt(int sheetNumber) {
        readSheetNumber = sheetNumber;
    }

    /**
     * 清除数据
     */
    public void clearAll() {
        list.clear();
        arrayData = null;
        titles.clear();
    }

    /**
     * 设置密码，未实现
     * param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
        Biff8EncryptionKey.setCurrentUserPassword(password);
    }
}