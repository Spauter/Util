package com.bloducspauter.excelutil.ewxce;

import com.bloducspauter.excelutil.ewxce.wrapper.ReadWrapper;
import com.bloducspauter.excelutil.ewxce.write.EncryptExcel;
import com.bloducspauter.excelutil.origin.BsExcelUtil;
import com.bloducspauter.excelutil.origin.task.ReadData;
import com.bloducspauter.excelutil.origin.task.ReadingDataTask;
import com.bloducspauter.excelutil.ewxce.read.ReadDataByThreads;
import com.bloducspauter.excelutil.ewxce.read.RowDataReader;
import com.bloducspauter.excelutil.base.init.FiledPropertyLoader;
import com.bloducspauter.excelutil.base.init.TableDefinition;
import com.bloducspauter.excelutil.ewxce.wrapper.WriteWrapper;
import lombok.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 严格读取工具，需要{@code TableDefinition}的字段进行反射
 * @author Bloduc Spauter
 * @since 1.19
 */
public class WrapperExcelUtil<T> extends RawUseWrapperExcelUtil{
    TableDefinition tableDefinition = null;
    /**
     * 初始化实体类,使用{@link FiledPropertyLoader#getTableDefinition(Class)}
     * <p>
     * 将相关结果存入{@link TableDefinition}
     *
     * @param entity 实体类，可以为空
     */
    public WrapperExcelUtil(Class<?> entity,@NonNull ReadWrapper readWrapper) throws Exception {
        super(readWrapper);
        if (entity == null) {
            return;
        }
        tableDefinition = FiledPropertyLoader.getTableDefinition(entity);
    }

    private List<T> read() throws Exception {
        super.init();
        if (tableDefinition == null) {
            throw new NullPointerException("TableDefinition is null, please check your entity class");
        }
        List<T> objects = new ArrayList<>();
        String dateformat = wrapper.getDateformat();

        for (int row = startRow; row < endRow; row++) {
            if (row == wrapper.getTitleLine()) {
                continue;
            }
            try {
                T entity= RowDataReader.readToEntity(sheetReader.getSheet(wrapper.getSheetIndex()),tableDefinition,titleMap,row,startColumn,maxColumn,dateformat);
                objects.add(entity);
            } catch (ClassCastException | IOException e) {
                System.out.println("Loading info failed in line " + row + ": " + e.getMessage());
            }
        }
        startRow = 0;
        endRow = maxRow;
        return objects;
    }

    /**
     *  读取数据，返回{@code Map<String,Object>},结果 Map 的键是类中字段的名字，需要验证和严格映射字段
     */
    public List<Map<String,Object>>readFiledKeyMap() throws Exception {
        super.init();
        if (tableDefinition == null) {
            throw new NullPointerException("TableDefinition is null because entity class is null.Please check your entity class");
        }
        List<Map<String,Object>>objects=new ArrayList<>();
        String dateformat = wrapper.getDateformat();
        for (int row = startRow; row < endRow; row++) {
            if (row == wrapper.getTitleLine()) {
                continue;
            }
            Map<String, Object> map = RowDataReader.readToFieldKeyMap(
                    sheetReader.getSheet(wrapper.getSheetIndex()),
                    tableDefinition, titleMap,
                    row, startColumn, maxColumn,
                    dateformat);
            objects.add(map);
        }
        startRow = 0;
        endRow = maxRow;
        return objects;
    }

    private ReadData<T> readDataImpl() throws Exception {
        int munCores = Runtime.getRuntime().availableProcessors();
        List<T> entities = new ArrayList<>();
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(munCores);
        List<ReadingDataTask<T>> readingDataTasks = getReadingDataTasks(munCores);
        return new ReadDataByThreads<T>().read(threadPoolExecutor, readingDataTasks, entities);
    }

    private List<ReadingDataTask<T>> getReadingDataTasks(int munCores) throws Exception {
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
            this.startRow = startIndex;
            this.endRow = endWithIndex;
            List<T> entities = read();
            ReadingDataTask<T> task = new ReadingDataTask<>(endWithIndex, entities);
            readingDataTasks.add(task);
        }
        return readingDataTasks;
    }


    /**
     * 通过多线程读取数据
     */
    public List<T> readData() throws Exception {
        //如果总数比较小，不采用多线程
        if (maxRow <= 128) {
            return read();
        }
        ReadData<T>readData=readDataImpl();
        return readData.getData();
    }

    /**
     * 读取所有数据
     */
    public List<T> readAll() throws Exception {
        return read();
    }

    /**
     * 读取一条
     *
     * @param index 行数
     */
    public T readOne(int index) throws Exception {
        if (index == wrapper.getTitleLine()) {
            throw new IllegalArgumentException("Don't know how to turn title line" + " into class " + tableDefinition.getClassName().getSimpleName());
        }
        return read().get(index);
    }

    public void write(WriteWrapper wrapper, List<T> entities) throws Exception {
        new BsExcelUtil<T>(tableDefinition.getClassName()).write(entities,wrapper.getPath());
        if(wrapper.getPassword()!=null) {
            EncryptExcel.encryptExcl(wrapper.getPath(),wrapper.getPassword());
        }
    }
}
