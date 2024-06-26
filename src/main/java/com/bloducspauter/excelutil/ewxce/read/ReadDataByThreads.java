package com.bloducspauter.excelutil.ewxce.read;

import com.bloducspauter.excelutil.origin.task.ReadData;
import com.bloducspauter.excelutil.origin.task.ReadingDataTask;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Bloduc Spauter
 * @version 1.19
 */
public class ReadDataByThreads<T> {
    /**
     * 用于并行读取多个数据源，并将读取到的所有数据合并到一个列表中
     * @param threadPoolExecutor 线程池
     * @param readingDataTasks 读取数据的任务
     * @param entities 存储读取到的数据
     */
    public ReadData<T> read(ThreadPoolExecutor threadPoolExecutor, List<ReadingDataTask<T>> readingDataTasks,
                            List<T> entities) throws InterruptedException, ExecutionException {
        // 创建一个 Future 列表，用于存储线程池中执行的任务
        List<Future<ReadData<T>>> futures = threadPoolExecutor.invokeAll(readingDataTasks);
        //任务完成了,停止所有任务;
        threadPoolExecutor.shutdown();
        // 遍历 Future 列表，获取每个任务的执行结果
        for (Future<ReadData<T>> future : futures) {
            ReadData<T> readData = future.get();
            entities.addAll(readData.getData());
        }
        // 创建一个 ReadData 对象，将合并后的数据返回
        ReadData<T> readData = new ReadData<>();
        readData.setData(entities);
        readData.setMaxRol(entities.size());
        return readData;
    }
}
