package com.github.themetalone.parking.analysis.exceptions;

/**
 * Created by steff on 15.07.2016.
 */
public class NoFurtherCar extends NoFurtherObject {
    public NoFurtherCar() {
        super();
    }

    public NoFurtherCar(String message) {
        super(message);
    }

    public NoFurtherCar(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFurtherCar(Throwable cause) {
        super(cause);
    }

    protected NoFurtherCar(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
