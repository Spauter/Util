package com.bloducspauter.origin.exceptions;
/**
 * 不支持格式异常
 * @author Bloduc Spauter
 *
 */
public class UnsupportedFileException extends IllegalAccessException{
    private static final long serialVersionUID = 184539654L;

    public UnsupportedFileException(String message){
        super(message);
    }

}
