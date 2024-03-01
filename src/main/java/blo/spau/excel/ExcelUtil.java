package blo.spau.excel;

import blo.spau.excel.bean.Basic;
import blo.spau.excel.read.Read;
import blo.spau.tool.ToolImpl;
import blo.spau.excel.output.Output;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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


public class ExcelUtil implements Read, Output {

    private final Map<Integer, String> titles = new HashMap<>();
    private List<Map<String, Object>> list = new ArrayList<>();
    static ToolImpl fileValidation = new ToolImpl();
    Basic basic = new Basic();
    private int maxRow = 0;
    private int maxCol = 0;


    private List<Map<String, Object>> readImpl(String file) throws IOException {
        Sheet sheet = getSheet(file);
        // 获取最后一行的num，即总行数。此处从0开始
        this.maxRow = sheet.getLastRowNum();
        for (int row = 1; row <= maxRow; row++) {
            Map<String, Object> map = new HashMap<>();
            this.maxCol = sheet.getRow(row).getLastCellNum();
            for (int rol = 0; rol < maxCol; rol++) {
                Cell title = sheet.getRow(0).getCell(rol);
                String titleInfo = String.valueOf(title);
                if (row == 1) {
                    this.titles.put(rol, titleInfo);
                }
                Cell info = sheet.getRow(row).getCell(rol);
                map.put(titleInfo, getCellValue(info));
            }
            list.add(map);
        }
        for (Map<String, Object> m : list) {
            System.out.println(m);
        }

        return list;
    }

    private static Sheet getSheet(String file) throws IOException {
        fileValidation.Ckeck_suffix(file);
        String suffix = file.split("\\.")[1];
        Workbook workbook;
        Sheet sheet;
        if (suffix.equals(suffix1)) {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        }
        // 创建工作簿对象
        // 获取工作簿下sheet的个数
        sheet = workbook.getSheetAt(0);
        return sheet;
    }

    /**
     * 处理表格里面的数据
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { // 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (DateUtil.isADateFormat(-1, cell.getCellStyle().getDataFormat() + "")) {
                        sdf = new SimpleDateFormat(basic.getDateformat());
                    }
                    if (sdf != null) {
                        cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                    }
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
                String infos = fileValidation.PrintInfo("WARRING: Failed to get title", 31, 0);
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
            String infos = fileValidation.PrintInfo("WARRING: Failed to get title", 31, 0);
            System.out.println(infos);
        }
        title = new String[titles.size()];
        for (int i = 0; i < titles.size(); i++) {
            title[i] = titles.get(i);
        }
        return title;
    }

    private void outPutImpl(String sheetName, Object[][] obj, String[] title, File file) throws IOException {
        fileValidation.Ckeck_suffix(file);
        fileValidation.conformity(obj, title);
        Workbook wb;
        if (file.getName().endsWith(suffix2)) {
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
        System.out.println("The file is successfully exported and saved to:" + file.getAbsolutePath());
    }

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
        list = readImpl(file.getAbsolutePath());
        return fileValidation.conformity(list, titles);
    }

    @Override
    public Object[][] readToArray(String Path) throws IOException {
        File file = fileValidation.conformity(Path);
        return readToArray(file);
    }


    @Override
    public String[] getTitle(File file) throws IOException {
        return getTitleImpl(file);
    }

    @Override
    public String[] getTitle(String Path) throws IOException {
        File file = fileValidation.conformity(Path);
        return getTitleImpl(file);
    }

    @Override
    public String[] getTitle() throws IOException {
        return getTitleImpl(null);
    }

    @Override
    public int getMaxRows() {
        return maxRow;
    }

    @Override
    public int getMaxCols() {
        return maxCol;
    }


    @Override
    public void outPut(String sheetName, Object[][] obj, String[] title, File file) throws IOException {
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(String sheetName, Object[][] obj, String[] title, String Path) throws IOException {
        File file = fileValidation.conformity(Path);
        outPutImpl(sheetName, obj, title, file);
    }


    @Override
    public void outPut(String sheetName, List<Map<String, Object>> list, String Path) throws IOException {
        File file = fileValidation.conformity(Path);
        String[] title = getTitle(list);
        Object[][] obj = fileValidation.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(String sheetName, List<Map<String, Object>> list, File file) throws IOException {
        String[] title = getTitle(list);
        Object[][] obj = fileValidation.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(List<Map<String, Object>> list, String Path) throws IOException {
        File file = fileValidation.conformity(Path);
        String[] title = getTitle(list);
        Object[][] obj = fileValidation.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(List<Map<String, Object>> list, File file) throws IOException {
        String[] title = getTitle(list);
        Object[][] obj = fileValidation.conformity(list, titles);
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(Object[][] obj, String[] title, String Path) throws IOException {
        File file = fileValidation.conformity(Path);
        outPutImpl(sheetName, obj, title, file);
    }

    @Override
    public void outPut(Object[][] obj, String[] title, File file) throws IOException {
        outPutImpl(sheetName, obj, title, file);

    }
}
