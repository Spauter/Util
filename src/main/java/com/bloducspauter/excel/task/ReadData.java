package com.bloducspauter.excel.task;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取最终结果
 * @author Bloduc Spauter
 * @version 1.18
 */
@Setter
@Getter
public class ReadData<T> {
    private List<T>data;
    private int maxRol;
}
