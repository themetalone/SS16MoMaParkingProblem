package com.github.themetalone.parking.core.car;

import com.github.themetalone.parking.core.ModelProvider;

/**
 * Created by steff on 06.07.2016.
 */
public interface CarProvider extends ModelProvider<Car> {

    Car next();

}
