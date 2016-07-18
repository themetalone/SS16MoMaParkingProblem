package com.github.themetalone.parking.analysis.exceptions;

/**
 * Created by steff on 15.07.2016.
 */
public class NoFurtherObject extends Exception {
    public NoFurtherObject() {
        super();
    }

    public NoFurtherObject(String message) {
        super(message);
    }

    public NoFurtherObject(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFurtherObject(Throwable cause) {
        super(cause);
    }

    protected NoFurtherObject(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
