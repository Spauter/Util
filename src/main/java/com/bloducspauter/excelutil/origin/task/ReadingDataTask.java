package com.bloducspauter.excelutil.origin.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @version 1.18
 * @param <T> 其实是实体类
 * @author Bloduc Spauter
 */
public class ReadingDataTask<T> implements Callable<ReadData<T>> {
    private final int maxRow;
    private final List<T> data;
    public ReadingDataTask(int maxRow, List<T> data ) {
        this.maxRow = maxRow;
        this.data = data;
    }

    @Override
    public ReadData<T> call() {
        List<T> entities = new ArrayList<>(data);
        ReadData<T> readData=new ReadData<>();
        readData.setData(entities);
        readData.setMaxRol(maxRow);
        return readData;
    }
}
