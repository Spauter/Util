package com.bloducspauter.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.annotation.ExcelField;
import com.bloducspauter.annotation.ExcelTable;
import com.bloducspauter.excel.task.ReadData;
import com.bloducspauter.excel.task.ReadingDataTask;
import com.bloducspauter.excel.tool.ExcelTool;
import com.bloducspauter.origin.exceptions.UnsupportedFileException;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 拓展的输入输出工具
 *
 * @author Bloduc Spauter
 * @see com.bloducspauter.excel.ExcelUtil
 * @since 1.18
 */
public class BsExcelUtil<T> extends ExcelUtil {

    private final TableDefinition entityTableDefinition;

    /**
     * 初始化实体类,使用{@link MyAnnotationConfigApplicationContext#getTableDefinition(Class)}
     * <p>
     * 读取该类的{@link ExcelTable}和{@link ExcelField}
     * <p>
     * 并将相关结果存入{@link TableDefinition}
     *
     * @param entity 实体类
     */
    public BsExcelUtil(Class<?> entity) {
        MyAnnotationConfigApplicationContext myAnnotationConfigApplicationContext = new MyAnnotationConfigApplicationContext();
        entityTableDefinition = myAnnotationConfigApplicationContext.getTableDefinition(entity);
    }

    /**
     * 读物excel表格，采用多线程方式
     * <p>
     * 先通过{@code  Runtime#getRuntime().availableProcessors()}获取CPU线程数
     * </p>
     * <p>
     * 然后将上面创建的FutureTask提交给Executor线程执行,如果任务没有完成。
     * 如果有线程未完成,会使用 {@link  ThreadPoolExecutor#invokeAll(Collection)}  阻塞此处
     * </p>
     *
     * @param filePath 文件路径
     * @see #readAll(String)
     */
    private ReadData<T> getReadDataIml(String filePath) throws IOException, InterruptedException, ExecutionException, NoSuchFieldException, UnsupportedFileException {
        sheet = super.getSheet(filePath);
        maxRow = maxRow == 0 ? sheet.getLastRowNum() : maxRow;
        //
        int munCores = Runtime.getRuntime().availableProcessors();
        List<T> entities = new ArrayList<>();
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(munCores);
        List<ReadingDataTask<T>> readingDataTasks = getReadingDataTasks(munCores, filePath);
        List<Future<ReadData<T>>> futures = threadPoolExecutor.invokeAll(readingDataTasks);
        //任务完成了,停止所有任务;
        threadPoolExecutor.shutdown();
        for (Future<ReadData<T>> future : futures) {
            ReadData<T> readData = future.get();
            entities.addAll(readData.getData());
        }
        ReadData<T> readData = new ReadData<>();
        readData.setData(entities);
        readData.setMaxRol(maxRow);
        return readData;
    }


    private List<ReadingDataTask<T>> getReadingDataTasks(int munCores, String filePath) throws IOException, NoSuchFieldException, UnsupportedFileException {
        int size = maxRow;
        int step = size / munCores;
        int startIndex, endWithIndex;
        List<ReadingDataTask<T>> readingDataTasks = new ArrayList<>();
        for (int i = 0; i < munCores; i++) {
            startIndex = i * step;
            if (i == munCores - 1) {
                endWithIndex = size;
            } else {
                endWithIndex = (i + 1) * step;
            }
            setStartRow(startIndex);
            setEndWithRow(endWithIndex);
            List<T> entities = readImpl(filePath);
            ReadingDataTask<T> task = new ReadingDataTask<>(endWithIndex, entities);
            readingDataTasks.add(task);
        }
        return readingDataTasks;
    }

    /**
     * 将Excel获取的数据以Json字符串形式存入List集合中
     *
     * @param file 文件路径
     * @return {@code List<String>}
     * @throws IOException          IO流异常
     * @throws NoSuchFieldException 如果找不到单元格对应的成员属性,也就是{@code   entityTableDefinition.getCellNameAndField().get(title)}为null时抛出此异常。
     *                              需要考虑{@link ExcelField}
     */
    @SuppressWarnings("unchecked")
    private List<T> readImpl(String file) throws IOException, NoSuchFieldException, UnsupportedFileException {
        Class<?> entity = entityTableDefinition.getClassName();
        List<T> objects = new ArrayList<>();
        try {
            sheet = sheet == null ? super.getSheet(file) : sheet;
            maxRow = (maxRow == 0) ? sheet.getLastRowNum() : maxRow;
            endWithRow = endWithRow == -1 ? maxRow : endWithRow;
            maxCol = sheet.getRow(titleLine).getLastCellNum();
        } catch (NotOLE2FileException e) {
            System.out.println("This error may occur if you are using HSSFWorkbook to read CSV files," +
                    " as HSSFWorkbook is primarily used to handle Excel file formats based on OLE2 (Object Linking and Embedding)," +
                    " Instead of a plain text CSV file.\n" +
                    "You should use Apache Commons CSV or direct Java file read operations to read CSV files, " +
                    "which is much simpler and more efficient. ");
            System.out.println(("Reading file failed"));
            throw e;
        } catch (Exception e) {
            System.out.println(("Reading file failed"));
            throw e;
        }
        super.setEndWithCol(maxCol);
        super.readTitle(sheet);
        for (int row = startRow; row < endWithRow; row++) {
            //如果是标题行则跳过
            if (row == titleLine) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            for (int rol = 0; rol < maxCol; rol++) {
                Cell info = sheet.getRow(row).getCell(rol);
                Object o = getCellValue(info);
                String title = titles.get(rol);
                Field field = entityTableDefinition.getCellNameAndField().get(title);
                if (field == null) {
                    String err = "The field \"" + title + "\" does not exists in this field in class:" +
                            entityTableDefinition.getClassName().getSimpleName();
                    throw new NoSuchFieldException(err);
                }
                String filedName = field.getName();
                map.put(filedName, o);
            }
            String jsonString = JSON.toJSONString(map);
            Object o = JSONObject.parseObject(jsonString, entity);
            try {
                objects.add((T) o);
            } catch (ClassCastException e) {
                System.out.println("Loading info failed in line " + row);
            }
        }
        //默认初始行和截至行
        startRow = 0;
        endWithRow = maxRow;
        return objects;
    }

