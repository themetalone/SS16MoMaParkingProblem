package com.github.themetalone.parking.core.car;

import com.github.themetalone.parking.core.ModelProvider;
import com.github.themetalone.parking.core.exceptions.NoNextCarException;

/**
 * The base interface for the car provider
 * Created by steff on 06.07.2016.
 */
public interface CarProvider extends ModelProvider<Car> {

    /**
     *
     * @return the next car
     * @throws NoNextCarException if no car can be returned
     */
    Car next() throws NoNextCarException;

}
