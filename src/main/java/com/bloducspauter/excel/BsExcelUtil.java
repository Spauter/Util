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
 * @version 1.18
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

    public ReadData<T> getReadData(File file) throws IOException, ExecutionException, NoSuchFieldException, InterruptedException {
        return getRreadData(file.getAbsolutePath());
    }

    /**
     * 读物excel表格，采用多线程方式
     *
     * @param filePath 文件路径
     * @see #readFile(String)
     */
    public ReadData<T> getRreadData(String filePath) throws IOException, InterruptedException, ExecutionException, NoSuchFieldException {
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


    private List<ReadingDataTask<T>> getReadingDataTasks(int munCores, String filePath) throws IOException, NoSuchFieldException {
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
     * @throws IOException IO流异常
     */
    @SuppressWarnings("unchecked")
    private List<T> readImpl(String file) throws IOException, NoSuchFieldException {
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
            }catch (ClassCastException e) {
                System.out.println("Loading info failed in line "+row);
            }
        }
        return objects;
    }

    public boolean bsOutputImpl(String sheetName,List<T> entities,String filePath) throws IOException, NoSuchFieldException {
        return true;
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
     *
     * @param path 文件路径
     * @return {@code List<T>}
     * @throws IOException IO流异常
     */
    public List<T> readFile(String path) throws IOException, NoSuchFieldException {
        return readImpl(path);
    }

    /**
     * 果了解多线程读取{@link #getRreadData(String)}
     */
    public List<T> readFile(File file) throws IOException, NoSuchFieldException {
        return readFile(file.getAbsolutePath());
    }

    public T readOne(File file,int index) throws IOException, NoSuchFieldException {
        return readOne(file.getAbsolutePath(),index);
    }

    public T readOne(String filePath, int index) throws IOException, NoSuchFieldException {
        if (index == titleLine) {
            throw new IllegalArgumentException("Don't know how to turn title line"+ " into class " + entityTableDefinition.getClassName().getSimpleName());
        }
        super.setStartRow(index);
        super.setEndWithRow(index+1);
        return readImpl(filePath).get(0);
    }

    public void bsOutPutFile(String sheetName, String path, List<T> entities) throws NoSuchFieldException, IllegalAccessException {
        for (T entity : entities) {
            Class<?>entityClass = entity.getClass();
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                System.out.println(field.get(entity));
            }
        }
        //TODO
    }

    public void bsOutPutFile(String sheetName, File file, List<T> entities) {
        //TODO
    }
}    
