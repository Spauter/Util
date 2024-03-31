package com.bloducspauter.excel;

import com.bloducspauter.MyTool;
import com.bloducspauter.excel.read.ReadExcel;
import com.bloducspauter.excel.tool.ExcelToolImpl;
import com.bloducspauter.excel.output.OutputExcel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//总工具
public class ExcelUtil implements ReadExcel, OutputExcel {
    //标题
    private final Map<Integer, String> titles = new HashMap<>();//表格的标题,也就是首行
    private List<Map<String, Object>> list = new ArrayList<>();
    static MyTool excelTool = new ExcelToolImpl();
    //日期格式，默认yyyy-MM-dd
    private String dateformat = "yyyy-MM-dd";

    private int titleLine=0;

    private Object[][] arrayData;

    private int maxRow = 0;
    private int maxCol = 0;
    //需要读取的Sheet
    private int readSheetNumber = 0;

    private void readTitle(Sheet sheet) {
        excelTool.check_titleLine(titleLine,maxRow);
        int maxCol=sheet.getRow(titleLine).getLastCellNum();
        for (int col = 0; col < maxCol; col++) {
            Cell title = sheet.getRow(titleLine).getCell(col);
            if (title == null) {
                throw new NullPointerException("Empty column in row "+(titleLine+1)+",column "+col);
            }
            String titleInfo = String.valueOf(title);
            this.titles.put(col, titleInfo);
        }
    }

    private List<Map<String, Object>> readImpl(String file) throws IOException {
        Sheet sheet = getSheet(file);
        // 获取最后一行的num，即总行数。此处从0开始
        this.maxRow = sheet.getLastRowNum();
        readTitle(sheet);
        for (int row = 1; row <= maxRow; row++) {
            Map<String, Object> map = new HashMap<>();
            this.maxCol = sheet.getRow(row).getLastCellNum();
            for (int rol = 0; rol < this.maxCol; rol++) {
                Cell info = sheet.getRow(row).getCell(rol);
                map.put(titles.get(rol), getCellValue(info));
            }
            list.add(map);
        }
        for (Map<String, Object> m : list) {
            System.out.println(m);
        }
        arrayData = excelTool.conformity(list, titles);
        return list;
    }

    //根据文件后缀名确定workbook是XSSFWorkbook还是HSSFWorkbook
    private Sheet getSheet(String file) throws IOException {
        excelTool.Check_suffix(file);
        excelTool.Check_file(new File(file));
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
        if (readSheetNumber > totalSheets - 1) {
            throw new IllegalArgumentException("Sheet index (" + readSheetNumber + ") is out of range " + totalSheets);
        }
        sheet = workbook.getSheetAt(readSheetNumber);
        return sheet;
    }

    /**
     * 处理表格里面的数据
     *
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { // 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (DateUtil.isADateFormat(-1, cell.getCellStyle().getDataFormat() + "")) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    } else {
                        sdf = new SimpleDateFormat(dateformat);
                    }
                    cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                    DecimalFormat df = new DecimalFormat("#");//转换成整型
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
                String infos = excelTool.PrintInfo("WARRING: Failed to get title", 33, 0);
                System.out.println(infos);
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
            String infos = excelTool.PrintInfo("Failed to get title", 31, 0);
            System.out.println(infos);
        }
        title = new String[titles.size()];
        for (int i = 0; i < titles.size(); i++) {
            title[i] = titles.get(i);
        }
        return title;
    }

    private void outPutImpl(String sheetName, Object[][] obj, String[] title, File file) throws IOException {
        excelTool.Check_suffix(file);
        if (file.exists()){
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
        String info="The file is successfully exported and saved to:\t" + file.getAbsolutePath();
        System.out.println(info);
    }

    /**
     * 通过已经获取的List<Map<String,Onject>>集合来获取标题,返回数组
     *
     * @param list
     * @return
     */
    @Override
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

    @Override
    public Map<Integer, String> titleMap() {
        return titles;
    }


    @Override
    public List<Map<String, Object>> readToList(String path) throws IOException {
        return readImpl(path);
    }


    @Override
    public List<Map<String, Object>> readToList(File file) throws IOException {
        return readImpl(file.getAbsolutePath());
    }


    @Override
    public Object[][] readToArray(File file) throws IOException {
        if (arrayData == null) {
            list = readImpl(file.getAbsolutePath());
            arrayData = excelTool.conformity(list, titles);
        }
        return arrayData;
    }


    @Override
    public Object[][] readToArray(String Path) throws IOException {
        File file = excelTool.conformity(Path);
        return readToArray(file);
    }


    @Override
    public String[] getTitle(File file) throws IOException {
        return getTitleImpl(file);
    }

    @Override
    public String[] getTitle(String Path) throws IOException {
        File file = excelTool.conformity(Path);
        return getTitleImpl(file);
    }

    @Override
    public String[] getTitle() throws IOException {
        return getTitleImpl(null);
    }

    @Override
    public void outPut(String sheetName, Object[][] obj, String[] title, File file) throws IOException {
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(String sheetName, Object[][] obj, String[] title, String Path) throws IOException {
        File file = excelTool.conformity(Path);
        outPutImpl(sheetName, obj, title, file);
    }


    @Override
    public void outPut(String sheetName, List<Map<String, Object>> list, String Path) throws IOException {
        File file = excelTool.conformity(Path);
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(String sheetName, List<Map<String, Object>> list, File file) throws IOException {
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(List<Map<String, Object>> list, String Path) throws IOException {
        File file = excelTool.conformity(Path);
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    @Override
    public void outPut(List<Map<String, Object>> list, File file) throws IOException {
        String[] title = getTitle(list);
        Object[][] obj = excelTool.conformity(list, titles);
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    @Override
    public void outPut(Object[][] obj, String[] title, String Path) throws IOException {
        File file = excelTool.conformity(Path);
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    @Override
    public void outPut(Object[][] obj, String[] title, File file) throws IOException {
        outPutImpl(SHEET_NAME, obj, title, file);
    }

    @Override
    public void outPut(String path) throws IOException {
        outPut(new File(path));
    }

    @Override
    public void outPut(File file) throws IOException {
        String[] title = getTitle();
        outPutImpl(SHEET_NAME, arrayData, title, file);
    }


    @Override
    public int getMaxRows() {
        return maxRow;
    }

    @Override
    public int getMaxCols() {
        return maxCol;
    }

    /**
     * 将某一行作为标题,输入从1开始
     * @param titleLine
     */
    @Override
    public void setTitleLine(int titleLine) {
        this.titleLine=titleLine;
    }

    //设置日期格式
    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    @Override
    //设置需要读取的sheet,最小为一
    public void readSheetAt(int sheetNumber) {
        readSheetNumber = sheetNumber;
    }

    @Override
    public void clearAll() {
        list.clear();
        arrayData = null;
        titles.clear();
    }
}
