package com.bloducspauter.excel.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ReadingDataTask<T> implements Callable<ReadData<T>> {
    private final int maxRow;
    private final List<T> data;
    public ReadingDataTask(int maxRow, List<T> data, int startRow ) {
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
