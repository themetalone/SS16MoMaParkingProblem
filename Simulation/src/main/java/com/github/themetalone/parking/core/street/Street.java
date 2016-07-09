package com.github.themetalone.parking.core.street;

import com.github.themetalone.parking.core.ModelObject;
import com.github.themetalone.parking.core.car.Car;

/**
 * Created by steff on 06.07.2016.
 */
public interface Street extends ModelObject{

    /**
     * Let's a car enter the street
     * @param car the car to enter the street
     */
    void enterLane(Car car);

    /**
     * Execute one discrete time step
     */
    void tick();

}
