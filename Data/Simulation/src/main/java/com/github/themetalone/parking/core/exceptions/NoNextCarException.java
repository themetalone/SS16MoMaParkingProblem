package com.github.themetalone.parking.core.exceptions;

/**
 * Created by steff on 14.07.2016.
 */
public class NoNextCarException extends NoNextObjectException {
    public NoNextCarException() {
        super();
    }

    public NoNextCarException(String message) {
        super(message);
    }

    public NoNextCarException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoNextCarException(Throwable cause) {
        super(cause);
    }

    protected NoNextCarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
