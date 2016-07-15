package com.github.themetalone.parking.core.exceptions;

/**
 * Created by steff on 14.07.2016.
 */
public class NoNextObjectException extends Exception {
    public NoNextObjectException() {
        super();
    }

    public NoNextObjectException(String message) {
        super(message);
    }

    public NoNextObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoNextObjectException(Throwable cause) {
        super(cause);
    }

    protected NoNextObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
