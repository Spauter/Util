package com.bloducspauter.excel.task;

import java.util.List;

/**
 * 获取最终结果
 * @author Bloduc Spauter
 * @version 1.18
 */
public class ReadData<T> {
    private List<T>data;
    private int maxRol;


    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getMaxRol() {
        return maxRol;
    }

    public void setMaxRol(int maxRol) {
        this.maxRol = maxRol;
    }
}
