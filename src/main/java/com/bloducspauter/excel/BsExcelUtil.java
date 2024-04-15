package com.bloducspauter.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bloducspauter.excel.task.ReadData;
import com.bloducspauter.excel.task.ReadingDataTask;
import com.bloducspauter.origin.init.MyAnnotationConfigApplicationContext;
import com.bloducspauter.origin.init.TableDefinition;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 使用Json字符串接收Excel表格数据,继承自{@link ExcelUtil}
 *
 * @author Bloduc Spauter
 */
public class BsExcelUtil<T> extends ExcelUtil {


    private final TableDefinition entityTableDefinition;

    /**
     * 初始化实体类,使用{@link MyAnnotationConfigApplicationContext#getTableDefinition(Class)}
     * <p>
     * 读取该类的{@link com.bloducspauter.annotation.ExcelTable}和{@link com.bloducspauter.annotation.ExcelField}
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
     * @see #readFile(String)
     * @param filePath 文件路径
     */
    public ReadData<T> getRreadData(String filePath) throws IOException, InterruptedException, ExecutionException {
        sheet = super.getSheet(filePath);
        maxRow = maxRow == 0 ? sheet.getLastRowNum() : maxRow;
        //获取CPU线程数
        int munCores = Runtime.getRuntime().availableProcessors();
        List<T> entities = new ArrayList<>();
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(munCores);
        List<ReadingDataTask<T>> readingDataTasks = getReadingDataTasks(munCores, filePath);
        //将上面创建的FutureTask提交给Executor线程执行,如果任务没有完成,invokeALl()阻塞处处
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


    private List<ReadingDataTask<T>> getReadingDataTasks(int munCores, String filePath) throws IOException {
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
            ReadingDataTask<T> task = new ReadingDataTask<>(endWithIndex, entities, startIndex);
            readingDataTasks.add(task);
        }
        return readingDataTasks;
    }

    /**
     * 将Excel获取的数据以Json字符串形式存入List集合中
     *
     * @param file 文件路径
     * @return {@code List<String>}
     * @throws IOException IO流异常
     */
    @SuppressWarnings("unchecked")
    public List<T> readImpl(String file) throws IOException {
        Class<?> entity = entityTableDefinition.getClassName();
        List<T> objects = new ArrayList<>();
        try {
            sheet = sheet == null ? super.getSheet(file) : sheet;
            maxRow = (maxRow == 0) ? sheet.getLastRowNum() : maxRow;
            endWithRow = endWithRow == -1 ? maxRow : endWithRow;
            maxCol = sheet.getRow(titleLine).getLastCellNum();
        } catch (NotOLE2FileException e) {
            System.out.println("This error may occur if you are using HSSFWorkbook to read CSV files, as HSSFWorkbook is primarily used to handle Excel file formats based on OLE2 (Object Linking and Embedding), Instead of a plain text CSV file.\n" +
                    "You should use Apache Commons CSV or direct Java file read operations to read CSV files, which is much simpler and more efficient. Here is sample code for reading a CSV file using Apache Commons CSV:");
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
                    String err = "THe field \"" + title + "\" does not exists in this field in class:" +
                            entityTableDefinition.getClassName().getName();
                    throw new IllegalArgumentException(err);
                }
                String filedName = field.getName();
                map.put(filedName, o);
            }
            String jsonString = JSON.toJSONString(map);
            Object o = JSONObject.parseObject(jsonString, entity);
            objects.add((T) o);
        }
        return objects;
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


    /**
     * 如果了解多线程读取{@link #getRreadData(String)}
     * @param path 文件路径
     * @return {@code List<T>}
     * @throws IOException IO流异常
     */
    public List<T> readFile(String path) throws IOException {
        return readImpl(path);
    }

    /**
     * 果了解多线程读取{@link #getRreadData(String)}
     */
    public List<T> readFile(File file) throws IOException {
        return readFile(file.getAbsolutePath());
    }


    public void bsOutPutFile(String sheetName, String path, List<T> entities) throws NoSuchFieldException, IllegalAccessException {
        //TODO
    }

    public void bsOutPutFile(String sheetName, File file, List<T> entities) {
        //TODO
    }

}