    private void bsOutPutFileImpl(String sheetName, File file, List<T> entities) throws IOException, UnsupportedFileException {
        if (entities.isEmpty()) {
            throw new NullPointerException("The list of entities is empty.");
        }
        if (excelTool.checkIsDirectory(file)) {
            String filepath = file.getAbsolutePath() + File.separator + UUID.randomUUID() + ".xlsx";
            file = new File(filepath);
        }
        excelTool.checkSuffix(file);
        super.getOutputWorkbook(file);
        Workbook wb = getOutputWorkbook(file);
        Sheet sheet = wb.createSheet(sheetName);
        TreeMap<Integer, Field> fieldTreeMap = entityTableDefinition.getIndexForCellName();
        // 写入标题行
        Row titleRow = sheet.createRow(0);
        int cellIndex = 0;
        for (Map.Entry<Integer, Field> entry : fieldTreeMap.entrySet()) {
            Field field = entry.getValue();
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            String title = (excelField != null && !excelField.value().isEmpty()) ? excelField.value() : field.getName();
            titleRow.createCell(cellIndex++).setCellValue(title);
        }
        int rowIndex = 1;
        for (T entity : entities) {
            Row row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            for (Map.Entry<Integer, Field> entry : fieldTreeMap.entrySet()) {
                Field field = entry.getValue();
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    String valueString = value == null ? "" : value.toString();
                    if (value instanceof Date) {
                        // 指定日期格式
                        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
                        valueString = dateFormat.format((Date) value);
                    }
                    row.createCell(columnIndex).setCellValue(valueString);
                    columnIndex++;
                } catch (IllegalAccessException e) {
                    System.out.println("Error accessing field: " + field.getName());
                }
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            wb.write(fileOut);
            System.out.println("The file is successfully exported and saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new IOException("File export failure: " + e.getMessage());
        }
    }

    /**
     * 设置起始列，在此类中不适用
     *
     * @param startCol 截止列
     */
    @Override
    @Deprecated
    public void setStartCol(int startCol) {
        System.out.println(("This method is deprecated in the class,"));
    }

    /**
     * 设置截止列，在此类中不适用
     *
     * @param endWithCol 截止列
     */
    @Deprecated
    @Override
    public void setEndWithCol(int endWithCol) {
        System.out.println(("This method is deprecated in the class,"));
    }

    @Override
    public List<Map<String, Object>> readToList(File file) throws IOException, ExecutionException, NoSuchFieldException, InterruptedException {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<T> entities = getReadData(file);
        Field[] fields = entityTableDefinition.getFields();
        for (T entity : entities) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(entity));
                } catch (IllegalAccessException e) {
                    System.out.println("Add entities failed:" + e.getMessage());
                    System.out.println("Entity:" + entity + ",Field:" + field.getName());
                }
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public Object[][] readToArray(String path) throws IOException, ExecutionException, NoSuchFieldException, InterruptedException {
        Object[][] objects = new Object[endWithRow - startRow][maxCol];
        List<T> entities = getReadData(path);
        Field[] fields = entityTableDefinition.getFields();
        for (int i = startRow; i < endWithRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                try {
                    if (i >= entities.size() || j > fields.length) {
                        objects[i][j] = null;
                        continue;
                    }
                    fields[j].setAccessible(true);
                    Object o = fields[j].get(entities.get(i));
                    objects[i][j] = o;
                } catch (IllegalAccessException e) {
                    System.out.println("Add entities failed:" + e.getMessage());
                    System.out.println("Entity:" + entities.get(i) + ",Field:" + fields[j].getName());
                }
            }
        }
        return objects;
    }

    @Override
    public Object[][] readToArray(File file) throws IOException, ExecutionException, NoSuchFieldException, InterruptedException {
        return readToArray(file.getAbsolutePath());
    }

    @Override
    public List<Map<String, Object>> readToList(String path) throws IOException, ExecutionException, NoSuchFieldException, InterruptedException {
        File file = new File(path);
        return readToList(file);
    }

    @Override
    @Deprecated
    public Object[][] readToArray() {
        return null;
    }

    @Override
    @Deprecated
    public List<Map<String, Object>> readToList() {
        return null;
    }


    /**
     * 如果了解多线程读取{@link #getReadData(File)}
     *
     * @param path 文件路径
     * @return {@code List<T>}
     * @throws IOException IO流异常
     */
    public List<T> readAll(String path) throws IOException, NoSuchFieldException, UnsupportedFileException {
        return readImpl(path);
    }

    /**
     * 果了解多线程读取{@link #getReadData(File)}
     */
    public List<T> readAll(File file) throws IOException, NoSuchFieldException, UnsupportedFileException {
        return readAll(file.getAbsolutePath());
    }

    /**
     * 读一个数据
     *
     * @param file  文件
     * @param index 行数
     * @return T
     * @throws IOException          IO流异常
     * @throws NoSuchFieldException 如果找不到单元格对应的成员属性,也就是{@code   entityTableDefinition.getCellNameAndField().get(title)}为null时抛出此异常。
     *                              需要考虑{@link ExcelField}
     */
    public T readOne(File file, int index) throws IOException, NoSuchFieldException, UnsupportedFileException {
        return readOne(file.getAbsolutePath(), index);
    }

    /**
     * 读一个数据
     *
     * @param filePath 文件路径
     * @param index    行数
     * @return T
     * @throws IOException          IO流异常
     * @throws NoSuchFieldException 如果找不到单元格对应的成员属性,也就是{@code   entityTableDefinition.getCellNameAndField().get(title)}为null时抛出此异常。
     *                              需要考虑{@link ExcelField}
     */
    public T readOne(String filePath, int index) throws IOException, NoSuchFieldException, UnsupportedFileException {
        if (index == titleLine) {
            throw new IllegalArgumentException("Don't know how to turn title line" + " into class " + entityTableDefinition.getClassName().getSimpleName());
        }
        super.setStartRow(index);
        super.setEndWithRow(index + 1);
        return readImpl(filePath).get(0);
    }

    /**
     * 采用多线程的方式读取文件,并将结果合并在一起
     *
     * @param file 文件
     * @return {@link List}
     * @throws IOException          IO流异常
     * @throws NoSuchFieldException 如果找不到单元格对应的成员属性,也就是{@code   entityTableDefinition.getCellNameAndField().get(title)}为null时抛出此异常。
     *                              需要考虑{@link ExcelField}
     * @throws InterruptedException 中断异常
     */
    public List<T> getReadData(File file) throws IOException, ExecutionException, NoSuchFieldException, InterruptedException, UnsupportedFileException {
        String path = file.getAbsolutePath();
        return getReadData(path);
    }

    /**
     * 采用多线程的方式读取文件,并将结果合并在一起
     *
     * @param path 文件路径
     * @return {@link List}
     * @throws IOException          IO流异常
     * @throws NoSuchFieldException 如果找不到单元格对应的成员属性,也就是{@code   entityTableDefinition.getCellNameAndField().get(title)}为null时抛出此异常。
     *                              需要考虑{@link ExcelField}
     * @throws InterruptedException 中断异常
     */
    public List<T> getReadData(String path) throws IOException, ExecutionException, NoSuchFieldException, InterruptedException, UnsupportedFileException {
        ReadData<T> readData = getReadDataIml(path);
        return readData.getData();
    }

    /**
     * 将实体类集合输出到Excel表中
     *
     * @param path     文件路径
     * @param entities 实体类集合
     * @throws IllegalArgumentException 见{@link ExcelTool}
     */
    public void write(List<T> entities, String path) throws IOException, UnsupportedFileException {
        write(entities, path, SHEET_NAME);
    }

    /**
     * 将实体类集合输出到Excel表中
     *
     * @param file     文件
     * @param entities 实体类集合
     * @throws IllegalArgumentException 见{@link ExcelTool}
     */
    public void write(List<T> entities, File file) throws IOException, UnsupportedFileException {
        write(entities, file, SHEET_NAME);
    }

    /**
     * 将实体类集合输出到Excel表中
     *
     * @param path      文件路径
     * @param sheetName 自定义Sheet名字
     * @param entities  实体类集合
     * @throws IOException              IO流异常
     * @throws IllegalArgumentException 见{@link ExcelTool}
     */
    public void write(List<T> entities, String path, String sheetName) throws IOException, UnsupportedFileException {
        File file = new File(path);
        bsOutPutFileImpl(sheetName, file, entities);
    }

    /**
     * 将实体类集合输出到Excel表中
     *
     * @param file      文件
     * @param sheetName 自定义Sheet名字
     * @param entities  实体类集合
     * @throws IOException              IO流异常
     * @throws IllegalArgumentException 见{@link ExcelTool}
     */
    public void write(List<T> entities, File file, String sheetName) throws IOException, UnsupportedFileException {
        bsOutPutFileImpl(sheetName, file, entities);
    }
}