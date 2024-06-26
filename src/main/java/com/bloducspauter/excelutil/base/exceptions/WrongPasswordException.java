package com.bloducspauter.excelutil.base.exceptions;

/**
 * @author Bloduc Spauter
 *
 */
public class WrongPasswordException extends IllegalArgumentException{

    private static final long serialVersionUID = 18454893156L;

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPasswordException(Throwable cause) {
        super(cause);
    }

    public WrongPasswordException() {
        super();
    }
}
