package com.github.themetalone.parking.analysis.data;

import com.github.themetalone.parking.analysis.exceptions.NoFurtherCar;
import com.github.themetalone.parking.analysis.exceptions.NoFurtherObject;
import com.github.themetalone.parking.analysis.model.CarEntry;
import com.github.themetalone.parking.analysis.model.ParkingStateEntry;
import com.github.themetalone.parking.core.car.Car;

/**
 * Created by steff on 15.07.2016.
 */
public interface DataAccess {

    CarEntry nextCar() throws NoFurtherCar;

    ParkingStateEntry nextParkingState() throws NoFurtherObject;

    void close();


}
