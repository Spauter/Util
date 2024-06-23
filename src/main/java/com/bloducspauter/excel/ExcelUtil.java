package com.bloducspauter.excel;

import com.bloducspauter.enums.ExcelType;
import com.bloducspauter.excel.service.ExcelService;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.excel.tool.ExcelValidationTool;
import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import com.bloducspauter.text.TextService;
import com.bloducspauter.text.TextUtil;
import lombok.Setter;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 表格工具
 * <p>
 *
 * @author Bloduc Spauter
 * @since  1.0
 */
public class ExcelUtil implements ExcelService {

    private final TextService textService = new TextUtil();
    /**
     * 标题行
     */
    protected final Map<Integer, String> titles = new HashMap<>();

    ExcelValidationTool excelTool = new ExcelTool();

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
     */
    @Setter
    protected int titleLine = 0;

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
    private List<Map<String, Object>> readImpl(String file) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            excelTool.checkSuffix(file);
            if (file.endsWith(ExcelType.CSV.suffix)) {
                System.out.println("Reading the text csv file");
                list = textService.readToList(file);
                String[] title = textService.getTitle();
                for (int i = 0; i < title.length; i++) {
                    titles.put(i, title[i]);
                }
                return list;
            }
            //如果是有参构造获取的Sheet就不要再获取一遍了
            sheet = sheet == null ? getSheet(file) : sheet;
            getMaxRowsAndCols(sheet);
            if (endWithRow == -1 || endWithCol == -1) {
                setDefaultEndWithRowsAndCols(endWithRow, endWithCol);
            }
            excelTool.checkRowCol(startRow, startCol, endWithRow, endWithCol, maxRow, maxCol);
            readTitle(sheet);
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
    protected Sheet getSheet(String file) throws Exception {
        Sheet sheet;
        File file1 = new File(file);
        Workbook workbook = getReadWorkbook(file1);
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

    protected Workbook getReadWorkbook(File file) throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getAbsolutePath() + " not found");
        }
        String suffix = excelTool.getSuffix(file.getName());
        switch (ExcelType.forSuffix(suffix)) {
            case XLSX: {
                return new XSSFWorkbook(new FileInputStream(file));
            }
            case XLS: {
                return new HSSFWorkbook(new FileInputStream(file));
            }
            default:
                throw new UnsupportedFileException("We need " + Arrays.toString(ExcelType.values()) + ",but you provided a " + suffix + "file");
        }
    }

    protected Workbook getOutputWorkbook(File file) {
        String suffix = excelTool.getSuffix(file.getName());
        switch (ExcelType.forSuffix(suffix)) {
            case XLSX: {
                return new XSSFWorkbook();
            }
            case XLS: {
                return new HSSFWorkbook();
            }
            default:
                throw new UnsupportedFileException("We need " + Arrays.toString(ExcelType.values()) + ",but you provided a " + suffix + "file");
        }
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

    private void outputImpl(String sheetName, Object[][] obj, String[] title, File file) throws Exception {
        if (obj == null || obj.length == 0 || title == null || title.length == 0) {
            throw new NullPointerException("Unable to invoke an empty data. Did you forgot to read file or clean it?");
        }
        if (excelTool.checkIsDirectory(file)) {
            String filepath = file.getAbsolutePath() + File.separator + UUID.randomUUID() + ".xlsx";
            file = new File(filepath);
        }
        if (excelTool.checkFileExists(file)) {
            throw new FileAlreadyExistsException("This file is already exists");
        }
        if (excelTool.getSuffix(file.getName()).equals(ExcelType.CSV.suffix)) {
            System.out.println("Writing a text csv file");
            textService.output(obj, title, file);
            System.out.println("Finished to write the file");
            return;
        }
        Workbook wb = getOutputWorkbook(file);
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

    @Override
    public List<Map<String, Object>> readToList(String path) throws Exception {
        return readImpl(path);
    }

    @Override
    public Object[][] readToArray(String path) throws Exception {
        return excelTool.conformity(readImpl(path), titles);
    }

    @Override
    public void output(String sheetName, Object[][] obj, String[] title, File file) throws Exception {
        outputImpl(sheetName, obj, title, file);
    }

    @Override
    public void output(String sheetName, Object[][] obj, String[] title, String path) throws Exception {
        File file = new File(path);
        outputImpl(sheetName, obj, title, file);
    }

    @Override
    public void output(String sheetName, List<Map<String, Object>> list, File file) throws Exception {
        Object[][] obj = excelTool.conformity(list, titles);
        String[] title = new String[titles.size()];
        for (int i = 0; i < titles.size(); i++) {
            title[i] = titles.get(i);
        }
        outputImpl(sheetName, obj, title, file);
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


}