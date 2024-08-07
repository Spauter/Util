package com.bloducspauter.excelutil.base.exceptions;
/**
 * 不支持格式异常
 * @author Bloduc Spauter
 *
 */
public class UnsupportedFileException extends IllegalArgumentException{
    private static final long serialVersionUID = 184539654L;

    /**
     * 构造具有详细消息的异常
     * @param message 异常消息
     */
    public UnsupportedFileException(String message){
        super(message);
    }
}
